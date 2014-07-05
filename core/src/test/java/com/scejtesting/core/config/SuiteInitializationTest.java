package com.scejtesting.core.config;

import com.scejtesting.core.CoreTestFixture;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class SuiteInitializationTest {

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

    @org.junit.Test(expected = RuntimeException.class)
    public void incorrectDefaultTestClassTest() throws IOException {

        testValidationException("com/scej/core/config/incorrectDefaultTestClass.xml", "Unknown default test class in config", RuntimeException.class);
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void incorrectRootSpecificationLocationTest() throws IOException {

        testValidationException("com/scej/core/config/incorrectRootSpecificationLocation.xml", "Incorrect root specification location in config", RuntimeException.class);
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void incorrectSpecificationTestTest() throws IOException {

        testValidationException("com/scej/core/config/incorrectSpecificationTestClass.xml", "Incorrect root specification test class in config", RuntimeException.class);
    }

    private void testValidationException(String suiteFilePath, String assertionMessage, Class expectedExceptionClass) {

        String filePath = Thread.currentThread().
                getContextClassLoader().getResource(suiteFilePath).getFile();

        Assert.assertTrue("File [" + suiteFilePath + "] does not exist [" + assertionMessage + "]", new File(filePath).exists());

        SuiteConfiguration.initConfiguration(filePath);
        Assert.fail("No suite validation error, exception expected [" + assertionMessage + "]");

    }

    @org.junit.Test
    public void testSuiteInitialization() {
        String file = Thread.currentThread().
                getContextClassLoader().getResource("com/scejtesting/core/config/correctConfig.xml").getFile();

        SuiteConfiguration.initConfiguration(file);

        Collection<Test> tests = SuiteConfiguration.getInstance().getSuite().getTests();
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
        Assert.assertEquals(CoreTestFixture.class, secondTest.getDefaultTestClass());
        Assert.assertNull(secondTest.getSpecification().getIncludes());
        Assert.assertNull(secondTest.getSpecification().getExcludes());

    }

}
