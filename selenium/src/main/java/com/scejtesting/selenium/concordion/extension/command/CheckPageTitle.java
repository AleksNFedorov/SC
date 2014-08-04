package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.Element;
import org.concordion.api.listener.AssertListener;

/**
 * Created by aleks on 8/3/14.
 */
public class CheckPageTitle extends AbstractSeleniumCheckCommand {

    public CheckPageTitle(AssertListener listener) {
        super(listener);
    }

    @Override
    public CommandResult performCheck(Object parameter, Element element) {
        String titleToCheck = parameter.toString();

        LOG.info("Test to search [{}]", titleToCheck);

        boolean checkResult = getTestFixture().getTitle().equals(titleToCheck);

        if (checkResult) {
            LOG.info("Text [{}] found on page", titleToCheck);
            return buildSuccessResult();
        } else {
            return buildFailResult(titleToCheck, "");
        }
    }

    @Override
    public String getCommandName() {
        return "checkTitle";
    }
}
