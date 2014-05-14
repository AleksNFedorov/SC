package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.listener.AssertListener;
import org.concordion.internal.util.Check;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by aleks on 5/10/14.
 */
public class CheckElementAttributeContainsText extends AbstractElementCheckCommand {

    public CheckElementAttributeContainsText(AssertListener listener) {
        super(listener);
    }

    @Override
    protected void validateParameters(Object parameter) {
        super.validateParameters(parameter);

        List parameterList = (List) parameter;

        Check.isTrue(parameterList.size() >= 3, "Three parameters expected");

        Object textToCheck = parameterList.get(2);

        Check.isTrue(textToCheck instanceof String, "String expected as text to search");


    }

    @Override
    protected boolean doCheck(By predicate, String content, List allParameters) {
        String textToSearch = allParameters.get(2).toString();
        return getTestFixture().checkElementAttributeContainsText(predicate, content, textToSearch);
    }

    @Override
    protected boolean doCheck(WebElement element, String content, List allParameters) {
        String textToSearch = allParameters.get(2).toString();
        return getTestFixture().checkElementAttributeContainsText(element, content, textToSearch);
    }

    @Override
    public String getCommandName() {
        return "checkElementAttributeContainsText";
    }
}
