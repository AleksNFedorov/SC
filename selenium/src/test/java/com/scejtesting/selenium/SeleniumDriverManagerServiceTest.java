package com.scejtesting.selenium;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContextService;
import com.scejtesting.selenium.webdriver.FakeClassPathRemoteWebDriverService;
import com.scejtesting.selenium.webdriver.TestRemoteWebDriver;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Properties;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by aleks on 30/3/14.
 */
public class SeleniumDriverManagerServiceTest {


    @BeforeClass
    public static void init() {

        Test specTest = mock(Test.class);
        when(specTest.getSpecification()).thenReturn(new Specification());
        new TestContextService().createNewTestContext(specTest);

    }

    @AfterClass
    public static void finish() {
        new TestContextService().destroyTestContext();
    }

    @After
    public void cleanTest() {
        new TestContextService().getCurrentTestContext().
                cleanAttribute(SeleniumDriverManagerService.SCEJ_DRIVER_SERVICE);

    }

    @org.junit.Test
    public void positiveFlowTest() {

        SeleniumDriverManagerService driverManagerService = new SeleniumDriverManagerService();

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

        SeleniumDriverManagerService driverManagerService = new SeleniumDriverManagerService();

        driverManagerService.openDriver("fakedriver");
        driverManagerService.openDriver("fakedriver");

    }


    @org.junit.Test(expected = RuntimeException.class)
    public void unknownDriver() {

        SeleniumDriverManagerService test = new SeleniumDriverManagerService();

        test.openDriver("someUnknown driver" + System.currentTimeMillis());

    }


    @org.junit.Test(expected = RuntimeException.class)
    public void doubleQuitDriver() {

        SeleniumDriverManagerService driverManagerService = new SeleniumDriverManagerService();

        driverManagerService.openDriver("fakedriver");

        driverManagerService.closeCurrentDriver();
        driverManagerService.closeCurrentDriver();

    }


    @org.junit.Test(expected = IllegalStateException.class)
    public void noDriverAtDriverServer() {

        SeleniumDriverManagerService driverManagerService = new SeleniumDriverManagerService();

        driverManagerService.openDriver("fakedriver");

        new TestContextService().getCurrentTestContext().
                addAttribute(SeleniumDriverManagerService.SCEJ_DRIVER_SERVICE, new FakeClassPathRemoteWebDriverService(new Properties()));


        driverManagerService.getCurrentDriver();

    }

}
