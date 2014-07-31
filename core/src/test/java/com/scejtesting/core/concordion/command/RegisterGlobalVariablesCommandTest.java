package com.scejtesting.core.concordion.command;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.Context;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.Evaluator;
import org.concordion.internal.SimpleEvaluatorFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * User: Fedorovaleks
 * Date: 20.03.14
 */
public class RegisterGlobalVariablesCommandTest {

    private TestContext currentTestContext;
    private Test mockTest;

    @Before
    public void initTest() {
        Specification root = spy(new Specification("/com/scej/Head.html"));

        com.scejtesting.core.config.Test test = mock(com.scejtesting.core.config.Test.class);
        when(test.getSpecification()).thenReturn(root);
        when(test.getName()).thenReturn("testName");

        currentTestContext = new TestContextService().createNewTestContext(test);

        mockTest = test;
    }

    @After
    public void finishTest() {
        new TestContextService().dropContext(currentTestContext);
    }


    @org.junit.Test
    public void initGlobalVariablesTest() {

        Evaluator evaluator = new SimpleEvaluatorFactory().createEvaluator(this);

        Context globalTestContext = new Context();

        globalTestContext.addGlobalVariable("#var1", "var1Value");
        globalTestContext.addGlobalVariable("#var2", "var2Value");

        RegisterGlobalVariablesCommand initGlobalVariableCommand = spy(new RegisterGlobalVariablesCommand());

        doReturn(globalTestContext).when(initGlobalVariableCommand).getTestContext();

        initGlobalVariableCommand.setUp(null, evaluator, null);

        Map<String, ?> globalVariablesMap = globalTestContext.getGlobalVariables();

        Assert.assertEquals(2, globalVariablesMap.size());

        for (Map.Entry<String, ?> globalVariableEntry : globalVariablesMap.entrySet()) {

            Object globalVariable = globalVariableEntry.getValue();
            Object evaluatorContextValue = evaluator.getVariable(globalVariableEntry.getKey());

            Assert.assertEquals(globalVariable, evaluatorContextValue);
        }

    }
}
