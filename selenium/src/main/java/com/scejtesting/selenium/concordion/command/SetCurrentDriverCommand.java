package com.scejtesting.selenium.concordion.command;

/**
 * User: Fedorovaleks
 * Date: 3/31/14
 */
public class SetCurrentDriverCommand extends AbstractSeleniumDriverCommand {

    @Override
    protected void processDriverCommand(String driverName) {
        seleniumDriverManagerService.setCurrentDriver(driverName);
    }

    @Override
    public String getCommandType() {
        return "setCurrentDriver";
    }
}
