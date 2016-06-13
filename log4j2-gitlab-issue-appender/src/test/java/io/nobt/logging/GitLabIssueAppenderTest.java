package io.nobt.logging;

import org.apache.logging.log4j.core.LogEvent;
import org.gitlab.api.GitlabAPI;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsMocks;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.*;

public class GitLabIssueAppenderTest {

    private GitLabIssueAppender sut;
    private GitlabAPI apiMock;

    @Before
    public void setUp() throws Exception {

        apiMock = mock(GitlabAPI.class);

        sut = new GitLabIssueAppender("appender-test", null, apiMock, 1208905);
    }

    @Test
    public void shouldCreateExceptionFromLog() throws Exception {

        final LogEvent logEventMock = mock(LogEvent.class, new ReturnsMocks());

        when(logEventMock.getThrown()).thenReturn(new Exception("Test"));

        sut.append(logEventMock);

        verify(apiMock).createIssue(eq(1208905), eq(0), eq(0), eq("bug"), startsWith("java.lang.Exception: Test"), eq(""));
    }
}