package com.scejtesting.core.concordion.command;

import com.scejtesting.core.context.Context;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.AbstractCommand;
import org.concordion.api.CommandCall;
import org.concordion.api.Evaluator;
import org.concordion.api.ResultRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * User: Fedorovaleks
 * Date: 20.03.14
 */
public class RegisterGlobalVariablesCommand extends AbstractCommand implements ScejCommand {

    public static final String COMMAND_NAME = "registerGlobals";
    private static Logger LOG = LoggerFactory.getLogger(RegisterGlobalVariablesCommand.class);


    @Override
    public void setUp(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
        LOG.debug("method invoked");
        Context currentTestContext = getTestContext();

        Map<String, ?> globalVariables = currentTestContext.getGlobalVariables();

        LOG.info("[{}] global variables found ", globalVariables.size());

        for (Map.Entry<String, ?> globalVariable : globalVariables.entrySet()) {
            evaluator.setVariable(globalVariable.getKey(), globalVariable.getValue());
            LOG.debug("Variable [{}][{}] has been added to specification context ",
                    globalVariable.getKey(),
                    globalVariable.getValue());
        }

        LOG.debug("method finished");
    }

    protected Context getTestContext() {
        return new TestContextService().getCurrentTestContext();
    }

    @Override
    public String getCommandType() {
        return COMMAND_NAME;
    }
}
