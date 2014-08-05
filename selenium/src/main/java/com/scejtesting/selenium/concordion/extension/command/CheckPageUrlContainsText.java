package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.Element;
import org.concordion.api.listener.AssertListener;

/**
 * Created by aleks on 8/3/14.
 */
public class CheckPageUrlContainsText extends AbstractSeleniumCheckCommand {

    public CheckPageUrlContainsText(AssertListener listener) {
        super(listener);
    }

    @Override
    public CommandResult performCheck(Object parameter, Element element) {
        String urlToCheck = parameter.toString();

        LOG.info("Url to check [{}]", urlToCheck);

        String currentUrl = getTestFixture().getCurrentURL();

        LOG.info("Current window title is [{}]", currentUrl);

        boolean checkResult = currentUrl.contains(urlToCheck);

        if (checkResult) {
            LOG.info("Url contains [{}]", urlToCheck);
            return buildSuccessResult();
        } else {
            return buildFailResult(urlToCheck, currentUrl);
        }
    }

    @Override
    public String getCommandName() {
        return "checkUrlContains";
    }
}
