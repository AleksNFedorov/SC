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
 * Created by aleks on 5/6/14.
 */
public class CheckChildExist extends AbstractSeleniumDriverCommand {

    public CheckChildExist(AssertListener listener) {
        super(listener);
    }

    @Override
    protected void processDriverCommand(Object parameter, Element element) {

        validateParameters(parameter);

        List parameterList = (List) parameter;

        Object parentElementPredicate = parameterList.get(0);
        By childElementToSearch = (By) parameterList.get(1);

        boolean checkResult;

        if (parentElementPredicate instanceof By) {
            By parentSearchBy = (By) parentElementPredicate;
            checkResult = getTestFixture().checkChildExist(parentSearchBy, childElementToSearch);
        } else {
            WebElement parentElement = (WebElement) parentElementPredicate;
            checkResult = getTestFixture().checkChildExist(parentElement, childElementToSearch);
        }

        announceResult(checkResult, element, "");
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
        Check.isTrue(parameter2 instanceof By,
                "By expected as second parameter");
    }

    @Override
    public String getCommandName() {
        return "checkChildExist";
    }

}
