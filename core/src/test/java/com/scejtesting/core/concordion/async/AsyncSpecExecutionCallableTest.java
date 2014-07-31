package com.scejtesting.core.concordion.async;

import com.scejtesting.core.concordion.SyncResultRecorderWrapper;
import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.CommandCall;
import org.concordion.api.ResultRecorder;
import org.concordion.internal.SummarizingResultRecorder;
import org.concordion.internal.command.RunCommand;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

/**
 * Created by aleks on 7/29/14.
 */
public class AsyncSpecExecutionCallableTest {

    private static final CommandCall commandCallStub = new CommandCall(null, null, null, null);
    private static final ResultRecorder resultRecorderStub = new SummarizingResultRecorder();

    private TestContext testContext;
    private Test testMock;

    @Before
    public void initTest() {
        Specification spec = new Specification("/somePath.html");
        Test test = mock(Test.class);
        doReturn(spec).when(test).getSpecification();
        TestContext context = new TestContextService().createNewTestContext(test);
        testContext = context;
        testMock = test;
    }

    @After
    public void finishTest() {
        if (!TestContext.isDestroyedContext(testContext)) {
            new TestContextService().dropContext(testContext);
        }

        new TestContextService().revertContextSwitch();
    }

    @org.junit.Test
    public void testException_nullMethodCallParameters() {

        try {
            new AsyncSpecExecutionCallable(null, commandCallStub, resultRecorderStub);
            Assert.fail("Context null exception expected");
        } catch (RuntimeException ex) {
        }

        try {
            new AsyncSpecExecutionCallable(testContext, null, resultRecorderStub);
            Assert.fail("Command call null exception expected");
        } catch (RuntimeException ex) {
        }

        try {
            new AsyncSpecExecutionCallable(testContext, commandCallStub, null);
            Assert.fail("Results record null exception expected");
        } catch (RuntimeException ex) {
        }

    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testException_destroyedContextToClone() {
        testContext.destroyTestContext();
        new AsyncSpecExecutionCallable(testContext, commandCallStub, resultRecorderStub);
    }

    @org.junit.Test
    public void testAsyncCallFinished_runnerException() {
        AsyncSpecExecutionCallable runner = spy(new AsyncSpecExecutionCallable(testContext,
                commandCallStub,
                resultRecorderStub));

        TestContextService serviceSpy = spy(new TestContextService());

        when(runner.buildRunCommand()).thenThrow(IllegalArgumentException.class);
        when(runner.buildTestContextService()).thenReturn(serviceSpy);

        testContext.getCurrentSpecificationContext().onNewAsyncCall();

        runner.run();

        Assert.assertEquals(Integer.valueOf(0),
                testContext.getCurrentSpecificationContext().getRunningAsyncCallsAmount());

        InOrder inOrder = inOrder(serviceSpy);

        inOrder.verify(serviceSpy, calls(1)).dropContext(any(TestContext.class));
    }


    @org.junit.Test
    public void testExecutionPassed_validData() {
        AsyncSpecExecutionCallable runner = spy(new AsyncSpecExecutionCallable(
                testContext,
                commandCallStub,
                resultRecorderStub));

        TestContextService serviceSpy = spy(new TestContextService());

        RunCommand commandMock = mock(RunCommand.class);

        when(runner.buildRunCommand()).thenReturn(commandMock);
        when(runner.buildTestContextService()).thenReturn(serviceSpy);

        testContext.getCurrentSpecificationContext().onNewAsyncCall();

        runner.run();

        Assert.assertEquals(Integer.valueOf(0),
                testContext.getCurrentSpecificationContext().getRunningAsyncCallsAmount());

        InOrder inOrder = inOrder(serviceSpy, commandMock);

        inOrder.verify(commandMock, calls(1)).execute(
                any(CommandCall.class),
                any(AsyncSpecExecutionCallable.FakeEvaluator.class),
                any(SyncResultRecorderWrapper.class));
        inOrder.verify(serviceSpy, calls(1)).dropContext(any(TestContext.class));
    }
}
