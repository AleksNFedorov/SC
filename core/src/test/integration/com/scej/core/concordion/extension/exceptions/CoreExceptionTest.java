package com.scej.core.concordion.extension.exceptions;

/**
 * User: Fedorovaleks
 * Date: 17.03.14
 */
public abstract class CoreExceptionTest {

    private static int echoCounter;

    private static int exceptionCounter;

    protected abstract Throwable buildException();

    public final boolean echo() {
        echoCounter++;
        return true;
    }

    public final boolean tryException() throws Throwable {
        exceptionCounter++;
        throw buildException();
    }


    public static void reset() {
        echoCounter = exceptionCounter = 0;
    }

    public static int getEchoCounter() {
        return echoCounter;
    }

    public static int getExceptionCounter() {
        return exceptionCounter;
    }
}
