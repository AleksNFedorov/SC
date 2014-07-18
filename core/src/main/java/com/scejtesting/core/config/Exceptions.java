package com.scejtesting.core.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Fedorovaleks
 * Date: 16.03.14
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Exceptions")
public class Exceptions {

    @XmlElement(required = false, name = "exception")
    protected List<Class<? extends Throwable>> exceptions;

    public boolean isRegistered(Throwable exceptionToCheck) {

        Class<? extends Throwable> exceptionCheckClass = exceptionToCheck.getCause().getClass();

        for (Class exception : getExceptions()) {
            if (exception.isAssignableFrom(exceptionCheckClass))
                return true;
        }
        return false;
    }

    public List<Class<? extends Throwable>> getExceptions() {
        return new ArrayList<Class<? extends Throwable>>(exceptions);
    }

}
