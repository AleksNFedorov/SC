package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.Element;
import org.concordion.api.listener.AssertListener;

/**
 * Created by aleks on 5/4/14.
 */
public class CheckElementContainsText extends AbstractSeleniumDriverCommand {


    public CheckElementContainsText(AssertListener listener) {
        super(listener);
    }

    @Override
    protected void processDriverCommand(Object parameter, Element element) {

    }

    @Override
    public String getCommandName() {
        return null;
    }


}
