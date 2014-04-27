package com.scejtesting.core.config;

import org.junit.Assert;
import org.junit.Test;

/**
 * User: Fedorovaleks
 * Date: 15.03.14
 */
public class SpecificationLocatorServiceTest {

    @Test(expected = RuntimeException.class)
    public void buildHREFForUnknownExtension() {
        Specification specification = new Specification("/rootSpecification.html");
        SpecificationLocatorService.getService().buildUniqueSpecificationHREF(specification, "childSpecification.txt");
    }

    @Test
    public void buildDefaultChildSpecification() {
        Specification specification = new Specification("/rootSpecification.html");
        Specification childSpecification = SpecificationLocatorService.getService().getChildSpecificationByRealLocation(specification, "childSpecification.html");
        Assert.assertNull(childSpecification.getTestClass());
    }

    @Test
    public void buildUniqueSpecificationHREF() {
        Specification specification = new Specification("/rootSpecification.html");
        String specificationHref = SpecificationLocatorService.getService().buildUniqueSpecificationHREF(specification, "childSpecification.html");
        Assert.assertTrue(SpecificationLocatorService.containsGeneratedSuffix(specificationHref));
    }

    @Test
    public void buildRealPathByUniqueHREF() {

        Specification specification = new Specification("/rootSpecification.html");
        String specificationHref = SpecificationLocatorService.getService().buildUniqueSpecificationHREF(specification, "childSpecification.html");

        String realPath = SpecificationLocatorService.getService().buildRealPathByUniqueHREF(specificationHref);

        Assert.assertFalse(SpecificationLocatorService.containsGeneratedSuffix(realPath));

    }

    @Test
    public void checkServicePathValidators() {

        Assert.assertTrue(SpecificationLocatorService.containsGeneratedSuffix(SpecificationTest.appendUniqueSuffix("someSpecification.html")));
        Assert.assertFalse(SpecificationLocatorService.containsGeneratedSuffix("someSpecification.html"));
        Assert.assertFalse(SpecificationLocatorService.containsGeneratedSuffix(""));

    }
}
