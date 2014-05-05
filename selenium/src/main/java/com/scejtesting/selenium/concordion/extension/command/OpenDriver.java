package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.Element;
import org.concordion.api.listener.AssertListener;
import org.concordion.api.listener.AssertSuccessEvent;


/**
 * User: Fedorovaleks
 * Date: 3/31/14
 */
public class OpenDriver extends AbstractSeleniumDriverCommand {


    public OpenDriver(AssertListener listener) {
        super(listener);
    }

    @Override
    protected void processDriverCommand(Object parameter, Element element) {

        webTestFixture.openDriver(parameter.toString());
        listeners.announce().successReported(new AssertSuccessEvent(element));
    }

    @Override
    public String getCommandName() {
        return "openDriver";
    }
}
