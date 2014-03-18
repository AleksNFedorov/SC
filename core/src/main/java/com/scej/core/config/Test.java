package com.scej.core.config;

import com.scej.core.CoreTest;
import org.concordion.internal.util.Check;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Test", propOrder = {
        "specification"
})
public class Test extends ExceptionsHolder {

    @XmlElement(required = true)
    protected Specification specification;

    @XmlAttribute(name = "defaultTestClass", required = false)
    protected Class clazz = CoreTest.class;

    @XmlAttribute(name = "substitutionDictionary", required = false)
    private String substitutionDictionary;

    public Test() {
    }

    void init() {
        Check.notNull(getDefaultTestClass(), "Default test class have not been resolved correctly");
        Check.notNull(getSpecification(), "Nothing to test, please add specification");
        getSpecification().init();
    }

    public Specification getSpecification() {
        return specification;
    }

    public String getSubstitutionDictionary() {
        return substitutionDictionary;
    }

    public Class getDefaultTestClass() {
        return clazz;
    }
}
