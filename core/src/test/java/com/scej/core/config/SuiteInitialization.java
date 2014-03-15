package com.scej.core.config;

import com.scej.core.CoreTest;
import org.junit.Assert;

import javax.xml.bind.UnmarshalException;
import java.io.IOException;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class SuiteInitialization {

    @org.junit.Test
    public void noSpecificationFileTest() {
        try {
            SuiteConfiguration.initConfiguration(null);
            Assert.fail("Null link to specification file exception expected");
        } catch (RuntimeException ex) {
        }

        try {
            SuiteConfiguration.initConfiguration("SomeUnknownFile");
            Assert.fail("No specification file exception expected");
        } catch (RuntimeException ex) {
        }
    }

    @org.junit.Test
    public void specificationLoadingValidationTest() throws IOException {

        testValidationException("com/scej/core/config/incorrectDefaultTestClass.xml", "Unknown default test class in config");
        testValidationException("com/scej/core/config/incorrectRootSpecificationLocation.xml", "Incorrect root specification location in config");
        testValidationException("com/scej/core/config/incorrectSpecificationTestClass.xml", "Incorrect root specification test class in config");
    }

    private void testValidationException(String suiteFilePath, String assertionMessage) {
        try {
            SuiteConfiguration.initConfiguration(suiteFilePath);
            Assert.fail("No suite validation error, exception expected [" + assertionMessage + "]");
        } catch (RuntimeException ex) {
            Assert.assertTrue("UnmarshalException as root case expected [" + assertionMessage + "]",
                    ex.getCause() instanceof UnmarshalException);
        }

    }

    @org.junit.Test
    public void testSuiteInitialization() {
        String file = Thread.currentThread().
                getContextClassLoader().getResource("com/scej/core/config/correctConfig.xml").getFile();

        SuiteConfiguration.initConfiguration(file);

        Collection<Test> tests = SuiteConfiguration.getInstance().getSuiteTests();
        Assert.assertEquals(2, tests.size());

        Test[] testsArray = tests.toArray(new Test[2]);

        Test firstTest = testsArray[0];
        Test secondTest = testsArray[1];

        Assert.assertNotNull(firstTest.getSpecification());
        Assert.assertTrue(firstTest.getSpecification().isTopLevelSpecification());
        Assert.assertEquals(Runtime.class, firstTest.getDefaultTestClass());
        Assert.assertEquals(String.class, firstTest.getSpecification().getTestClass());

        Assert.assertFalse(firstTest.getSpecification().getIncludes().getSpecifications().get(0).isTopLevelSpecification());
        Assert.assertFalse(firstTest.getSpecification().getExcludes()
                .getSpecifications().get(0).isTopLevelSpecification());

        Assert.assertNotNull(firstTest.getSpecification().getExcludes());
        Assert.assertNotNull(firstTest.getSpecification().getIncludes());


        Assert.assertNotNull(secondTest.getSpecification());
        Assert.assertEquals(CoreTest.class, secondTest.getDefaultTestClass());
        Assert.assertNull(secondTest.getSpecification().getIncludes());
        Assert.assertNull(secondTest.getSpecification().getExcludes());


    }


}
