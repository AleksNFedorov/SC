package com.scejtesting.core.concordion.extension;


import com.scejtesting.core.config.Exceptions;
import com.scejtesting.core.config.Suite;
import com.scejtesting.core.config.Test;
import org.concordion.api.listener.ThrowableCaughtEvent;

import static org.mockito.Mockito.*;

/**
 * User: Fedorovaleks
 * Date: 3/20/14
 */
public class SuiteFailFastExceptionListenerTest {

    @org.junit.Test
    public void testNoGlobalExceptions() {

        SuiteFailFastExceptionListener listener = spy(new SuiteFailFastExceptionListener());
        Suite testSuite = mock(Suite.class);
        Test currentTest = mock(Test.class);

        Exceptions registeredExceptions = mock(Exceptions.class);
        when(registeredExceptions.isRegistered(any(Throwable.class))).thenReturn(Boolean.FALSE);

        when(testSuite.getExceptions()).thenReturn(registeredExceptions);
        when(currentTest.getExceptions()).thenReturn(registeredExceptions);


        doReturn(testSuite).when(listener).getSuite();
        doReturn(currentTest).when(listener).getCurrentTest();


        ThrowableCaughtEvent throwableCaughtEvent = mock(ThrowableCaughtEvent.class);

        when(throwableCaughtEvent.getThrowable()).thenReturn(new RuntimeException());

        listener.throwableCaught(throwableCaughtEvent);

        verify(testSuite, never()).setThrownException(any(Throwable.class));
        verify(currentTest, never()).setThrownException(any(Throwable.class));

    }

    @org.junit.Test
    public void testSuiteOnlyExceptions() {

        SuiteFailFastExceptionListener listener = spy(new SuiteFailFastExceptionListener());
        Suite testSuite = mock(Suite.class);
        Test currentTest = mock(Test.class);

        Exceptions testExceptions = mock(Exceptions.class);
        when(testExceptions.isRegistered(any(Throwable.class))).thenReturn(Boolean.FALSE);

        Exceptions suiteExceptions = mock(Exceptions.class);
        when(suiteExceptions.isRegistered(any(Throwable.class))).thenReturn(Boolean.TRUE);


        when(testSuite.getExceptions()).thenReturn(suiteExceptions);
        when(currentTest.getExceptions()).thenReturn(testExceptions);


        doReturn(testSuite).when(listener).getSuite();
        doReturn(currentTest).when(listener).getCurrentTest();


        ThrowableCaughtEvent throwableCaughtEvent = mock(ThrowableCaughtEvent.class);

        when(throwableCaughtEvent.getThrowable()).thenReturn(new RuntimeException());

        listener.throwableCaught(throwableCaughtEvent);

        verify(testSuite, atMost(1)).setThrownException(any(Throwable.class));
        verify(testSuite, atLeast(1)).setThrownException(any(Throwable.class));
        verify(currentTest, never()).setThrownException(any(Throwable.class));

    }

    @org.junit.Test
    public void testTestOnlyExceptions() {

        SuiteFailFastExceptionListener listener = spy(new SuiteFailFastExceptionListener());
        Suite testSuite = mock(Suite.class);
        Test currentTest = mock(Test.class);

        Exceptions suiteExceptions = mock(Exceptions.class);
        when(suiteExceptions.isRegistered(any(Throwable.class))).thenReturn(Boolean.FALSE);

        Exceptions testExceptions = mock(Exceptions.class);
        when(testExceptions.isRegistered(any(Throwable.class))).thenReturn(Boolean.TRUE);


        when(testSuite.getExceptions()).thenReturn(suiteExceptions);
        when(currentTest.getExceptions()).thenReturn(testExceptions);


        doReturn(testSuite).when(listener).getSuite();
        doReturn(currentTest).when(listener).getCurrentTest();


        ThrowableCaughtEvent throwableCaughtEvent = mock(ThrowableCaughtEvent.class);

        when(throwableCaughtEvent.getThrowable()).thenReturn(new RuntimeException());

        listener.throwableCaught(throwableCaughtEvent);

        verify(currentTest, atMost(1)).setThrownException(any(Throwable.class));
        verify(currentTest, atLeast(1)).setThrownException(any(Throwable.class));
        verify(testSuite, never()).setThrownException(any(Throwable.class));

    }

    @org.junit.Test
    public void testBothExceptions() {

        SuiteFailFastExceptionListener listener = spy(new SuiteFailFastExceptionListener());
        Suite testSuite = mock(Suite.class);
        Test currentTest = mock(Test.class);

        Exceptions suiteExceptions = mock(Exceptions.class);
        when(suiteExceptions.isRegistered(any(Throwable.class))).thenReturn(Boolean.TRUE);

        Exceptions testExceptions = mock(Exceptions.class);
        when(testExceptions.isRegistered(any(Throwable.class))).thenReturn(Boolean.TRUE);


        when(testSuite.getExceptions()).thenReturn(suiteExceptions);
        when(currentTest.getExceptions()).thenReturn(testExceptions);


        doReturn(testSuite).when(listener).getSuite();
        doReturn(currentTest).when(listener).getCurrentTest();


        ThrowableCaughtEvent throwableCaughtEvent = mock(ThrowableCaughtEvent.class);

        when(throwableCaughtEvent.getThrowable()).thenReturn(new RuntimeException());

        listener.throwableCaught(throwableCaughtEvent);

        verify(currentTest, atMost(1)).setThrownException(any(Throwable.class));
        verify(currentTest, atLeast(1)).setThrownException(any(Throwable.class));
        verify(testSuite, atMost(1)).setThrownException(any(Throwable.class));
        verify(testSuite, atLeast(1)).setThrownException(any(Throwable.class));

    }
}
