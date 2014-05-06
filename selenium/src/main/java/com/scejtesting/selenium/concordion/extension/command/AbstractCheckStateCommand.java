package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.Element;
import org.concordion.api.listener.AssertFailureEvent;
import org.concordion.api.listener.AssertListener;
import org.concordion.api.listener.AssertSuccessEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by aleks on 5/5/14.
 */
public abstract class AbstractCheckStateCommand extends AbstractSeleniumDriverCommand {
    public AbstractCheckStateCommand(AssertListener listener) {
        super(listener);
    }

    @Override
    protected void processDriverCommand(Object parameter, Element element) {

        boolean checkResult;

        if (parameter instanceof By) {
            checkResult = checkWithBy((By) parameter);
        } else if (parameter instanceof WebElement) {
            checkResult = checkWithWebElement((WebElement) parameter);
        } else {
            throw new IllegalArgumentException("Parameter has incorrect type [" + parameter.getClass() + "]");
        }

        if (checkResult) {
            listeners.announce().successReported(new AssertSuccessEvent(element));
        } else {
            listeners.announce().failureReported(new AssertFailureEvent(element, "", ""));
        }
    }

    protected abstract boolean checkWithBy(By byValue);

    protected abstract boolean checkWithWebElement(WebElement element);
}
