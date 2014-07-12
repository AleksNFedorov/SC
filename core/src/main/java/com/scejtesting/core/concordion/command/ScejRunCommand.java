package com.scejtesting.core.concordion.command;

import com.scejtesting.core.Constants;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.CommandCall;
import org.concordion.api.Evaluator;
import org.concordion.api.ResultRecorder;
import org.concordion.internal.command.RunCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aleks on 7/12/14.
 */
public class ScejRunCommand extends RunCommand implements ScejCommand {

    private final TestContext currentTestContext = new TestContextService().getCurrentTestContext();
    private static Logger LOG = LoggerFactory.getLogger(RegisterGlobalVariablesCommand.class);

    @Override
    public void execute(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
        LOG.debug("method invoked");
        Integer contextId = getTestContext().getContextId();
        evaluator.setVariable(Constants.CONCORDION_VARIABLE_FOR_TEST_CONTEXT, contextId);
        LOG.info("TestContext id variable set to [{}]", contextId);
        executeConcordionRun(commandCall, evaluator, resultRecorder);
        LOG.debug("Method finished");
    }

    void executeConcordionRun(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
        super.execute(commandCall, evaluator, resultRecorder);
    }

    protected TestContext getTestContext() {
        return currentTestContext;
    }

    @Override
    public String getCommandName() {
        return "run";
    }
}
