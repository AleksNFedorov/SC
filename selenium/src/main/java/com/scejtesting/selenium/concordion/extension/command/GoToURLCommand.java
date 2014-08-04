package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.Element;
import org.concordion.api.ResultRecorder;
import org.concordion.api.listener.AssertListener;
import org.concordion.api.listener.AssertSuccessEvent;
import org.concordion.internal.util.Check;

/**
 * Created by aleks on 8/3/14.
 */
public class GoToURLCommand extends AbstractSeleniumDriverCommand {

    public GoToURLCommand(AssertListener listener) {
        super(listener);
    }

    @Override
    protected void processDriverCommand(Object parameter, Element element, ResultRecorder recoreder) {

        Check.notEmpty(parameter.toString(), "URL cant be mepty");

        getTestFixture().goToURL(parameter.toString());
        listeners.announce().successReported(new AssertSuccessEvent(element));
    }

    @Override
    public String getCommandName() {
        return "goToUrl";
    }
}
