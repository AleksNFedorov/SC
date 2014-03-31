package com.scejtesting.selenium.concordion.command;

/**
 * User: Fedorovaleks
 * Date: 3/31/14
 */
public class QuiteDriverCommand extends AbstractSeleniumDriverCommand {


    @Override
    protected void processDriverCommand(String driverName) {
        seleniumHelper.quitDriver(driverName);
    }

    @Override
    public String getCommandType() {
        return "quiteDriver";
    }
}
