package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.Element;
import org.concordion.api.Result;
import org.concordion.api.ResultRecorder;
import org.concordion.api.listener.AssertFailureEvent;
import org.concordion.api.listener.AssertListener;
import org.concordion.api.listener.AssertSuccessEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aleks on 8/3/14.
 */
public abstract class AbstractSeleniumCheckCommand extends AbstractSeleniumDriverCommand {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractSeleniumCheckCommand.class);


    public AbstractSeleniumCheckCommand(AssertListener listener) {
        super(listener);
    }

    protected class CommandResult {
        private final Result result;
        private String expected;
        private Object actual;

        private CommandResult(Result result) {
            this.result = result;
        }

        @Override
        public String toString() {
            return "CommandResult{" +
                    "result=" + result +
                    '}';
        }
    }

    public abstract CommandResult performCheck(Object parameter, Element element);

    @Override
    protected final void processDriverCommand(Object parameter, Element element, ResultRecorder resultRecorder) {
        LOG.debug("method invoked");

        CommandResult result = performCheck(parameter, element);
        LOG.info("check command result acquired [{}]", result);
        processResult(result, element, resultRecorder);
        LOG.info("Results ");

        LOG.debug("method finished");
    }

    protected CommandResult buildSuccessResult() {
        CommandResult result = new CommandResult(Result.SUCCESS);
        LOG.debug("Success result build");
        return result;
    }

    protected CommandResult buildFailResult() {
        CommandResult result = new CommandResult(Result.FAILURE);
        LOG.debug("Fail result built");
        return result;
    }

    protected CommandResult buildFailResult(String expected, Object actual) {
        CommandResult result = new CommandResult(Result.FAILURE);
        result.expected = expected;
        result.actual = actual;
        LOG.debug("Fail result built");
        return result;
    }

    private void processResult(CommandResult executionResult, Element element, ResultRecorder resultRecorder) {
        LOG.debug("method invoked");
        resultRecorder.record(executionResult.result);
        LOG.info("Result [{}] recorded", executionResult.result);
        switch (executionResult.result) {
            case SUCCESS:
                announceSuccess(element);
                LOG.info("Success result announced");
                break;
            case FAILURE:
                announceFailure(element, executionResult.expected, executionResult.actual);
                LOG.info("Fail result announced");
                break;
            default:
                LOG.error("Unknown result type [{}]", executionResult.result);
        }
        LOG.debug("method finished");
    }

    private void announceSuccess(Element element) {
        listeners.announce().successReported(new AssertSuccessEvent(element));
    }

    private void announceFailure(Element element, String expected, Object actual) {
        listeners.announce().failureReported(new AssertFailureEvent(element, expected, actual));
    }


}
