package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.listener.AssertListener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by aleks on 5/4/14.
 */
public class CheckElementDisplayed extends AbstractCheckElementStateCommand {

    public CheckElementDisplayed(AssertListener listener) {
        super(listener);
    }

    @Override
    protected boolean checkWithBy(By byValue) {
        return getTestFixture().checkElementDisplayed(byValue);
    }

    @Override
    protected boolean checkWithWebElement(WebElement element) {
        return element.isDisplayed();
    }

    @Override
    public String getCommandName() {
        return "checkElementDisplayed";
    }
}
