
package com.scej.core.config;

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

    public Specification() {
        tmpFileSuffix = generateSuffix();
    }

    public Specification(String location) {
        this();
        setLocation(location);
    }

    private String generateSuffix() {
        suffixSequence++;
        return MIDDLE_SUFFIX+suffixSequence;
    }

    void setRealPath(String realPath) {
        this.realPath = realPath;
    }


    /**
     * Gets the value of the includes property.
     *
     * @return possible object is
     *         {@link Includes }
     */
    Includes getIncludes() {
        return includes;
    }

    /**
     * Gets the value of the excludes property.
     *
     * @return possible object is
     *         {@link Excludes }
     */
    Excludes getExcludes() {
        return excludes;
    }


    /**
     * Gets the value of the location property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    void setLocation(String value) {
        this.location = value;
    }

    public String getTmpFileSuffix() {
        return tmpFileSuffix;
    }

    public String getRealPath() {
        return realPath;
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
