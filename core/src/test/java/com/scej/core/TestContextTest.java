package com.scej.core;

import com.scej.core.config.Specification;
import com.scej.core.config.Test;
import org.concordion.api.Resource;
import org.junit.Assert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Fedorovaleks
 * Date: 15.03.14
 */
public class TestContextTest {

    @org.junit.Test
    public void doubleInitializationTest() {

        Test fakeTest = mock(Test.class);
        when(fakeTest.getSpecification()).thenReturn(new Specification());

        TestContext.createTestContext(fakeTest);
        try {
            TestContext.createTestContext(fakeTest);
            Assert.fail("Attempt to initialize context when existing one");
        } catch (RuntimeException ex) {

        }
        Assert.assertNotNull(TestContext.getInstance());
        TestContext.getInstance().destroyCurrentSpecificationContext();
        Assert.assertNull(TestContext.getInstance());
    }

    @org.junit.Test
    public void initializationTest() {
        Test fakeTest = mock(Test.class);
        when(fakeTest.getSpecification()).thenReturn(new Specification());

        TestContext.createTestContext(fakeTest);
        TestContext.getInstance().destroyCurrentSpecificationContext();
        TestContext.createTestContext(fakeTest);

        Assert.assertNotNull(TestContext.getInstance());
        TestContext.getInstance().destroyCurrentSpecificationContext();
        Assert.assertNull(TestContext.getInstance());

    }

    @org.junit.Test
    public void positiveFlow() {
        Test fakeTest = mock(Test.class);
        Specification rootSpecification = new Specification();
        Specification childSpecification = new Specification();

        when(fakeTest.getSpecification()).thenReturn(rootSpecification);

        TestContext.createTestContext(fakeTest);
        Assert.assertNotNull(TestContext.getInstance());
        Assert.assertNotNull(TestContext.getInstance().getCurrentSpecificationContext());
        Assert.assertEquals(rootSpecification, TestContext.getInstance().getCurrentSpecificationContext().getSpecification());

        TestContext.getInstance().createNewSpecificationContext(new Resource("/some/path"), childSpecification);
        Assert.assertEquals(childSpecification, TestContext.getInstance().getCurrentSpecificationContext().getSpecification());


        TestContext.getInstance().destroyCurrentSpecificationContext();

        Assert.assertNotNull(TestContext.getInstance());
        TestContext.getInstance().destroyCurrentSpecificationContext();
        Assert.assertNull(TestContext.getInstance());

    }

    @org.junit.Test
    public void undefinedSpecification() {
        Test fakeTest = mock(Test.class);
        Specification rootSpecification = new Specification();

        when(fakeTest.getSpecification()).thenReturn(rootSpecification);

        TestContext.createTestContext(fakeTest);
        Assert.assertNotNull(TestContext.getInstance());
        Assert.assertNotNull(TestContext.getInstance().getCurrentSpecificationContext());
        Assert.assertEquals(rootSpecification, TestContext.getInstance().getCurrentSpecificationContext().getSpecification());

        try {
            TestContext.getInstance().createNewSpecificationContext(new Resource("/some/path"), null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        Assert.assertNotNull(TestContext.getInstance());
        TestContext.getInstance().destroyCurrentSpecificationContext();
        Assert.assertNull(TestContext.getInstance());

    }

}
