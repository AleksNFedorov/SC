package com.scej.core.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collections;
import java.util.List;

/**
 * User: Fedorovaleks
 * Date: 16.03.14
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Exceptions")
public class Exceptions {

    @XmlElement(required = false, name = "exception")
    protected List<Class> exceptions = Collections.emptyList();

    public boolean isRegistered(Throwable exceptionToCheck) {

        Class exceptionCheckClass = exceptionToCheck.getClass();

        for (Class exception : exceptions) {
            if (exceptionCheckClass.isAssignableFrom(exception))
                return true;
        }
        return false;
    }

}
