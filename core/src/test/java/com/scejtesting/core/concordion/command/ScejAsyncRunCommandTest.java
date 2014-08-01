package com.scejtesting.core.concordion.command;

import com.scejtesting.core.concordion.async.AsyncSpecExecutionCallable;
import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Suite;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.CommandCall;
import org.concordion.api.Element;
import org.concordion.api.Evaluator;
import org.concordion.api.ResultRecorder;
import org.concordion.internal.SimpleEvaluatorFactory;
import org.concordion.internal.SummarizingResultRecorder;
import org.junit.Assert;
import org.mockito.InOrder;

import static org.mockito.Mockito.*;

/**
 * Created by aleks on 7/12/14.
 */
public class ScejAsyncRunCommandTest {


    @org.junit.Test
    public void testCommandExecuted_validParameters() {

        final Evaluator evaluator = new SimpleEvaluatorFactory().createEvaluator(this);
        final CommandCall commandCall = new CommandCall(null, new Element("test"), null, null);
        final ResultRecorder resultRecorder = new SummarizingResultRecorder();

        Specification testSpec = new Specification("/test.html");
        Test mockTest = mock(Test.class);
        when(mockTest.getSpecification()).thenReturn(testSpec);
        when(mockTest.getName()).thenReturn("runCommandTest");

        final TestContextService service = new TestContextService();
        service.createNewTestContext(mockTest);

        Suite suiteSpy = spy(new Suite());
        doNothing().when(suiteSpy).submitAsyncTask((any(Runnable.class)));

        ScejRunAsyncCommand runCommand = spy(new ScejRunAsyncCommand());
        doNothing().when(runCommand).saveLinkElement(any(Element.class));
        doReturn(suiteSpy).when(runCommand).getSuite();

        InOrder verifier = inOrder(suiteSpy, runCommand);

        runCommand.execute(commandCall, evaluator, resultRecorder);

        Assert.assertSame("runAsync", runCommand.getCommandName());
        Assert.assertSame(service.getCurrentTestContext(), runCommand.getTestContext());
        Assert.assertEquals(Integer.valueOf(1),
                service.getCurrentTestContext().getCurrentSpecificationContext().getRunningAsyncCallsAmount());

        verifier.verify(suiteSpy, calls(1)).submitAsyncTask(any(AsyncSpecExecutionCallable.class));


        service.getCurrentTestContext().getCurrentSpecificationContext().onAsyncCallFinished();

    }
}
