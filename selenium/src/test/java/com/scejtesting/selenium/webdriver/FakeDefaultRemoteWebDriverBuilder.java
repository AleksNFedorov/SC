package com.scejtesting.selenium.webdriver;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Properties;

/**
 * Created by aleks on 30/3/14.
 */
public class FakeDefaultRemoteWebDriverBuilder extends RemoteWebDriverBuilder {


    private final TestRemoteWebDriver remoteWebDriver = new TestRemoteWebDriver(getDriverType());

    protected DriverType getDriverType() {
        return DriverType.Default;
    }


    @Override
    protected DesiredCapabilities getDriverSpecificCapabilities() {
        remoteWebDriver.onGetCapabilitiesInvoked();
        return new DesiredCapabilities();
    }

    @Override
    public RemoteWebDriver buildDriver(Properties properties) {
        remoteWebDriver.setProperties(properties);
        remoteWebDriver.onBuildDriverInvoked();
        return remoteWebDriver;
    }

}
