package com.scejtesting.core.config;

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
    private Throwable thrownException;

    public Exceptions getExceptions() {
        return exceptions;
    }

    public Throwable getThrownException() {
        return thrownException;
    }

    public void setThrownException(Throwable thrownException) {
        Check.notNull(thrownException, "Exception can't be null");
        Check.isTrue(this.thrownException == null, "Exception already set");
        this.thrownException = thrownException;
    }
}
