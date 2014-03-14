
package com.scej.core.config;

import com.scej.core.CoreTest;
import org.concordion.internal.util.Check;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for Specification complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="Specification">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="includes" type="{}Includes" minOccurs="0"/>
 *         &lt;element name="excludes" type="{}Excludes" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="location" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Specification", propOrder = {
        "includes",
        "excludes"
})
public class Specification {


    private static Integer suffixSequence = new Integer(0);
    public static final String MIDDLE_SUFFIX = "__aurora_";


    @XmlElement
    protected Includes includes;

    @XmlElement
    protected Excludes excludes;

    @XmlAttribute(required = true)
    protected String location;

    @XmlTransient
    private String tmpFileSuffix;

    @XmlTransient
    private String realPath;

    @XmlTransient
    private Boolean isTopLevelSpecification = Boolean.FALSE;

    @XmlAttribute(name = "testClass", required = false)
    protected Class<? extends CoreTest> clazz;


    public Specification() {
        tmpFileSuffix = generateSuffix();
    }

    public Specification(String location) {
        this();
        setLocation(location);
    }

    private String generateSuffix() {
        suffixSequence++;
        return MIDDLE_SUFFIX + suffixSequence;
    }

    void setRealPath(String realPath) {
        this.realPath = realPath;
    }


    Includes getIncludes() {
        return includes;
    }

    Excludes getExcludes() {
        return excludes;
    }

    public String getLocation() {
        return location;
    }

    void setLocation(String value) {
        this.location = value;
    }

    public String getTmpFileSuffix() {
        return tmpFileSuffix;
    }

    public String getRealPath() {
        return realPath;
    }

    public Boolean isTopLevelSpecification() {
        return isTopLevelSpecification;
    }

    void setTopLevelSpecification() {
        Check.isFalse(this.isTopLevelSpecification, "Top level sign already defined");
        isTopLevelSpecification = Boolean.TRUE;
    }

    public Class<? extends CoreTest> getTestClass() {
        return clazz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Specification that = (Specification) o;

        if (!location.equals(that.location)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }

    @Override
    public String toString() {
        return "Specification{" +
                "location='" + location + '\'' +
                ", tmpFileSuffix='" + tmpFileSuffix + '\'' +
                ", realPath='" + realPath + '\'' +
                '}';
    }
}
