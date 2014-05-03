package com.scejtesting.selenium;

import com.scejtesting.core.context.TestContextService;
import com.scejtesting.selenium.webdriver.FakeClassPathRemoteWebDriverService;
import com.scejtesting.selenium.webdriver.TestRemoteWebDriver;
import org.junit.Assert;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Properties;

/**
 * Created by aleks on 30/3/14.
 */
public class CoreWebTestFixtureTest extends CoreScejTest {


    @org.junit.Test
    public void positiveFlowTest() {

        CoreWebTestFixture driverManagerService = new CoreWebTestFixture();

        RemoteWebDriver webDriver = driverManagerService.openDriver("fakedriver");

        Assert.assertNotNull(webDriver);

        Assert.assertSame(TestRemoteWebDriver.class, webDriver.getClass());


        RemoteWebDriver webDriverByGet = driverManagerService.getCurrentDriver();

        Assert.assertSame(webDriver, webDriverByGet);

        driverManagerService.closeCurrentDriver();

        RemoteWebDriver unExistDriver = driverManagerService.getCurrentDriver();

        Assert.assertNull(unExistDriver);

    }


    @org.junit.Test(expected = RuntimeException.class)
    public void doubleBuildTest() {

        CoreWebTestFixture driverManagerService = new CoreWebTestFixture();

        driverManagerService.openDriver("fakedriver");
        driverManagerService.openDriver("fakedriver");

    }


    @org.junit.Test(expected = RuntimeException.class)
    public void unknownDriver() {

        CoreWebTestFixture test = new CoreWebTestFixture();

        test.openDriver("someUnknown driver" + System.currentTimeMillis());

    }


    @org.junit.Test(expected = RuntimeException.class)
    public void doubleQuitDriver() {

        CoreWebTestFixture driverManagerService = new CoreWebTestFixture();

        driverManagerService.openDriver("fakedriver");

        driverManagerService.closeCurrentDriver();
        driverManagerService.closeCurrentDriver();

    }


    @org.junit.Test(expected = IllegalStateException.class)
    public void noDriverAtDriverServer() {

        CoreWebTestFixture driverManagerService = new CoreWebTestFixture();

        driverManagerService.openDriver("fakedriver");

        new TestContextService().getCurrentTestContext().
                addAttribute(CoreWebTestFixture.SCEJ_DRIVER_SERVICE, new FakeClassPathRemoteWebDriverService(new Properties()));


        driverManagerService.getCurrentDriver();

    }

}
