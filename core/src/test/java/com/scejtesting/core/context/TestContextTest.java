package com.scejtesting.core.context;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
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
    public void cloneTest() {
        Test fakeTest = mock(Test.class);
        when(fakeTest.getSpecification()).thenReturn(new Specification());

        TestContext testContext = new TestContext(fakeTest);

        Object key = new Object();
        Object value = new Object();

        testContext.addAttribute(key, value);

        TestContext clonedContext = testContext.clone();

        Assert.assertTrue(clonedContext.getContextId() > 0);
        Assert.assertSame(value, clonedContext.getAttribute(key));
        Assert.assertNotEquals(testContext.getContextId(), clonedContext.getContextId());
        Assert.assertSame(testContext.getTest(), clonedContext.getTest());

        clonedContext.cleanAttribute(key);

        Assert.assertSame(value, testContext.getAttribute(key));
        Assert.assertEquals(testContext.getSpecificationStack().size(), clonedContext.getSpecificationStack().size());

    }

    @org.junit.Test
    public void attributesTest() {
        Test fakeTest = mock(Test.class);
        when(fakeTest.getSpecification()).thenReturn(new Specification());

        TestContext testContext = new TestContext(fakeTest);

        Object key = new Object();
        Object value = new Object();
        Object value2 = new Object();

        testContext.addAttribute(key, value);
        testContext.addAttribute(key, value2);

        Object contextValue = testContext.getAttribute(key);

        Assert.assertEquals(value2, contextValue);

        testContext.cleanAttribute(key);

        Assert.assertNull(testContext.getAttribute(key));

    }

    /*
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
            destroyTestContextService();
            Assert.assertNull(getCurrentTestContext());
        }

        @org.junit.Test
        public void initializationTest() {
            Test fakeTest = mock(Test.class);
            when(fakeTest.getSpecification()).thenReturn(new Specification());

            createNewTestContext(fakeTest);

            destroyTestContextService();
            createNewTestContext(fakeTest);

            Assert.assertNotNull(getCurrentTestContext());
            destroyTestContextService();
            Assert.assertNull(getCurrentTestContext());

        }

    */
    @org.junit.Test
    public void incorrectDestroy() {

        Test fakeTest = mock(Test.class);
        when(fakeTest.getSpecification()).thenReturn(new Specification());

        TestContext context = new TestContext(fakeTest);

        try {
            context.destroyCurrentSpecificationContext();
            Assert.fail("incorrect destroy exception expected");
        } catch (RuntimeException ex) {

        }

        context.createNewSpecificationContext(new Resource("/path"), new Specification());

        try {
            context.destroyTestContext();
            Assert.fail("Incorrect destroy exception expected");
        } catch (RuntimeException ex) {

        }

    }

    @org.junit.Test
    public void positiveFlow() {
        Test fakeTest = mock(Test.class);
        Specification rootSpecification = new Specification();
        Specification childSpecification = new Specification();

        when(fakeTest.getSpecification()).thenReturn(rootSpecification);

        TestContext context = new TestContext(fakeTest);

        Assert.assertTrue(context.getContextId() > 0);
        Assert.assertNotNull(context.getCurrentSpecificationContext());
        Assert.assertEquals(rootSpecification, context.getCurrentSpecificationContext().getSpecification());

        context.createNewSpecificationContext(new Resource("/some/path"), childSpecification);
        Assert.assertEquals(childSpecification, context.getCurrentSpecificationContext().getSpecification());

        context.destroyCurrentSpecificationContext();
        context.destroyTestContext();
        Assert.assertEquals(0, context.getSpecificationStack().size());
        Assert.assertEquals(TestContext.DESTROYED_CONTEXT, context.getContextId());

    }

    @org.junit.Test
    public void undefinedSpecification() {
        Test fakeTest = mock(Test.class);
        Specification rootSpecification = new Specification();

        when(fakeTest.getSpecification()).thenReturn(rootSpecification);

        TestContext context = new TestContext(fakeTest);

        Assert.assertNotNull(context.getCurrentSpecificationContext());
        Assert.assertEquals(rootSpecification, context.getCurrentSpecificationContext().getSpecification());

        try {
            context.createNewSpecificationContext(new Resource("/some/path"), null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

    }

}
