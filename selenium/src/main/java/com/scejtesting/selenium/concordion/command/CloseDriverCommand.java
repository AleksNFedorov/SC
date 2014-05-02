package com.scejtesting.selenium.concordion.command;

/**
 * User: Fedorovaleks
 * Date: 3/31/14
 */
public class CloseDriverCommand extends AbstractSeleniumDriverCommand {

    @Override
    protected void processDriverCommand(String driverName) {
        seleniumDriverManagerService.closeCurrentDriver();
    }

    @Override
    public String getCommandType() {
        return "closeDriver";
    }
}
