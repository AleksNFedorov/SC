package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.Element;
import org.concordion.api.ResultRecorder;
import org.concordion.api.listener.AssertListener;
import org.concordion.api.listener.AssertSuccessEvent;

/**
 * Created by aleks on 5/3/14.
 */
public class WaitCommand extends AbstractSeleniumDriverCommand {

    public WaitCommand(AssertListener listener) {
        super(listener);
    }

    @Override
    protected void processDriverCommand(Object parameter, Element element, ResultRecorder recorder) {

        try {
            getTestFixture().waitSeconds(Long.valueOf(parameter.toString()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        listeners.announce().successReported(new AssertSuccessEvent(element));
    }

    @Override
    public String getCommandName() {
        return "waitSeconds";
    }
}
