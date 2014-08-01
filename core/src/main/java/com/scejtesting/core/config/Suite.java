
package com.scejtesting.core.config;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * <p>Java class for Suite complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="Suite">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tests" type="{}Test" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="prefix" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Suite extends ExceptionsHolder {

    @XmlElement(required = true, name = "test")
    protected List<Test> tests;

    @XmlTransient
    private Executor asyncSpecExecutor = Executors.newCachedThreadPool();

    public List<Test> getTests() {
        if (tests == null) {
            tests = new ArrayList<Test>();
        }
        return this.tests;
    }

    public void submitAsyncTask(Runnable taskToRun) {
        asyncSpecExecutor.execute(taskToRun);
    }
}
