package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.Element;
import org.concordion.api.listener.AssertListener;

/**
 * Created by aleks on 5/4/14.
 */
public class CheckTextOnPage extends AbstractSeleniumCheckCommand {

    public CheckTextOnPage(AssertListener listener) {
        super(listener);
    }

    @Override
    public CommandResult performCheck(Object parameter, Element element) {

        String textToSearch = parameter.toString();

        LOG.info("Test to search [{}]", textToSearch);

        boolean checkResult = getTestFixture().checkTextOnPage(textToSearch);

        if (checkResult) {
            LOG.info("Text [{}] found on page", textToSearch);
            return buildSuccessResult();
        } else {
            return buildFailResult(textToSearch, "");
        }
    }

    @Override
    public String getCommandName() {
        return "checkTextOnPage";
    }
}
