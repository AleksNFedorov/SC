package com.scejtesting.core.concordion.extension;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.Resource;
import org.junit.Assert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Fedorovaleks
 * Date: 16.03.14
 */
public class HierarchySpecificationLocatorTest extends TestContextService {

    @org.junit.Test
    public void rootSpecificationResourceResolvingTest() {


        String specificationRealPath = "/SpecificationToRealPath.html";

        Specification specification = mock(Specification.class);

        when(specification.isTopLevelSpecification()).thenReturn(Boolean.TRUE);
        when(specification.getRealPath()).thenReturn(specificationRealPath);


        Test test = mock(Test.class);

        when(test.getSpecification()).thenReturn(specification);

        createNewTestContext(test);


        HierarchySpecificationLocator locator = new HierarchySpecificationLocator();

        Resource resource = locator.locateSpecification(null);

        Assert.assertEquals(specificationRealPath, resource.getPath());

        destroyTestContext();

    }

    @org.junit.Test
    public void nonRootSpecificationResourceResolve() {

        String specificationRealPath = "SpecificationToRealPath.html";

        Specification specification = mock(Specification.class);

        when(specification.getRealPath()).thenReturn(specificationRealPath);

        Test test = mock(Test.class);

        when(test.getSpecification()).thenReturn(specification);

        createNewTestContext(test);

        Resource parentSpecificationResource = new Resource("/some/path/to/parentSpec.html");

        getCurrentTestContext().createNewSpecificationContext(parentSpecificationResource, specification);


        HierarchySpecificationLocator locator = new HierarchySpecificationLocator();

        Resource resource = locator.locateSpecification(null);

        Assert.assertEquals("/some/path/to/" + specificationRealPath, resource.getPath());

        getCurrentTestContext().destroyCurrentSpecificationContext();

        destroyTestContext();

    }
}
