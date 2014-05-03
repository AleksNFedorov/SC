package com.scejtesting.selenium.concordion.extension.command;

/**
 * User: Fedorovaleks
 * Date: 3/31/14
 */
public class CreateAndOpenDriverCommand extends AbstractSeleniumDriverCommand {

    @Override
    protected void processDriverCommand(String driverName) {
        seleniumDriverManagerService.openDriver(driverName);
    }

    @Override
    public String getCommandType() {
        return "openDriver";
    }
}
