package com.scejtesting.selenium.webdriver;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Created by aleks on 29/3/14.
 */
public class RemoteWebDriverBuilderServiceTest {

    public static final String TEST_DRIVER_NAME = "fakedriver";

    @Test
    public void loadDefaultPropertyTest() {


        RemoteWebDriverBuilderService builderService = new RemoteWebDriverBuilderService("fakedriver_default");
        builderService.init();


        RemoteWebDriver driver = builderService.getRemoteWebDriver();

        Assert.assertEquals(TestRemoteWebDriver.class, driver.getClass());

        TestRemoteWebDriver testRemoteWebDriver = (TestRemoteWebDriver) driver;

        Assert.assertEquals(DriverType.Default, testRemoteWebDriver.getType());

    }


    @Test
    public void loadFromClassPathTest() {
        RemoteWebDriverBuilderService builderService = new RemoteWebDriverBuilderService("fakedriver");
        builderService.init();


        RemoteWebDriver driver = builderService.getRemoteWebDriver();

        Assert.assertEquals(TestRemoteWebDriver.class, driver.getClass());

        TestRemoteWebDriver testRemoteWebDriver = (TestRemoteWebDriver) driver;

        Assert.assertEquals(DriverType.FromClassPath, testRemoteWebDriver.getType());

    }

    @Test
    public void loadFromEnvironmentTest() {

        System.setProperty("fakedriver" + RemoteWebDriverBuilderService.DRIVER_PROPERTIES_FILE_SUFFIX, getClass().getClassLoader().getResource("fakedriver.environment.properties").getFile());

        RemoteWebDriverBuilderService builderService = new RemoteWebDriverBuilderService("fakedriver");
        builderService.init();


        RemoteWebDriver driver = builderService.getRemoteWebDriver();

        Assert.assertEquals(TestRemoteWebDriver.class, driver.getClass());

        TestRemoteWebDriver testRemoteWebDriver = (TestRemoteWebDriver) driver;

        Assert.assertEquals(DriverType.FromEnvironment, testRemoteWebDriver.getType());

    }

    @Test(expected = RuntimeException.class)
    public void unknownDriverTest() {
        RemoteWebDriverBuilderService builderService = new RemoteWebDriverBuilderService("fakedriver" + System.currentTimeMillis());
        builderService.init();
    }

    @Test(expected = RuntimeException.class)
    public void unknownBuilderClassTest() {

        System.setProperty("fakedriver" + RemoteWebDriverBuilderService.DRIVER_PROPERTIES_FILE_SUFFIX, getClass().getClassLoader().getResource("fakedriver.epmty.properties").getFile());

        RemoteWebDriverBuilderService builderService = new RemoteWebDriverBuilderService("fakedriver");
        builderService.init();
    }

    @AfterClass
    public static void cleanTest() {

        System.clearProperty("fakedriver" + RemoteWebDriverBuilderService.DRIVER_PROPERTIES_FILE_SUFFIX);

    }

}
