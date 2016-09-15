package io.nobt.logging;

import io.nobt.application.env.Config;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

@Plugin(name = "GitLabIssueAppender", category = "Core", elementType = "appender")
public class GitLabIssueAppender extends AbstractAppender {

    private static final Set<String> history = new HashSet<>();

    private final GitlabAPI api;
    private final Integer projectId;

    public GitLabIssueAppender(String name, Layout<? extends Serializable> layout, GitlabAPI api, Integer projectId) {
        super(name, null, layout);
        this.api = api;
        this.projectId = projectId;
    }

    @Override
    public void append(LogEvent logEvent) {

        final Boolean shouldReportErrorsAsIssues = Config.reportServerErrorsAsIssues().orElse(false);

        if (shouldReportErrorsAsIssues && logEvent.getThrown() != null) {
            final Throwable exception = logEvent.getThrown();

            final String issueTitle = new String(getLayout().toByteArray(logEvent), Charset.forName("UTF-8"));
            final String issueDescription = getStacktraceAsString(exception);
            final String formattedDescription = String.format("````java\n\r%s\n\r````", issueDescription);

            final String hashOfDescription = MD5Util.createMD5Hash(formattedDescription);

            if (!history.contains(hashOfDescription)) {
                try {
                    api.createIssue(projectId, 0, 0, "bug", formattedDescription, issueTitle);
                } catch (IOException e) {
                    LOGGER.error("Failed to create issue on GitLab", e);
                }

                history.add(hashOfDescription);
            }
        }
    }

    private String getStacktraceAsString(Throwable exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        exception.printStackTrace(writer);
        return stringWriter.toString();
    }

    @PluginFactory
    public static GitLabIssueAppender createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginAttribute("host") String host,
            @PluginAttribute("username") String username,
            @PluginAttribute("password") String password,
            @PluginAttribute("projectId") Integer projectId) {

        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }

        try {
            final GitlabSession session = GitlabAPI.connect(host, username, password);
            return new GitLabIssueAppender(name, layout, GitlabAPI.connect(host, session.getPrivateToken()), projectId);
        } catch (IOException e) {
            return null;
        }
    }
}
