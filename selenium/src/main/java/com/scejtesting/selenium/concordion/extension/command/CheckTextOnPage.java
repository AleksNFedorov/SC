package com.scejtesting.selenium.concordion.extension.command;

import org.concordion.api.Element;
import org.concordion.api.listener.AssertFailureEvent;
import org.concordion.api.listener.AssertListener;
import org.concordion.api.listener.AssertSuccessEvent;

/**
 * Created by aleks on 5/4/14.
 */
public class CheckTextOnPage extends AbstractSeleniumDriverCommand {

    public CheckTextOnPage(AssertListener listener) {
        super(listener);
    }

    @Override
    protected void processDriverCommand(Object parameter, Element element) {

        String textToSearch = parameter.toString();

        LOG.info("Test to search [{}]", textToSearch);

        boolean checkResult = getTestFixture().checkTextOnPage(textToSearch);


        if (checkResult) {
            LOG.info("Text [{}] found on page", textToSearch);
            listeners.announce().successReported(new AssertSuccessEvent(element));
        } else {
            listeners.announce().failureReported(new AssertFailureEvent(element, textToSearch, "NONE"));
        }
    }

    @Override
    public String getCommandName() {
        return "checkTextOnPage";
    }
}
