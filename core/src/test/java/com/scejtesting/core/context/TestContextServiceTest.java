package com.scejtesting.core.context;


import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import org.junit.After;
import org.junit.Assert;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ofedorov on 7/9/14.
 */
public class TestContextServiceTest {

    @After
    public void finishTest() {
        new TestContextService().revertContextSwitch();
    }

    @org.junit.Test
    public void dropContextTest() {

        Test mockTest = makeMockTest();

        TestContextService service = new TestContextService();
        TestContext context = service.createNewTestContext(mockTest);
        TestContext clonedContext = service.cloneContext(context);

        service.switchContext(clonedContext);

        service.dropContext(clonedContext);

        try {
            new TestContextService().getCurrentTestContext();
            Assert.fail("No context exception expected");
        } catch (RuntimeException ex) {

        }
        Assert.assertNull(service.getTestContext(clonedContext.getContextId()));
        Assert.assertSame(context, service.getTestContext(context.getContextId()));

        Assert.assertEquals(1, TestContextService.contexts.size());
    }

    @org.junit.Test
    public void cloneContextTest() {
        Test mockTest = makeMockTest();

        TestContextService service = new TestContextService();
        TestContext context = service.createNewTestContext(mockTest);
        TestContext clonedContext = service.cloneContext(context);

        Assert.assertNotNull(clonedContext);
        Assert.assertEquals(2, TestContextService.contexts.size());
        Assert.assertNotSame(context, clonedContext);

    }

    @org.junit.Test
    public void getContextTest() {
        Test mockTest = makeMockTest();

        TestContextService service = new TestContextService();
        TestContext context = service.createNewTestContext(mockTest);
        TestContext clonedContext = service.cloneContext(context);

        service.switchContext(clonedContext);

        Assert.assertSame(clonedContext, service.getCurrentTestContext());
        Assert.assertSame(clonedContext, service.getTestContext(clonedContext.getContextId()));
        Assert.assertSame(context, service.getTestContext(context.getContextId()));

    }

    @org.junit.Test
    public void createNewContextTest() {
        Test mockTest = makeMockTest();

        TestContextService service = new TestContextService();

        TestContext context = service.createNewTestContext(mockTest);

        Assert.assertNotNull(context);
        Assert.assertSame(context, service.getTestContext(context.getContextId()));
        Assert.assertSame(context, service.getCurrentTestContext());
        Assert.assertSame(mockTest, context.getTest());
    }

    @org.junit.Test
    public void switchContextTest() throws Exception {

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
    public void switchContext_exceptionOnNullContext() {
        new TestContextService().switchContext(null);
    }

    private Test makeMockTest() {
        Test testMock = mock(Test.class);
        when(testMock.getSpecification()).thenReturn(new Specification("/mock.html"));
        return testMock;
    }


}
