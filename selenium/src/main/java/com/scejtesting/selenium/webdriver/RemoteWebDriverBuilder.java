package com.scejtesting.selenium.webdriver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by aleks on 27/3/14.
 */
public abstract class RemoteWebDriverBuilder {

    protected abstract DesiredCapabilities getDriverSpecificCapabilities();

    public abstract RemoteWebDriver buildDriver(Properties properties);


    protected Capabilities buildFromProperties(Properties properties) {
        DesiredCapabilities driverCapabilities = getDriverSpecificCapabilities();
        driverCapabilities.merge(new DesiredCapabilities(new HashMap<String, Object>((Map) properties)));

        return driverCapabilities;
    }


    public void onFinish() {

    }
}
