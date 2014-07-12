package com.scejtesting.core.concordion.command;

import com.scejtesting.core.Constants;
import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.Evaluator;
import org.concordion.internal.SimpleEvaluatorFactory;
import org.junit.Assert;

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

        TestContextService service = new TestContextService();
        TestContext testContext = service.createNewTestContext(mockTest);
        testContext = service.cloneContext(testContext.getContextId());

        ScejRunCommand runCommand = spy(new ScejRunCommand());

        doReturn(testContext).when(runCommand).getTestContext();
        doNothing().when(runCommand).executeConcordionRun(null, evaluator, null);

        runCommand.execute(null, evaluator, null);


        Object contextIdFromEvaluator = evaluator.getVariable(Constants.CONCORDION_VARIABLE_FOR_TEST_CONTEXT);

        Assert.assertEquals(testContext.getContextId(), contextIdFromEvaluator);
    }
}
