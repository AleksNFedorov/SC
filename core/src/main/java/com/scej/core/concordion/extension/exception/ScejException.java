package com.scej.core.concordion.extension.exception;

/**
 * User: Fedorovaleks
 * Date: 16.03.14
 */
public abstract class ScejException extends RuntimeException {

    public ScejException(Throwable cause) {
        super(cause);
    }
}
