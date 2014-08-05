package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.Element;
import org.concordion.api.listener.AssertListener;

/**
 * Created by aleks on 8/3/14.
 */
public class CheckPageTitleContainsText extends AbstractSeleniumCheckCommand {

    public CheckPageTitleContainsText(AssertListener listener) {
        super(listener);
    }

    @Override
    public CommandResult performCheck(Object parameter, Element element) {
        String titleToCheck = parameter.toString();

        LOG.info("Title to check [{}]", titleToCheck);

        String currentTitle = getTestFixture().getTitle();

        LOG.info("Current window title is [{}]", currentTitle);

        boolean checkResult = currentTitle.contains(titleToCheck);

        if (checkResult) {
            LOG.info("Page title contains [{}]", titleToCheck);
            return buildSuccessResult();
        } else {
            return buildFailResult(titleToCheck, currentTitle);
        }
    }

    @Override
    public String getCommandName() {
        return "checkTitleContains";
    }
}
