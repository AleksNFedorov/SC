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
        TestContext context = spy(serviceMock.getCurrentTestContext());
        doReturn(TestContext.DESTROYED_CONTEXT).when(context).getContextId();
        new ContextSyncRunner() {
            @Override
            public Object runCallBack(TestContext context) {
                return null;
            }
        }.runSync(context);
    }

    @org.junit.Test
    public void positiveFlowTest() throws Exception {

        ContextSyncRunner runner = spy(new ContextSyncRunner() {
            @Override
            public Object runCallBack(TestContext context) {
                new TestContextService().setTestContextInitialized();
                return new Object();
            }

            @Override
            protected void afterLocked() {
                super.afterLocked();
            }

            @Override
            protected void onInitialized() {
                super.onInitialized();
            }

            @Override
            TestContextService getTestContextService() {
                return serviceMock;
            }
        });

        InOrder testServiceVerifier = inOrder(serviceMock);
        InOrder runnerVerifier = inOrder(runner);

        Integer contextIndex = serviceMock.getCurrentTestContext().getContextId();

        runner.runSync(serviceMock.getCurrentTestContext());

        testServiceVerifier.verify(serviceMock, never()).getTestContext(contextIndex);
        testServiceVerifier.verify(serviceMock, calls(1)).lock();
        testServiceVerifier.verify(serviceMock, calls(1)).setContextIdToUse(contextIndex);
        testServiceVerifier.verify(serviceMock, calls(1)).waitForInitialization();
        testServiceVerifier.verify(serviceMock, calls(1)).unLock();
        runnerVerifier.verify(runner, calls(1)).afterLocked();
        runnerVerifier.verify(runner, calls(1)).runCallBack(serviceMock.getCurrentTestContext());
        runnerVerifier.verify(runner, calls(1)).onInitialized();
    }
}
