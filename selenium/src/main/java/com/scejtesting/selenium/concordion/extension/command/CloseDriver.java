package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.Element;
import org.concordion.api.Evaluator;
import org.concordion.api.ResultRecorder;
import org.concordion.api.listener.AssertListener;
import org.concordion.api.listener.AssertSuccessEvent;

/**
 * User: Fedorovaleks
 * Date: 3/31/14
 */
public class CloseDriver extends AbstractSeleniumDriverCommand {

    public CloseDriver(AssertListener listener) {
        super(listener);
    }

    @Override
    protected void processDriverCommand(Object parameter, Element element, ResultRecorder resultRecorder) {
        getTestFixture().closeCurrentDriver();
        listeners.announce().successReported(new AssertSuccessEvent(element));
    }

    @Override
    protected Object evaluateExpression(String expression, Evaluator evaluator) {
        LOG.info("No expression expected for close driver command");
        return null;
    }

    @Override
    public String getCommandName() {
        return "closeCurrentDriver";
    }
}
