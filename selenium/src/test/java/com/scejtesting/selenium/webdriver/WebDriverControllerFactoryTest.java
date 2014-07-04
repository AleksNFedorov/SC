package com.scejtesting.selenium.webdriver;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by aleks on 29/3/14.
 */
public class WebDriverControllerFactoryTest {

    public static final String TEST_DRIVER_NAME = "fakedriver";

    @AfterClass
    public static void cleanTest() {

        System.clearProperty("fakedriver" + WebDriverControllerFactory.DRIVER_PROPERTIES_FILE_SUFFIX);

    }

    @Test
    public void loadDefaultPropertyTest() {

        WebDriverControllerFactory serviceFactory = new WebDriverControllerFactory();

        WebDriverController fakeDefaultService = serviceFactory.buildDriverService("fakedriver_default");

        Assert.assertNotNull(fakeDefaultService);

        Assert.assertEquals(FakeDefaultRemoteWebDriverService.class, fakeDefaultService.getClass());

    }

    @Test
    public void loadFromClassPathTest() {

        WebDriverControllerFactory serviceFactory = new WebDriverControllerFactory();

        WebDriverController fakeDefaultService = serviceFactory.buildDriverService("fakedriver");

        Assert.assertNotNull(fakeDefaultService);

        Assert.assertEquals(FakeClassPathRemoteWebDriverService.class, fakeDefaultService.getClass());

    }

    @Test
    public void loadFromEnvironmentTest() {

        System.setProperty("fakedriver" + WebDriverControllerFactory.DRIVER_PROPERTIES_FILE_SUFFIX, getClass().getClassLoader().getResource("fakedriver.environment.properties").getFile());

        WebDriverControllerFactory serviceFactory = new WebDriverControllerFactory();

        WebDriverController fakeDefaultService = serviceFactory.buildDriverService("fakedriver");

        Assert.assertNotNull(fakeDefaultService);

        Assert.assertEquals(FakeEnvironmentRemoteWebDriverService.class, fakeDefaultService.getClass());

    }

    @Test(expected = RuntimeException.class)
    public void unknownDriverTest() {
        new WebDriverControllerFactory().buildDriverService("fakedriver" + System.currentTimeMillis());
    }

    @Test(expected = RuntimeException.class)
    public void unknownBuilderClassTest() {

        System.setProperty("fakedriver" + WebDriverControllerFactory.DRIVER_PROPERTIES_FILE_SUFFIX, getClass().getClassLoader().getResource("fakedriver.epmty.properties").getFile());
        new WebDriverControllerFactory().buildDriverService("fakedriver");
    }

}
