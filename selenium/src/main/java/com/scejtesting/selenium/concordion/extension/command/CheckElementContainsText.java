package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.listener.AssertListener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by aleks on 5/4/14.
 */
public class CheckElementContainsText extends AbstractCheckElementContentCommand {

    public CheckElementContainsText(AssertListener listener) {
        super(listener);
    }

    @Override
    protected boolean doCheck(By predicate, String content, List allParameters) {
        return getTestFixture().checkElementContainsText(predicate, content);
    }

    @Override
    protected boolean doCheck(WebElement element, String content, List allParameters) {
        return getTestFixture().checkElementContainsText(element, content);
    }

    @Override
    public String getCommandName() {
        return "checkElementContainsText";
    }

}
