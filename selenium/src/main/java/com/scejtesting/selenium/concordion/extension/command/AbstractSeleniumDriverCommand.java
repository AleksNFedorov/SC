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

    protected final WebTestFixture seleniumDriverManagerService = new WebTestFixture();

    protected Announcer<AssertListener> listeners = Announcer.to(AssertListener.class);

    public AbstractSeleniumDriverCommand(AssertListener listener) {
        listeners.addListener(listener);
    }

    protected abstract void processDriverCommand(Object expression, Element element);

    @Override
    public final void setUp(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {

        String expression = commandCall.getExpression();
        Object evaluationResult = evaluator.evaluate(expression);
        LOG.info("Driver name evaluated as [{}]", expression);

        processDriverCommand(evaluationResult, commandCall.getElement());

    }


}
