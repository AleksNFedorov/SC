package com.scejtesting.selenium.concordion.extension.command;

import com.scejtesting.core.concordion.command.ScejCommand;
import com.scejtesting.selenium.WebTestFixture;
import org.concordion.api.*;
import org.concordion.api.listener.AssertListener;
import org.concordion.internal.util.Announcer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Fedorovaleks
 * Date: 3/31/14
 */
public abstract class AbstractSeleniumDriverCommand extends AbstractCommand implements ScejCommand {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractSeleniumDriverCommand.class);

    private final WebTestFixture webTestFixture = new WebTestFixture();
    protected Announcer<AssertListener> listeners = Announcer.to(AssertListener.class);

    public AbstractSeleniumDriverCommand(AssertListener listener) {
        listeners.addListener(listener);
    }

    protected abstract void processDriverCommand(Object parameter, Element element);

    @Override
    public final void setUp(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {

        if (commandCall.hasChildCommands()) {
            evaluateChildren(commandCall.getChildren(), evaluator, resultRecorder);
        }

        Object evaluationResult = evaluator.evaluate(commandCall.getExpression());

        LOG.info("Command value evaluation finished, [{}]", evaluationResult);

        processDriverCommand(evaluationResult, commandCall.getElement());

    }

    private void evaluateChildren(CommandCallList childCommandList, Evaluator evaluator, ResultRecorder resultRecorder) {

        LOG.debug("method invoked");

        LOG.info("Command children detected [{}]", childCommandList.size());

        childCommandList.setUp(evaluator, resultRecorder);
        childCommandList.execute(evaluator, resultRecorder);

        LOG.info("Children command execution finished");


        LOG.debug("method finished");
    }

    public WebTestFixture getTestFixture() {
        return webTestFixture;
    }
}
