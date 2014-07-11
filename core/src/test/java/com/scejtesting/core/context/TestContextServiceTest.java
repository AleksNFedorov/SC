package com.scejtesting.core.context;


import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import org.junit.Assert;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ofedorov on 7/9/14.
 */
public class TestContextServiceTest {

    @org.junit.Test
    public void dropContextTest() {

        Test mockTest = makeMockTest();

        TestContextService service = new TestContextService();
        TestContext context = service.createNewTestContext(mockTest);
        TestContext clonedContext = service.cloneContext(context.getContextId());

        service.setContextIdToUse(clonedContext.getContextId());

        service.dropContext(clonedContext.getContextId());

        Assert.assertNull(service.getCurrentTestContext());
        Assert.assertNull(service.getTestContext(clonedContext.getContextId()));
        Assert.assertSame(context, service.getTestContext(context.getContextId()));

        Assert.assertEquals(1, TestContextService.contexts.size());
    }

    @org.junit.Test
    public void cloneContextTest() {
        Test mockTest = makeMockTest();

        TestContextService service = new TestContextService();
        TestContext context = service.createNewTestContext(mockTest);
        TestContext clonedContext = service.cloneContext(context.getContextId());

        Assert.assertNotNull(clonedContext);
        Assert.assertEquals(2, TestContextService.contexts.size());
        Assert.assertNotSame(context, clonedContext);

    }

    @org.junit.Test
    public void getContextTest() {
        Test mockTest = makeMockTest();

        TestContextService service = new TestContextService();
        TestContext context = service.createNewTestContext(mockTest);
        TestContext clonedContext = service.cloneContext(context.getContextId());

        service.setContextIdToUse(clonedContext.getContextId());

        Assert.assertSame(clonedContext, service.getCurrentTestContext());
        Assert.assertSame(clonedContext, service.getTestContext(clonedContext.getContextId()));
        Assert.assertSame(context, service.getTestContext(context.getContextId()));

    }

    @org.junit.Test
    public void createNewContextTest() {
        Test mockTest = makeMockTest();

        TestContextService service = new TestContextService();

        TestContext context = service.createNewTestContext(mockTest);

        Assert.assertFalse(new TestContextService().isExtensionInitialized());
        Assert.assertNotNull(context);
        Assert.assertSame(context, service.getTestContext(context.getContextId()));
        Assert.assertSame(context, service.getCurrentTestContext());
        Assert.assertSame(mockTest, context.getTest());
    }

    @org.junit.Test
    public void initializationWaitTest() {

        final TestContextService service = new TestContextService();
        service.setContextIdToUse(1);

        new Thread() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    service.setExtensionInitialized();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        service.waitForInitialization();

        Assert.assertTrue(new TestContextService().isExtensionInitialized());
    }

    private Test makeMockTest() {
        Test testMock = mock(Test.class);
        when(testMock.getSpecification()).thenReturn(new Specification("/mock.html"));
        return testMock;
    }


}
