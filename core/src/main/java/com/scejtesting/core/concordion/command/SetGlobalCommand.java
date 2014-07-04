package com.scejtesting.core.concordion.command;

import com.scejtesting.core.context.Context;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.CommandCall;
import org.concordion.api.Evaluator;
import org.concordion.api.ResultRecorder;
import org.concordion.internal.command.SetCommand;
import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Fedorovaleks
 * Date: 20.03.14
 */
public class SetGlobalCommand extends SetCommand implements ScejCommand {

    public static final String COMMAND_NAME = "setGlobal";
    private static Logger LOG = LoggerFactory.getLogger(SetGlobalCommand.class);

    @Override
    public void setUp(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
        LOG.debug("method invoked");
        super.setUp(commandCall, evaluator, resultRecorder);
        saveVariableAsGlobal(commandCall.getExpression(), evaluator);
        LOG.debug("method finished");
    }

    private void saveVariableAsGlobal(String expression, Evaluator evaluator) {
        LOG.debug("method invoked [{}]", expression);

        String variableName = extractVariableName(expression);

        LOG.debug("variable name in expression [{}] resolved as [{}]", expression, variableName);

        Object variableValue = evaluator.getVariable(variableName);

        LOG.debug("variable [{}] value is [{}]", variableName, variableValue);

        Check.notNull(variableValue, "Can't extract variable [" + variableName + "] from context");

        Context globalTestContext = getTestContext();

        globalTestContext.addGlobalVariable(variableName, variableValue);

        LOG.info("variable [{}] stored to global context ", variableName);

        LOG.debug("method finished");
    }

    private String extractVariableName(String expression) {
        int lastIndex = expression.contains("=") ? expression.indexOf("=") : expression.length();
        return expression.substring(0, lastIndex).trim();
    }

    protected Context getTestContext() {
        return new TestContextService().getCurrentTestContext();
    }

    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }
}
