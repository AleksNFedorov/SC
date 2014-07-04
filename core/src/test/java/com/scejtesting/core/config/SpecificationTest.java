package com.scejtesting.core.config;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class SpecificationTest {

    public static String appendUniqueSuffix(String origString) {
        return origString.replace(".html", Specification.MIDDLE_SUFFIX + "1.html");
    }

    @Test
    public void testSuffixOperations() {
        Specification specification = new Specification();
        Specification specificationSecond = new Specification();
        String fakePath = "blaaa" + specification.getTmpFileSuffix() + ".html";

        Assert.assertNotEquals(specification.getTmpFileSuffix(), specificationSecond.getTmpFileSuffix());

        Assert.assertTrue(SpecificationLocatorService.containsGeneratedSuffix(fakePath));
        Assert.assertFalse(SpecificationLocatorService.containsGeneratedSuffix("noSuffixString"));

        String cleanedSpecPath = SpecificationLocatorService.cleanSuffix(fakePath);
        System.out.println("Full fake path is " + fakePath);
        System.out.println("Cleanded spec path is " + cleanedSpecPath);
        Assert.assertEquals("blaaa.html", cleanedSpecPath);

    }

    @Test
    public void specificationLocationValidationTest() {
        Specification specification = new Specification();

        specification.setLocation("someLocation.html");

        try {
            specification.setLocation("someLocation");
            Assert.fail("Wrong location exception expected");
        } catch (RuntimeException ex) {

        }
    }
}
