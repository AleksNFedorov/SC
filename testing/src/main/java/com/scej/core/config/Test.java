
package com.scejtesting.core.config;

import com.scejtesting.core.WebCoreTest;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for Test complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="Test">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="specification" type="{}Specification"/>
 *       &lt;/sequence>
 *       &lt;attribute name="class" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Test", propOrder = {
        "specification"
})
public class Test {

    @XmlElement(required = true)
    protected Specification specification;
    @XmlAttribute(name = "class", required = false)
    protected Class<? extends WebCoreTest> clazz = WebCoreTest.class;

    public Test() {
    }

    public Test(Specification specification, Class<? extends WebCoreTest> clazz) {
        this.specification = specification;
        this.clazz = clazz;
    }

    /**
     * Gets the value of the specification property.
     *
     * @return possible object is
     *         {@link Specification }
     */
    public Specification getSpecification() {
        return specification;
    }

    /**
     * Sets the value of the specification property.
     *
     * @param value allowed object is
     *              {@link Specification }
     */
    public void setSpecification(Specification value) {
        this.specification = value;
    }

    /**
     * Gets the value of the clazz property.
     *
     * @return possible object is
     *         {@link String }
     */
    public Class<? extends WebCoreTest> getClazz() {
        return clazz;
    }

    /**
     * Sets the value of the clazz property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setClazz(String value) throws ClassNotFoundException {
        this.clazz = (Class<? extends WebCoreTest>) Class.forName(value);
    }

}
