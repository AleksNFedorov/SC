package com.scejtesting.core.concordion.command;

import com.scejtesting.core.concordion.async.AsyncExecutionResult;
import com.scejtesting.core.concordion.async.AsyncSpecExecutionCallable;
import org.concordion.api.CommandCall;
import org.concordion.api.Evaluator;
import org.concordion.api.ResultRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by aleks on 7/23/14.
 */
public class ScejRunAsyncCommand extends ScejRunCommand {

    private static Logger LOG = LoggerFactory.getLogger(ScejRunAsyncCommand.class);

    @Override
    public void execute(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
        LOG.debug("method invoked");

        saveLinkElement(commandCall.getElement());
        LOG.info("element saved [{}]", commandCall.getElement());

        AsyncSpecExecutionCallable asyncCall = new AsyncSpecExecutionCallable(
                getTestContext(),
                commandCall,
                resultRecorder
        );
        Future<AsyncExecutionResult> asyncCallFuture = submitResult(asyncCall);
        getTestContext().getCurrentSpecificationContext().saveAsyncCall(asyncCallFuture);
        LOG.info("Async call submitted");

        LOG.debug("method finished");
    }

    private Future<AsyncExecutionResult> submitResult(AsyncSpecExecutionCallable callable) {
        //TODO replace after tests with proper implementation
        return Executors.newFixedThreadPool(1).submit(callable);
    }

    @Override
    public String getCommandName() {
        return "runAsync";
    }
}
