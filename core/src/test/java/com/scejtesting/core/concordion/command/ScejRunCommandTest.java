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
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.Mockito.*;

/**
 * Created by aleks on 7/12/14.
 */
public class ScejRunCommandTest {


    private Test testMock;

    @Before
    public void initTest() {
        Specification testSpec = new Specification("/test.html");
        testMock = mock(Test.class);
        when(testMock.getSpecification()).thenReturn(testSpec);
        when(testMock.getName()).thenReturn("runCommandTest");

        TestContextService service = new TestContextService();
        service.createNewTestContext(testMock);
    }

    @After
    public void tearDownTest() {
        TestContextService service = new TestContextService();
        service.dropContext(service.getCurrentTestContext());
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testInitializationException_noTestContext() {

        TestContextService service = new TestContextService();
        service.dropContext(service.getCurrentTestContext());

        try {
            new ScejRunCommand();
        } finally {
            service.createNewTestContext(testMock);
        }
    }

    @org.junit.Test
    public void testCommandCreated_validContext() {

        TestContextService service = new TestContextService();
        TestContext testContext = service.cloneContext(service.getCurrentTestContext());
        service.switchContext(testContext);

        ScejRunCommand runCommand = new ScejRunCommand();

        Assert.assertSame(testContext, runCommand.getTestContext());
        Assert.assertEquals("run", runCommand.getCommandName());

        service.revertContextSwitch();
        service.dropContext(testContext);
    }

    @org.junit.Test
    public void positiveFlowTest() {

        Evaluator evaluator = new SimpleEvaluatorFactory().createEvaluator(this);

        when(testMock.getName()).thenReturn("runCommandTest");

        final TestContextService service = new TestContextService();
        TestContext testContext = service.cloneContext(service.getCurrentTestContext());

        final AtomicBoolean check = new AtomicBoolean(false);

        Element runElement = new Element("a");
        runElement.addAttribute("href", testMock.getSpecification().getLocation());

        final CommandCall commandCall = new CommandCall(null, runElement, null, null);

        ScejRunCommand runCommand = spy(new ScejRunCommand() {
            @Override
            void executeConcordionRun(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
                check.set(true);
                service.revertContextSwitch();
            }
        });

        doReturn(testContext).when(runCommand).getTestContext();

        runCommand.execute(commandCall, evaluator, null);

        Assert.assertTrue(check.get());
        Assert.assertSame(runElement, testContext.getChildSpecificationElement(testMock.getSpecification().getLocation()));
    }
}
