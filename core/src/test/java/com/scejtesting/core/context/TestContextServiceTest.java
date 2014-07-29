package com.scejtesting.core.context;


import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ofedorov on 7/9/14.
 */
public class TestContextServiceTest {


    private Test testMock;
    private TestContextService service;
    private TestContext initialContext;

    @Before
    public void initTest() {
        testMock = makeMockTest();
        service = new TestContextService();
        initialContext = service.createNewTestContext(testMock);
    }

    @After
    public void finishTest() {
        new TestContextService().revertContextSwitch();
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testDropContextException_nullContext() {
        service.dropContext(null);
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testDropContextException_destroyedContext() {
        initialContext.destroyTestContext();
        service.dropContext(initialContext);
    }

    @org.junit.Test
    public void testDropContext_validSingleContext() {
        service.dropContext(initialContext);

        Assert.assertTrue(TestContext.isDestroyedContext(initialContext));

        try {
            service.getCurrentTestContext();
            Assert.fail("No context exception expected");
        } catch (RuntimeException ex) {
        }

    }

    @org.junit.Test
    public void testDropContext_currentContext() {

        TestContext clonedContext = service.cloneContext(initialContext);

        Integer contextId = clonedContext.getContextId();

        service.switchContext(clonedContext);

        service.dropContext(clonedContext);

        try {
            new TestContextService().getCurrentTestContext();
            Assert.fail("No context exception expected");
        } catch (RuntimeException ex) {

        }
        Assert.assertEquals(TestContext.DESTROYED_CONTEXT, clonedContext.getContextId());
        Assert.assertNull(service.getTestContext(contextId));
        Assert.assertSame(initialContext, service.getTestContext(initialContext.getContextId()));

        Assert.assertEquals(1, TestContextService.contexts.size());
    }

    @org.junit.Test
    public void testDropContext_nonCurrentContext() {

        TestContext clonedContext = service.cloneContext(initialContext);
        Integer initialContextId = initialContext.getContextId();

        service.switchContext(clonedContext);

        service.dropContext(initialContext);

        Assert.assertEquals(TestContext.DESTROYED_CONTEXT, initialContext.getContextId());
        Assert.assertNull(service.getTestContext(initialContextId));
        Assert.assertSame(clonedContext, service.getTestContext(clonedContext.getContextId()));
        Assert.assertSame(clonedContext, service.getCurrentTestContext());

        Assert.assertEquals(1, TestContextService.contexts.size());
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testConeContextException_nullContext() {
        service.cloneContext(null);
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testCloneContextException_destroyedContext() {
        initialContext.destroyTestContext();
        service.cloneContext(initialContext);
    }

    @org.junit.Test
    public void testCloneContext_validContext() {
        TestContext clonedContext = service.cloneContext(initialContext);

        Assert.assertSame(initialContext, service.getCurrentTestContext());
        Assert.assertSame(clonedContext, service.getTestContext(clonedContext.getContextId()));
        Assert.assertEquals(2, TestContextService.contexts.size());
        Assert.assertNotSame(initialContext, clonedContext);
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testGetCurrentContextException_noContextCreated() {
        service.dropContext(initialContext);
        Assert.assertTrue(TestContextService.contexts.isEmpty());
        new TestContextService().getCurrentTestContext();
    }

    @org.junit.Test
    public void testGetCurrentContext_multipleContexts() {

        Assert.assertSame(initialContext, service.getCurrentTestContext());

        TestContext clonedContext = service.cloneContext(initialContext);

        Assert.assertSame(initialContext, service.getCurrentTestContext());
        Assert.assertSame(clonedContext, service.getTestContext(clonedContext.getContextId()));

        service.switchContext(clonedContext);

        Assert.assertSame(clonedContext, service.getCurrentTestContext());
        Assert.assertSame(initialContext, service.getTestContext(initialContext.getContextId()));

    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testGetContextException_nullId() {
        service.getTestContext(null);
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testGetContextException_destroyedContextId() {
        service.getTestContext(TestContext.DESTROYED_CONTEXT);
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testGetContextException_negativeId() {
        service.getTestContext(-2);
    }

    @org.junit.Test
    public void testGetTestContext_unknownContext() {
        Assert.assertNull(service.getTestContext(Integer.MAX_VALUE));
    }

    @org.junit.Test
    public void testGetTestContext_knownContext() {
        Assert.assertSame(initialContext, service.getTestContext(initialContext.getContextId()));
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testCreateNewTestContext_nullTest() {
        service.createNewTestContext(null);
    }


    @org.junit.Test
    public void testCreateNewContext_validTest() {

        service.cloneContext(initialContext);

        Assert.assertEquals(2, TestContextService.contexts.size());

        TestContext context = service.createNewTestContext(testMock);

        Assert.assertSame(context, service.getTestContext(context.getContextId()));
        Assert.assertSame(context, service.getCurrentTestContext());
        Assert.assertEquals(1, TestContextService.contexts.size());
    }

    @org.junit.Test
    public void testCorrectRevert_multipleAttempts() {
        TestContext clonedContext = service.cloneContext(initialContext);
        service.switchContext(clonedContext);
        service.revertContextSwitch();
        service.revertContextSwitch();

        Assert.assertSame(initialContext, service.getCurrentTestContext());
        Assert.assertEquals(2, TestContextService.contexts.size());

    }

    @org.junit.Test
    public void testSwitchContext_validTestContext() throws Exception {

        final TestContextService service = new TestContextService();
        TestContext testContext1 = service.createNewTestContext(makeMockTest());
        TestContext testContext2 = service.cloneContext(testContext1);
        service.switchContext(testContext2);

        Assert.assertEquals(testContext2, new TestContextService().getCurrentTestContext());

        new Thread() {
            @Override
            public void run() {
                try {
                    service.revertContextSwitch();
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        Assert.assertEquals(testContext1, new TestContextService().getCurrentTestContext());
    }


    @org.junit.Test(expected = RuntimeException.class)
    public void testSwitchContextException_nullContext() {
        new TestContextService().switchContext(null);
    }

    private Test makeMockTest() {
        Test testMock = mock(Test.class);
        when(testMock.getSpecification()).thenReturn(new Specification("/mock.html"));
        return testMock;
    }


}
