package com.scejtesting.core.context;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import org.concordion.api.Element;
import org.concordion.api.Resource;
import org.junit.Assert;
import org.junit.Before;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Fedorovaleks
 * Date: 15.03.14
 */
public class TestContextTest {

    private Test testMock;
    private TestContext contextToTest;

    @Before
    public void initTest() {
        Test fakeTest = mock(Test.class);
        when(fakeTest.getSpecification()).thenReturn(new Specification());

        contextToTest = new TestContext(fakeTest);
        testMock = fakeTest;
    }

    @org.junit.Test
    public void testContextCloned_validContext() {

        String elementHref = "someHref" + System.currentTimeMillis();
        Element elementToStore = new Element("a");
        elementToStore.addAttribute("href", elementHref);

        Object key = new Object();
        Object value = new Object();

        contextToTest.addAttribute(key, value);
        contextToTest.saveChildSpecificationElement(elementToStore);

        TestContext clonedContext = contextToTest.clone();

        Assert.assertTrue(clonedContext.getContextId() > 0);
        Assert.assertSame(value, clonedContext.getAttribute(key));
        Assert.assertNotEquals(contextToTest.getContextId(), clonedContext.getContextId());
        Assert.assertSame(contextToTest.getTest(), clonedContext.getTest());
        Assert.assertEquals(elementToStore, clonedContext.getChildSpecificationElement(elementHref));
        Assert.assertNotNull(contextToTest.getCurrentSpecificationContext().getResultRegistry());

        clonedContext.cleanAttribute(key);

        contextToTest.getCurrentSpecificationContext().onNewAsyncCall();
        contextToTest.getCurrentSpecificationContext().onNewAsyncCall();
        Assert.assertEquals(Integer.valueOf(2), contextToTest.getCurrentSpecificationContext().getRunningAsyncCallsAmount());
        contextToTest.getCurrentSpecificationContext().onAsyncCallFinished();
        Assert.assertEquals(Integer.valueOf(1), contextToTest.getCurrentSpecificationContext().getRunningAsyncCallsAmount());
        contextToTest.getCurrentSpecificationContext().onAsyncCallFinished();
        Assert.assertEquals(Integer.valueOf(0), contextToTest.getCurrentSpecificationContext().getRunningAsyncCallsAmount());

        Assert.assertSame(value, contextToTest.getAttribute(key));
        Assert.assertEquals(contextToTest.getSpecificationStack().size(), clonedContext.getSpecificationStack().size());


    }

    @org.junit.Test
    public void testAttributesManipulated_validContext() {

        Object key = new Object();
        Object value = new Object();
        Object value2 = new Object();

        contextToTest.addAttribute(key, value);
        contextToTest.addAttribute(key, value2);

        Object contextValue = contextToTest.getAttribute(key);

        Assert.assertEquals(value2, contextValue);

        contextToTest.cleanAttribute(key);

        Assert.assertNull(contextToTest.getAttribute(key));

    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testSpecificationContextDestroyException_rootTestContext() {
        contextToTest.destroyCurrentSpecificationContext();
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testRootContextDestroyException_specificationTestContext() {
        contextToTest.createNewSpecificationContext(new Resource("/path"), new Specification());
        contextToTest.destroyTestContext();
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testSaveChildElementException_nullElement() {
        contextToTest.saveChildSpecificationElement(null);
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testSaveChildElementException_emptyHrefAttribute() {
        contextToTest.saveChildSpecificationElement(new Element("a"));
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testGetChildElementException_nullHref() {
        contextToTest.getChildSpecificationElement(null);
    }


    @org.junit.Test(expected = RuntimeException.class)
    public void testCreationException_nullTest() {
        new TestContext(null);
    }

    @org.junit.Test
    public void testNullGetChildElement_unknownLink() {
        Element result = contextToTest.getChildSpecificationElement("someUnknownLink" + System.currentTimeMillis());
        Assert.assertNull(result);
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testContextDestroyedTestException_nullContext() {
        TestContext.isDestroyedContext(null);
    }

    @org.junit.Test
    public void testContextDestroyedCheck_validData() {
        Assert.assertFalse(TestContext.isDestroyedContext(contextToTest));
        contextToTest.destroyTestContext();
        Assert.assertTrue(TestContext.isDestroyedContext(contextToTest));
    }

    @org.junit.Test
    public void testContextCreated_validData() {
        Specification childSpecification = new Specification();

        Assert.assertTrue(contextToTest.getContextId() > 0);
        Assert.assertNotNull(contextToTest.getCurrentSpecificationContext());
        Assert.assertEquals(testMock.getSpecification(),
                contextToTest.getCurrentSpecificationContext().getSpecification());

        contextToTest.createNewSpecificationContext(new Resource("/some/path"), childSpecification);
        Assert.assertEquals(childSpecification,
                contextToTest.getCurrentSpecificationContext().getSpecification());

        contextToTest.destroyCurrentSpecificationContext();
        contextToTest.destroyTestContext();
        Assert.assertEquals(0, contextToTest.getSpecificationStack().size());
        Assert.assertEquals(TestContext.DESTROYED_CONTEXT, contextToTest.getContextId());

    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testNewSpecificationContextException_nullSpecification() {
        Assert.assertNotNull(contextToTest.getCurrentSpecificationContext());
        Assert.assertEquals(testMock.getSpecification(), contextToTest.getCurrentSpecificationContext().getSpecification());

        contextToTest.createNewSpecificationContext(new Resource("/some/path"), null);

    }

}
