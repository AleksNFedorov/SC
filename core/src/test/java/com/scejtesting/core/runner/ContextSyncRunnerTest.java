package com.scejtesting.core.runner;


import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.junit.Before;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

/**
 * Created by aleks on 7/12/14.
 */
public class ContextSyncRunnerTest {

    private TestContextService serviceMock;

    @Before
    public void initTest() {
        Specification rootSpec = new Specification("/root.html");
        Test test = mock(Test.class);
        when(test.getSpecification()).thenReturn(rootSpec);
        when(test.getName()).thenReturn("testMock");
        serviceMock = spy(new TestContextService());
        serviceMock.createNewTestContext(test);
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void noContextTest() {
        new ContextSyncRunner() {
            @Override
            public Object runCallBack(TestContext context) {
                return null;
            }
        }.runSync(null);
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testContextDestroyedTest() {
        new ContextSyncRunner() {
            @Override
            public Object runCallBack(TestContext context) {
                return null;
            }
        }.runSync(TestContext.DESTROYED_CONTEXT);
    }

    @org.junit.Test
    public void positiveFlowTest() {

        ContextSyncRunner runner = spy(new ContextSyncRunner() {
            @Override
            public Object runCallBack(TestContext context) {
                new TestContextService().setTestContextInitialized();
                return new Object();
            }

            @Override
            TestContextService getTestContextService() {
                return serviceMock;
            }
        });

        InOrder testServiceVerifier = inOrder(serviceMock);
        InOrder runnerVerifier = inOrder(runner);

        Integer contextIndex = serviceMock.getCurrentTestContext().getContextId();

        runner.runSync(contextIndex);

        testServiceVerifier.verify(serviceMock, calls(1)).getTestContext(contextIndex);
        testServiceVerifier.verify(serviceMock, calls(1)).lock();
        testServiceVerifier.verify(serviceMock, calls(1)).setContextIdToUse(contextIndex);
        testServiceVerifier.verify(serviceMock, calls(1)).waitForInitialization();
        testServiceVerifier.verify(serviceMock, calls(1)).unLock();
        runnerVerifier.verify(runner, calls(1)).runCallBack(serviceMock.getCurrentTestContext());
    }
}
