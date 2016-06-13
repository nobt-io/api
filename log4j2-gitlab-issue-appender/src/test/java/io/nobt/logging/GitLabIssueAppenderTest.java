package io.nobt.logging;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.layout.PatternLayout;
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

        sut = new GitLabIssueAppender("appender-test", PatternLayout.createDefaultLayout(), apiMock, 1208905);
    }

    @Test
    public void shouldCreateExceptionFromLog() throws Exception {

        final LogEvent logEventMock = mock(LogEvent.class, new ReturnsMocks());

        when(logEventMock.getThrown()).thenReturn(new Exception("Test"));

        sut.append(logEventMock);

        verify(apiMock).createIssue(eq(1208905), eq(0), eq(0), eq("bug"), startsWith("````java\n\rjava.lang.Exception: Test"), anyString());
    }

    @Test
    public void shouldNotCreateTwoIssuesForSameStackTrace() throws Exception {

        final LogEvent logEventMock = mock(LogEvent.class, new ReturnsMocks());

        when(logEventMock.getThrown()).thenReturn(new Exception("Test"));

        sut.append(logEventMock);
        sut.append(logEventMock);

        verify(apiMock, times(1)).createIssue(eq(1208905), eq(0), eq(0), eq("bug"), startsWith("````java\n\rjava.lang.Exception: Test"), anyString());
    }
}