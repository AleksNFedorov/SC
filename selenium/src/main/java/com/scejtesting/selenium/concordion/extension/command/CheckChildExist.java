package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.Element;
import org.concordion.api.listener.AssertListener;
import org.concordion.internal.util.Check;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by aleks on 5/6/14.
 */
public class CheckChildExist extends AbstractSeleniumCheckCommand {

    public CheckChildExist(AssertListener listener) {
        super(listener);
    }

    @Override
    public CommandResult performCheck(Object parameter, Element element) {

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

        if (checkResult) {
            return buildSuccessResult();
        } else {
            return buildFailResult();
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
