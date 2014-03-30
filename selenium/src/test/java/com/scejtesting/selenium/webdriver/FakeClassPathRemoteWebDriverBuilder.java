package com.scejtesting.selenium.webdriver;

/**
 * Created by aleks on 30/3/14.
 */
public class FakeClassPathRemoteWebDriverBuilder extends FakeDefaultRemoteWebDriverBuilder {

    @Override
    protected DriverType getDriverType() {
        return DriverType.FromClassPath;
    }
}
