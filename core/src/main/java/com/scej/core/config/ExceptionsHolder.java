package com.scej.core.config;

import com.scej.core.concordion.extension.exception.ScejException;
import org.concordion.internal.util.Check;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * User: Fedorovaleks
 * Date: 16.03.14
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ExceptionsHolder {

    @XmlElement(required = false)
    protected Exceptions exceptions;

    @XmlTransient
    private ScejException thrownException;

    public Exceptions getExceptions() {
        return exceptions;
    }

    public void setThrownException(ScejException thrownException) {
        Check.notNull(thrownException, "Exception can't be null");
        Check.isTrue(this.thrownException == null, "Exception already set");
        Check.isTrue(getExceptions().isRegistered(thrownException.getCause()), "Exception is not registered");
        this.thrownException = thrownException;
    }

    public ScejException getThrownException() {
        return thrownException;
    }
}
