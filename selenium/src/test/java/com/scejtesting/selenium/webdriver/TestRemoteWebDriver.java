package com.scejtesting.selenium.webdriver;

import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Properties;

/**
 * Created by aleks on 30/3/14.
 */
public class TestRemoteWebDriver extends RemoteWebDriver {

    private final DriverType type;

    private int getCapabilitiesInvokedTimes;
    private int buildDriverInvokedTimes;

    private Properties properties;


    public TestRemoteWebDriver(DriverType type) {
        this.type = type;
    }

    public DriverType getType() {
        return type;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public int getGetCapabilitiesInvokedTimes() {
        return getCapabilitiesInvokedTimes;
    }

    public void onGetCapabilitiesInvoked() {
        this.getCapabilitiesInvokedTimes++;
    }

    public int getBuildDriverInvokedTimes() {
        return buildDriverInvokedTimes;
    }

    public void onBuildDriverInvoked() {
        this.buildDriverInvokedTimes++;
    }
}
