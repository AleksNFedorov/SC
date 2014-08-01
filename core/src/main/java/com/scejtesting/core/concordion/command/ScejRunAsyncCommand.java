package com.scejtesting.core.concordion.command;

import com.scejtesting.core.concordion.async.AsyncSpecExecutionCallable;
import com.scejtesting.core.config.Suite;
import com.scejtesting.core.config.SuiteConfiguration;
import org.concordion.api.CommandCall;
import org.concordion.api.Evaluator;
import org.concordion.api.ResultRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aleks on 7/23/14.
 */
public class ScejRunAsyncCommand extends ScejRunCommand {

    private static Logger LOG = LoggerFactory.getLogger(ScejRunAsyncCommand.class);

    @Override
    public void execute(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
        LOG.debug("method invoked");

        saveLinkElement(commandCall.getElement());
        LOG.info("element saved [{}]", commandCall.getElement().getText());

        AsyncSpecExecutionCallable asyncCall = new AsyncSpecExecutionCallable(
                getTestContext(),
                commandCall,
                resultRecorder);
        submitResult(asyncCall);
        getTestContext().getCurrentSpecificationContext().onNewAsyncCall();
        LOG.info("Async call submitted");

        LOG.debug("method finished");
    }

    private void submitResult(Runnable taskToRun) {
        getSuite().submitAsyncTask(taskToRun);
    }

    protected Suite getSuite() {
        return SuiteConfiguration.getInstance().getSuite();
    }

    @Override
    public String getCommandName() {
        return "runAsync";
    }
}
