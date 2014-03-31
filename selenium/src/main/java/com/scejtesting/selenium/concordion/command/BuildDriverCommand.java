package com.scejtesting.selenium.concordion.command;

/**
 * User: Fedorovaleks
 * Date: 3/31/14
 */
public class BuildDriverCommand extends AbstractSeleniumDriverCommand {

    @Override
    protected void processDriverCommand(String driverName) {
        seleniumHelper.buildDriver(driverName);
    }

    @Override
    public String getCommandType() {
        return "buildDriver";
    }
}
