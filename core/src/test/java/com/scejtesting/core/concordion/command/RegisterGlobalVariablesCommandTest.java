package com.scejtesting.core.concordion.command;

import com.scejtesting.core.context.Context;
import org.concordion.api.Evaluator;
import org.concordion.internal.SimpleEvaluatorFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * User: Fedorovaleks
 * Date: 20.03.14
 */
public class RegisterGlobalVariablesCommandTest {

    @Test
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
