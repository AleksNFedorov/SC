package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.Element;
import org.concordion.api.listener.AssertListener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by aleks on 5/5/14.
 */
public abstract class AbstractCheckElementStateCommand extends AbstractSeleniumCheckCommand {
    public AbstractCheckElementStateCommand(AssertListener listener) {
        super(listener);
    }

    @Override
    public CommandResult performCheck(Object parameter, Element element) {

        boolean checkPassed;

        if (parameter instanceof By) {
            checkPassed = checkWithBy((By) parameter);
        } else if (parameter instanceof WebElement) {
            checkPassed = checkWithWebElement((WebElement) parameter);
        } else {
            throw new IllegalArgumentException("Parameter has incorrect type [" + parameter.getClass() + "]");
        }

        return buildResult(checkPassed);
    }

    protected CommandResult buildResult(boolean checkPassed) {
        if (checkPassed) {
            return buildSuccessResult();
        } else {
            return buildFailResult();
        }
    }

    protected abstract boolean checkWithBy(By byValue);

    protected abstract boolean checkWithWebElement(WebElement element);
}
