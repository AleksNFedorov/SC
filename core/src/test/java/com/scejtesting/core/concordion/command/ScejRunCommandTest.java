package com.scejtesting.core.concordion.command;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.CommandCall;
import org.concordion.api.Element;
import org.concordion.api.Evaluator;
import org.concordion.api.ResultRecorder;
import org.concordion.internal.SimpleEvaluatorFactory;
import org.junit.Assert;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.Mockito.*;

/**
 * Created by aleks on 7/12/14.
 */
public class ScejRunCommandTest {


    @org.junit.Test
    public void testInitialization() {

        Specification testSpec = new Specification("/test.html");
        Test mockTest = mock(Test.class);
        when(mockTest.getSpecification()).thenReturn(testSpec);
        when(mockTest.getName()).thenReturn("runCommandTest");

        TestContextService service = new TestContextService();
        TestContext testContext = service.createNewTestContext(mockTest);
        testContext = service.cloneContext(testContext.getContextId());
        service.setContextIdToUse(testContext.getContextId());

        ScejRunCommand runCommand = new ScejRunCommand();

        Assert.assertSame(testContext, runCommand.getTestContext());
        Assert.assertEquals("run", runCommand.getCommandName());

    }

    @org.junit.Test
    public void positiveFlowTest() {

        Evaluator evaluator = new SimpleEvaluatorFactory().createEvaluator(this);

        Specification testSpec = new Specification("/test.html");
        Test mockTest = mock(Test.class);
        when(mockTest.getSpecification()).thenReturn(testSpec);
        when(mockTest.getName()).thenReturn("runCommandTest");

        final TestContextService service = new TestContextService();
        TestContext testContext = service.createNewTestContext(mockTest);
        testContext = service.cloneContext(testContext.getContextId());

        final AtomicBoolean check = new AtomicBoolean(false);

        Element runElement = new Element("a");
        runElement.addAttribute("href", testSpec.getLocation());

        final CommandCall commandCall = new CommandCall(null, runElement, null, null);

        ScejRunCommand runCommand = spy(new ScejRunCommand() {
            @Override
            void executeConcordionRun(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
                check.set(true);
                service.setTestContextInitialized();
            }
        });

        doReturn(testContext).when(runCommand).getTestContext();

        runCommand.execute(commandCall, evaluator, null);

        Assert.assertTrue(check.get());
        Assert.assertSame(runElement, testContext.getChildSpecificationElement(testSpec.getLocation()));
    }
}
