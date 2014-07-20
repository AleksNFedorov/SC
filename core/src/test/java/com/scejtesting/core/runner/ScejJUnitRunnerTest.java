package com.scejtesting.core.runner;

import org.junit.runner.Result;
import org.junit.runner.notification.RunNotifier;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

/**
 * User: Fedorovaleks
 * Date: 3/19/14
 */
public class ScejJUnitRunnerTest {


    @org.junit.Test
    public void testCommonFlow() {

        ScejJUnitRunner runner = spy(new ScejJUnitRunner(Class.class));

        Result successResultMock = mock(Result.class);
        when(successResultMock.getRunCount()).thenReturn(1);
        when(successResultMock.wasSuccessful()).thenReturn(true);

        doReturn(successResultMock).when(runner).runSuite();

        RunNotifier notifier = mock(RunNotifier.class);

        InOrder inOrder = inOrder(notifier);

        runner.run(notifier);

        inOrder.verify(notifier, calls(1)).fireTestRunStarted(runner.getDescription());
        inOrder.verify(notifier, calls(1)).fireTestRunFinished(successResultMock);
    }


}
