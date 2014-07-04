package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.Element;
import org.concordion.api.listener.AssertListener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by aleks on 5/6/14.
 */
public class ClearElement extends AbstractSeleniumDriverCommand {

    public ClearElement(AssertListener listener) {
        super(listener);
    }

    @Override
    protected void processDriverCommand(Object parameter, Element element) {

        if (parameter instanceof By) {
            getTestFixture().clearElement((By) parameter);
        } else if (parameter instanceof WebElement) {
            getTestFixture().clearElement((WebElement) parameter);
        } else {
            throw new IllegalArgumentException("Parameter has incorrect type [" + parameter.getClass() + "]");
        }

    }

    @Override
    public String getCommandName() {
        return "clearElement";
    }
}
