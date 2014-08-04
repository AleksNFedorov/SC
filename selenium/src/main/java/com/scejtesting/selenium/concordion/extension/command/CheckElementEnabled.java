package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.listener.AssertListener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by aleks on 5/4/14.
 */
public class CheckElementEnabled extends AbstractCheckElementStateCommand {

    public CheckElementEnabled(AssertListener listener) {
        super(listener);
    }

    @Override
    protected boolean checkWithBy(By byValue) {
        return getTestFixture().checkElementEnabled(byValue);
    }

    @Override
    protected boolean checkWithWebElement(WebElement element) {
        return getTestFixture().checkElementEnabled(element);
    }

    @Override
    public String getCommandName() {
        return "checkElementEnabled";
    }
}
