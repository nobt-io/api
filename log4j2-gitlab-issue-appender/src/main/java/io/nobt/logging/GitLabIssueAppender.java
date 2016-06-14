package io.nobt.logging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

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

        if (logEvent.getThrown() != null) {
            final Throwable exception = logEvent.getThrown();

            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            exception.printStackTrace(new PrintStream(baos));

            final String issueTitle = new String(getLayout().toByteArray(logEvent), Charset.forName("UTF-8"));
            final String issueDescription = getIssueDescription(baos);

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

    private String getIssueDescription(ByteArrayOutputStream baos) {
        try {
            return baos.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
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
