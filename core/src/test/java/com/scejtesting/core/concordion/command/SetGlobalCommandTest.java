package com.scejtesting.core.concordion.command;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.context.Context;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.CommandCall;
import org.concordion.api.Element;
import org.concordion.api.Evaluator;
import org.concordion.internal.SimpleEvaluatorFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * User: Fedorovaleks
 * Date: 20.03.14
 */
public class SetGlobalCommandTest {

    public String getValue() {
        return "Some value";
    }

    public String getValue(String param1) {
        return param1;
    }

    @Before
    public void initTest() {
        Specification testSpec = new Specification("/test.html");
        com.scejtesting.core.config.Test testMock = mock(com.scejtesting.core.config.Test.class);
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

    @Test
    public void testEvaluator_thisClass() {

        Evaluator evaluator = new SimpleEvaluatorFactory().createEvaluator(this);

        evaluator.setVariable("#var1 = getValue()", "");

        Object var1 = evaluator.getVariable("#var1");

        Assert.assertEquals(getValue(), var1.toString());

        evaluator.setVariable("#var2 = getValue(#var1)", "");

        Object var2 = evaluator.getVariable("#var2");

        Assert.assertEquals(getValue(var1.toString()), var2.toString());

    }

    @Test
    public void testGlobalVariableSet_nonTextValue() {

        Evaluator evaluator = new SimpleEvaluatorFactory().createEvaluator(this);

        Context globalTestContext = new Context();

        Element specificationElement = new Element("element");

        CommandCall commandCall = mock(CommandCall.class);

        when(commandCall.hasChildCommands()).thenReturn(Boolean.FALSE);
        when(commandCall.getExpression()).thenReturn("#var1=getValue()");
        when(commandCall.getElement()).thenReturn(specificationElement);

        SetGlobalCommand setGlobalCommand = spy(new SetGlobalCommand());

        doReturn(globalTestContext).when(setGlobalCommand).getTestContext();

        setGlobalCommand.setUp(commandCall, evaluator, null);

        Map globalVariablesMap = globalTestContext.getGlobalVariables();

        Assert.assertEquals(1, globalVariablesMap.size());
        Assert.assertEquals(getValue(), globalVariablesMap.get("#var1").toString());

    }

    @Test
    public void testGlobalVariableSet_textValue() {

        Evaluator evaluator = new SimpleEvaluatorFactory().createEvaluator(this);

        Context globalTestContext = new Context();

        String elementValue = "value";

        Element specificationElement = new Element("element");
        specificationElement.appendText(elementValue);

        CommandCall commandCall = mock(CommandCall.class);

        when(commandCall.hasChildCommands()).thenReturn(Boolean.FALSE);
        when(commandCall.getExpression()).thenReturn("#var1");
        when(commandCall.getElement()).thenReturn(specificationElement);

        SetGlobalCommand setGlobalCommand = spy(new SetGlobalCommand());

        doReturn(globalTestContext).when(setGlobalCommand).getTestContext();

        setGlobalCommand.setUp(commandCall, evaluator, null);

        Map globalVariablesMap = globalTestContext.getGlobalVariables();

        Assert.assertEquals(1, globalVariablesMap.size());
        Assert.assertEquals(elementValue, globalVariablesMap.get("#var1").toString());
    }

}
