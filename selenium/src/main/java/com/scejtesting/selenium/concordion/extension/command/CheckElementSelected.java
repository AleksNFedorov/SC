package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.listener.AssertListener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by aleks on 5/4/14.
 */
public class CheckElementSelected extends AbstractCheckElementStateCommand {

    public CheckElementSelected(AssertListener listener) {
        super(listener);
    }

    @Override
    protected boolean checkWithBy(By byValue) {
        return getTestFixture().checkElementSelected(byValue);
    }

    @Override
    protected boolean checkWithWebElement(WebElement element) {
        return getTestFixture().checkElementSelected(element);
    }

    @Override
    public String getCommandName() {
        return "checkElementSelected";
    }
}
