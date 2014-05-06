package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.listener.AssertListener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by aleks on 5/4/14.
 */
public class CheckElementExist extends AbstractCheckStateCommand {


    public CheckElementExist(AssertListener listener) {
        super(listener);
    }

    @Override
    protected boolean checkWithBy(By byValue) {
        return getTestFixture().checkElementExist(byValue);
    }

    @Override
    protected boolean checkWithWebElement(WebElement element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCommandName() {
        return "checkElementExist";
    }
}
