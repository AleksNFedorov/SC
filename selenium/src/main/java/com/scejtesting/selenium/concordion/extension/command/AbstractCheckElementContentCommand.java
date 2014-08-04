package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.Element;
import org.concordion.api.listener.AssertListener;
import org.concordion.internal.util.Check;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by aleks on 5/4/14.
 */
public abstract class AbstractCheckElementContentCommand extends AbstractSeleniumCheckCommand {

    public AbstractCheckElementContentCommand(AssertListener listener) {
        super(listener);
    }

    @Override
    public CommandResult performCheck(Object parameter, Element element) {

        validateParameters(parameter);

        List parameterList = (List) parameter;

        String contentString = (String) parameterList.get(1);
        Object elementSearchPredicate = parameterList.get(0);

        boolean checkPassed;

        if (elementSearchPredicate instanceof By) {
            By bySearchPredicate = (By) elementSearchPredicate;
            checkPassed = doCheck(bySearchPredicate, contentString, parameterList);
        } else {
            WebElement elementToCheck = (WebElement) elementSearchPredicate;
            checkPassed = doCheck(elementToCheck, contentString, parameterList);
        }

        return buildResult(checkPassed, contentString);
    }

    protected abstract boolean doCheck(By predicate, String content, List allParameters);

    protected abstract boolean doCheck(WebElement element, String content, List allParameters);

    protected CommandResult buildResult(boolean checkPassed, String textToSearch) {
        if (checkPassed) {
            return buildSuccessResult();
        } else {
            return buildFailResult(textToSearch, "");
        }
    }

    protected void validateParameters(Object parameter) {
        Check.isTrue(parameter instanceof List, "Parameter is not a List");

        List parametersList = (List) parameter;

        Check.isTrue(parametersList.size() >= 2, "At least two parameters expected");

        Object parameter1 = parametersList.get(0);
        Object parameter2 = parametersList.get(1);

        Check.isTrue(parameter1 instanceof By || parameter1 instanceof WebElement,
                "By or WebElement expected as first parameter");
        Check.isTrue(parameter2 instanceof String, "String expected as second parameter");
    }

}
