package com.scej.core.concordion.extension;

import com.scej.core.TestContext;
import com.scej.core.config.Specification;
import com.scej.core.config.Test;
import org.concordion.api.Resource;
import org.junit.Assert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Fedorovaleks
 * Date: 16.03.14
 */
public class HierarchySpecificationLocatorTest {

    @org.junit.Test
    public void rootSpecificationResourceResolvingTest() {


        String specificationRealPath = "/SpecificationToRealPath.html";

        Specification specification = mock(Specification.class);

        when(specification.isTopLevelSpecification()).thenReturn(Boolean.TRUE);
        when(specification.getRealPath()).thenReturn(specificationRealPath);


        Test test = mock(Test.class);

        when(test.getSpecification()).thenReturn(specification);

        TestContext.createTestContext(test);


        HierarchySpecificationLocator locator = new HierarchySpecificationLocator();

        Resource resource = locator.locateSpecification(null);

        Assert.assertEquals(specificationRealPath, resource.getPath());

        TestContext.getInstance().destroyCurrentSpecificationContext();

    }

    @org.junit.Test
    public void nonRootSpecificationResourceResolve() {

        String specificationRealPath = "SpecificationToRealPath.html";

        Specification specification = mock(Specification.class);

        when(specification.getRealPath()).thenReturn(specificationRealPath);

        Test test = mock(Test.class);

        when(test.getSpecification()).thenReturn(specification);

        TestContext.createTestContext(test);

        Resource parentSpecificationResource = new Resource("/some/path/to/parentSpec.html");

        TestContext.getInstance().createNewSpecificationContext(parentSpecificationResource, specification);


        HierarchySpecificationLocator locator = new HierarchySpecificationLocator();

        Resource resource = locator.locateSpecification(null);

        Assert.assertEquals("/some/path/to/" + specificationRealPath, resource.getPath());

        TestContext.getInstance().destroyCurrentSpecificationContext();

        TestContext.getInstance().destroyCurrentSpecificationContext();

    }
}
