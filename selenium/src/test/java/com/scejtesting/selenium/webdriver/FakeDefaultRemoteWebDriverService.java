package com.scejtesting.selenium.webdriver;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Properties;

/**
 * Created by aleks on 30/3/14.
 */
public class FakeDefaultRemoteWebDriverService extends ScejDriverService {


    private final TestRemoteWebDriver remoteWebDriver = new TestRemoteWebDriver();

    protected FakeDefaultRemoteWebDriverService(Properties driverProperties) {
        super(driverProperties);
    }


    @Override
    protected DesiredCapabilities getDriverSpecificCapabilities() {
        return new DesiredCapabilities();
    }

    @Override
    public RemoteWebDriver buildDriver(DesiredCapabilities capabilities) {
        return remoteWebDriver;
    }

    @Override
    protected void onDriverClose() {

    }

}
