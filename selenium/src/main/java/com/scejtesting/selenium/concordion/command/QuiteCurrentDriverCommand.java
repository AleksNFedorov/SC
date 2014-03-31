package com.scejtesting.selenium.concordion.command;

/**
 * User: Fedorovaleks
 * Date: 3/31/14
 */
public class QuiteCurrentDriverCommand extends QuiteDriverCommand {

    @Override
    protected void processDriverCommand(String driverName) {
        seleniumHelper.quitCurrentDriver();
    }
}
