package com.scejtesting.core.config;

import org.junit.Assert;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * Created by aleks on 7/10/14.
 */
public class ExceptionsTest {

    private static class TestException extends IOException {
    }

    @org.junit.Test
    public void positiveFlowTest() {

        List<Class<? extends Exception>> exceptionsToCheck = Arrays.asList(
                IOException.class,
                IllegalArgumentException.class);

        Exceptions exceptions = spy(new Exceptions());
        doReturn(exceptionsToCheck).when(exceptions).getExceptions();

        Assert.assertFalse(exceptions.isRegistered(new Throwable()));
        Assert.assertTrue(exceptions.isRegistered(new IOException()));
        Assert.assertTrue(exceptions.isRegistered(new IllegalArgumentException()));
        Assert.assertTrue(exceptions.isRegistered(new TestException()));

    }

    @org.junit.Test
    public void testEmptyExceptionList() {
        Exceptions exceptions = new Exceptions();

        Assert.assertFalse(exceptions.isRegistered(new Throwable()));
        Assert.assertFalse(exceptions.isRegistered(new IOException()));
        Assert.assertFalse(exceptions.isRegistered(new IllegalArgumentException()));
        Assert.assertFalse(exceptions.isRegistered(new TestException()));

    }
}
