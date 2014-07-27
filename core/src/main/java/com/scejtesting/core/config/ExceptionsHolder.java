package com.scejtesting.core.config;

import org.concordion.internal.util.Check;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User: Fedorovaleks
 * Date: 16.03.14
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ExceptionsHolder {

    @XmlElement(required = false)
    protected Exceptions exceptions;

    @XmlTransient
    private final AtomicReference<Throwable> thrownException = new AtomicReference<Throwable>(null);

    public Exceptions getExceptions() {
        return exceptions;
    }

    public Throwable getThrownException() {
        return thrownException.get();
    }

    public void setThrownException(Throwable thrownException) {
        Check.notNull(thrownException, "Exception can't be null");
        this.thrownException.set(thrownException);
    }
}
