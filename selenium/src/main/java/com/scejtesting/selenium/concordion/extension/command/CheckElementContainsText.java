package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.Element;
import org.concordion.api.listener.AssertFailureEvent;
import org.concordion.api.listener.AssertListener;
import org.concordion.api.listener.AssertSuccessEvent;
import org.concordion.internal.util.Check;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by aleks on 5/4/14.
 */
public class CheckElementContainsText extends AbstractSeleniumDriverCommand {


    public CheckElementContainsText(AssertListener listener) {
        super(listener);
    }

    @Override
    protected void processDriverCommand(Object parameter, Element element) {

        validateParameters(parameter);

        List parameterList = (List) parameter;

        String textToSearch = (String) parameterList.get(1);
        Object elementSearchPredicate = parameterList.get(0);

        boolean checkResult;

        if (elementSearchPredicate instanceof By) {
            By bySearchPredicate = (By) elementSearchPredicate;
            checkResult = getTestFixture().checkElementContainsText(bySearchPredicate, textToSearch);
        } else {
            WebElement elementToCheck = (WebElement) elementSearchPredicate;
            checkResult = getTestFixture().checkElementContainsText(elementToCheck, textToSearch);
        }

        announceResult(checkResult, element, textToSearch);
    }

    private void announceResult(boolean checkPassed, Element element, String textToSearch) {
        if (checkPassed) {
            listeners.announce().successReported(new AssertSuccessEvent(element));
        } else {
            listeners.announce().failureReported(new AssertFailureEvent(element, textToSearch, ""));
        }
    }

    private void validateParameters(Object parameter) {
        Check.isTrue(parameter instanceof List, "Parameter is not a List");

        List parametersList = (List) parameter;

        Check.isTrue(parametersList.size() == 2, "Two parameters expected");

        Object parameter1 = parametersList.get(0);
        Object parameter2 = parametersList.get(1);

        Check.isTrue(parameter1 instanceof By || parameter1 instanceof WebElement,
                "By or WebElement expected as first parameter");
        Check.isTrue(parameter2 instanceof String, "String expected as second parameter");
    }

    @Override
    public String getCommandName() {
        return "checkElementContainsText";
    }


}