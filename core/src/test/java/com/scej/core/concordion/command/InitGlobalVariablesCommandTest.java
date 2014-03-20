package com.scej.core.concordion.command;

import com.scej.core.context.Context;
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
public class InitGlobalVariablesCommandTest {

    @Test
    public void initGlobalVariablesTest() {

        Evaluator evaluator = new SimpleEvaluatorFactory().createEvaluator(this);

        Context globalTestContext = new Context();

        globalTestContext.addGlobalVariable("#var1", "var1Value");
        globalTestContext.addGlobalVariable("#var2", "var2Value");


        InitGlobalVariablesCommand initGlobalVariableCommand = spy(new InitGlobalVariablesCommand());

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
