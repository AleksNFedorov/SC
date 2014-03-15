package com.scej.core.config;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 * Date: 01.02.14
 * Time: 11:59
 * To change this template use File | Settings | File Templates.
 */
public class SpecificationTest {

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
}
