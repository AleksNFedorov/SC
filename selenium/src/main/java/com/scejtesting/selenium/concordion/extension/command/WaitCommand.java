package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.Element;
import org.concordion.api.listener.AssertListener;

/**
 * Created by aleks on 5/3/14.
 */
public class WaitCommand extends AbstractSeleniumDriverCommand {

    public WaitCommand(AssertListener listener) {
        super(listener);
    }

    @Override
    protected void processDriverCommand(Object parameter, Element element) {

        try {
            webTestFixture.waitSeconds(Long.valueOf(parameter.toString()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getCommandName() {
        return "waitSeconds";
    }
}
