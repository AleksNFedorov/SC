package com.scejtesting.selenium.concordion.command;

/**
 * User: Fedorovaleks
 * Date: 3/31/14
 */
public class OpenDriverCommandasdf extends AbstractSeleniumDriverCommand {

    @Override
    protected void processDriverCommand(String driverName) {
        seleniumDriverManagerService.openDriver(driverName);
    }

    @Override
    public String getCommandType() {
        return "openDriver";
    }
}
