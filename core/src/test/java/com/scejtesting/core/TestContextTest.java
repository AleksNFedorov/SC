package com.scejtesting.core;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.Resource;
import org.junit.Assert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Fedorovaleks
 * Date: 15.03.14
 */
public class TestContextTest extends TestContextService {

    @org.junit.Test
    public void doubleInitializationTest() {

        Test fakeTest = mock(Test.class);
        when(fakeTest.getSpecification()).thenReturn(new Specification());

        createNewTestContext(fakeTest);
        try {
            createNewTestContext(fakeTest);
            Assert.fail("Attempt to initialize context when existing one");
        } catch (RuntimeException ex) {

        }
        Assert.assertNotNull(getCurrentTestContext());
        getCurrentTestContext().destroyCurrentSpecificationContext();
        Assert.assertNull(getCurrentTestContext());
    }

    @org.junit.Test
    public void initializationTest() {
        Test fakeTest = mock(Test.class);
        when(fakeTest.getSpecification()).thenReturn(new Specification());

        createNewTestContext(fakeTest);
        getCurrentTestContext().destroyCurrentSpecificationContext();
        createNewTestContext(fakeTest);

        Assert.assertNotNull(getCurrentTestContext());
        getCurrentTestContext().destroyCurrentSpecificationContext();
        Assert.assertNull(getCurrentTestContext());

    }

    @org.junit.Test
    public void positiveFlow() {
        Test fakeTest = mock(Test.class);
        Specification rootSpecification = new Specification();
        Specification childSpecification = new Specification();

        when(fakeTest.getSpecification()).thenReturn(rootSpecification);

        createNewTestContext(fakeTest);
        Assert.assertNotNull(getCurrentTestContext());
        Assert.assertNotNull(getCurrentTestContext().getCurrentSpecificationContext());
        Assert.assertEquals(rootSpecification, getCurrentTestContext().getCurrentSpecificationContext().getSpecification());

        getCurrentTestContext().createNewSpecificationContext(new Resource("/some/path"), childSpecification);
        Assert.assertEquals(childSpecification, getCurrentTestContext().getCurrentSpecificationContext().getSpecification());


        getCurrentTestContext().destroyCurrentSpecificationContext();

        Assert.assertNotNull(getCurrentTestContext());
        getCurrentTestContext().destroyCurrentSpecificationContext();
        Assert.assertNull(getCurrentTestContext());

    }

    @org.junit.Test
    public void undefinedSpecification() {
        Test fakeTest = mock(Test.class);
        Specification rootSpecification = new Specification();

        when(fakeTest.getSpecification()).thenReturn(rootSpecification);

        createNewTestContext(fakeTest);
        Assert.assertNotNull(getCurrentTestContext());
        Assert.assertNotNull(getCurrentTestContext().getCurrentSpecificationContext());
        Assert.assertEquals(rootSpecification, getCurrentTestContext().getCurrentSpecificationContext().getSpecification());

        try {
            getCurrentTestContext().createNewSpecificationContext(new Resource("/some/path"), null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        Assert.assertNotNull(getCurrentTestContext());
        getCurrentTestContext().destroyCurrentSpecificationContext();
        Assert.assertNull(getCurrentTestContext());

    }

}
