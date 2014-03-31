package com.scejtesting.selenium;

import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Created by aleks on 27/3/14.
 */
public class SeleniumCoreTest {

    private final SeleniumDriverManagerService driverManagerService = buildDriverManagerService();


    public final RemoteWebDriver buildDriver(String driverName) {
        return driverManagerService.buildDriver(driverName);
    }

    public RemoteWebDriver getCurrentDriver() {
        return driverManagerService.getCurrentDriver();
    }

    public boolean setCurrentDriver(String driverName) {
        return driverManagerService.setCurrentDriver(driverName);
    }

    public RemoteWebDriver getDriver(String driverName) {
        return driverManagerService.getDriver(driverName);
    }

    public boolean quitCurrentDriver() {
        return driverManagerService.quitCurrentDriver();
    }

    public boolean quitDriver(String driverName) {
        return driverManagerService.quitDriver(driverName);
    }

    protected SeleniumDriverManagerService buildDriverManagerService() {
        return new SeleniumDriverManagerService();
    }

}
