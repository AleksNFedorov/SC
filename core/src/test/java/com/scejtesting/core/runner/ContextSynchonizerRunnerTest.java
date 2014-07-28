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
public class ContextSynchonizerRunnerTest {

    private TestContextService serviceMock;

    @Before
    public void initTest() {
        Specification rootSpec = new Specification("/root.html");
        Test test = mock(Test.class);
        when(test.getSpecification()).thenReturn(rootSpec);
        when(test.getName()).thenReturn("testMock");
        serviceMock = spy(new TestContextService());
        serviceMock.revertContextSwitch();
        serviceMock.createNewTestContext(test);
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void noContextTest() {
        new ContextSynchronizer() {
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
        new ContextSynchronizer() {
            @Override
            public Object runCallBack(TestContext context) {
                return null;
            }
        }.runSync(context);
    }

    @org.junit.Test
    public void positiveFlowTest() throws Exception {

        ContextSynchronizer runner = spy(new ContextSynchronizer() {
            @Override
            public Object runCallBack(TestContext context) {
                serviceMock.revertContextSwitch();
                return new Object();
            }

            @Override
            TestContextService getTestContextService() {
                return serviceMock;
            }
        });

        InOrder testServiceVerifier = inOrder(serviceMock);
        InOrder runnerVerifier = inOrder(runner);

        TestContext currentTestContext = serviceMock.getCurrentTestContext();

        runner.runSync(serviceMock.getCurrentTestContext());

        testServiceVerifier.verify(serviceMock, calls(1)).switchContext(currentTestContext);
        testServiceVerifier.verify(serviceMock, calls(1)).revertContextSwitch();
        runnerVerifier.verify(runner, calls(1)).runCallBack(currentTestContext);
    }
}
