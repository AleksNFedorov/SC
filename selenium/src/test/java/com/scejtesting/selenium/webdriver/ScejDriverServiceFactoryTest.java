package com.scejtesting.selenium.webdriver;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by aleks on 29/3/14.
 */
public class ScejDriverServiceFactoryTest {

    public static final String TEST_DRIVER_NAME = "fakedriver";

    @AfterClass
    public static void cleanTest() {

        System.clearProperty("fakedriver" + ScejDriverServiceFactory.DRIVER_PROPERTIES_FILE_SUFFIX);

    }

    @Test
    public void loadDefaultPropertyTest() {


        ScejDriverServiceFactory serviceFactory = new ScejDriverServiceFactory();

        ScejDriverService fakeDefaultService = serviceFactory.buildDriverService("fakedriver_default");

        Assert.assertNotNull(fakeDefaultService);

        Assert.assertEquals(FakeDefaultRemoteWebDriverService.class, fakeDefaultService.getClass());

    }

    @Test
    public void loadFromClassPathTest() {

        ScejDriverServiceFactory serviceFactory = new ScejDriverServiceFactory();

        ScejDriverService fakeDefaultService = serviceFactory.buildDriverService("fakedriver");

        Assert.assertNotNull(fakeDefaultService);

        Assert.assertEquals(FakeClassPathRemoteWebDriverService.class, fakeDefaultService.getClass());

    }

    @Test
    public void loadFromEnvironmentTest() {

        System.setProperty("fakedriver" + ScejDriverServiceFactory.DRIVER_PROPERTIES_FILE_SUFFIX, getClass().getClassLoader().getResource("fakedriver.environment.properties").getFile());

        ScejDriverServiceFactory serviceFactory = new ScejDriverServiceFactory();

        ScejDriverService fakeDefaultService = serviceFactory.buildDriverService("fakedriver");

        Assert.assertNotNull(fakeDefaultService);

        Assert.assertEquals(FakeEnvironmentRemoteWebDriverService.class, fakeDefaultService.getClass());

    }

    @Test(expected = RuntimeException.class)
    public void unknownDriverTest() {
        new ScejDriverServiceFactory().buildDriverService("fakedriver" + System.currentTimeMillis());
    }

    @Test(expected = RuntimeException.class)
    public void unknownBuilderClassTest() {

        System.setProperty("fakedriver" + ScejDriverServiceFactory.DRIVER_PROPERTIES_FILE_SUFFIX, getClass().getClassLoader().getResource("fakedriver.epmty.properties").getFile());
        new ScejDriverServiceFactory().buildDriverService("fakedriver");
    }

}
