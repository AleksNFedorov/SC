package com.scejtesting.core.config;

import com.scejtesting.core.CoreTestFixture;
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
    protected Class clazz = CoreTestFixture.class;

    @XmlAttribute(name = "substitutionDictionary", required = false)
    private String substitutionDictionary;

    @XmlAttribute(name = "name", required = true)
    private String name;

    public Test() {
    }

    void init() {
        Check.notNull(getDefaultTestClass(), "Default test class have not been resolved correctly");
        Check.notNull(getSpecification(), "Nothing to test, please add specification");
        Check.notNull(getName(), "Test must have a name");
        getSpecification().init();
    }

    public String getName() {
        return name;
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

    @Override
    public String toString() {
        return "Test{" +
                "specification=" + specification +
                ", clazz=" + clazz +
                ", substitutionDictionary='" + substitutionDictionary + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
