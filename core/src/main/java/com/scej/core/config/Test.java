
package com.scej.core.config;

import com.scej.core.CoreTest;
import org.concordion.internal.util.Check;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Test", propOrder = {
    "specification"
})
public class Test {

    @XmlElement(required = true)
    protected Specification specification;
    @XmlAttribute(name = "defaultTestClass", required = false)
    protected Class<? extends CoreTest> clazz = CoreTest.class;

    public Test() {
    }

    public Test(Specification specification, Class<? extends CoreTest> clazz) {
        this.specification = specification;
        this.clazz = clazz;
    }

    void init() {
        Check.notNull(getDefaultTestClass(), "Default test class have not been resolved correctly");
        Check.notNull(getSpecification(), "Nothing to test, please add specification");
    }


    public Specification getSpecification() {
        return specification;
    }

    public Class<? extends CoreTest> getDefaultTestClass() {
        return clazz;
    }

}
