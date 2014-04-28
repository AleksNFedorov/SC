package com.scejtesting.selenium;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContextService;
import com.scejtesting.selenium.webdriver.DriverType;
import com.scejtesting.selenium.webdriver.RemoteWebDriverBuilderServiceRegistry;
import com.scejtesting.selenium.webdriver.TestRemoteWebDriver;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.openqa.selenium.remote.RemoteWebDriver;

import static org.mockito.Mockito.*;

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
        new TestContextService().getCurrentTestContext().destroyCurrentSpecificationContext();
    }

    @org.junit.Test
    public void positiveFlowTest() {

        RemoteWebDriverBuilderServiceRegistry driverFactory = mock(RemoteWebDriverBuilderServiceRegistry.class);

        when(driverFactory.buildRemoteWebDriver(anyString())).thenReturn(new TestRemoteWebDriver(DriverType.Default));

        SeleniumDriverManagerService test = spy(new SeleniumDriverManagerService());

        doReturn(driverFactory).when(test).buildRemoteWebDriverRegistry();

        String driver1Name = "coretestdriver1";
        String driver2Name = "coretestdriver2";

        test.buildDriver(driver2Name);
        RemoteWebDriver driver = test.buildDriver(driver1Name);
        RemoteWebDriver currentDriver = test.getCurrentDriver();
        RemoteWebDriver driver1ByName = test.getDriver(driver1Name);
        RemoteWebDriver driver2ByName = test.getDriver(driver2Name);


        Assert.assertNotNull(driver);
        Assert.assertNotNull(currentDriver);
        Assert.assertNotNull(driver1ByName);
        Assert.assertNotNull(driver2ByName);

        Assert.assertEquals(driver, currentDriver);
        Assert.assertEquals(driver, driver1ByName);
        Assert.assertEquals(driver2ByName, test.getDriver(driver2Name));

        test.quitCurrentDriver();

        Assert.assertNull(test.getCurrentDriver());
        Assert.assertNull(test.getDriver(driver1Name));

        Assert.assertEquals(driver2ByName, test.getDriver(driver2Name));

        test.setCurrentDriver(driver2Name);

        Assert.assertEquals(driver2ByName, test.getCurrentDriver());

        test.quitCurrentDriver();

        Assert.assertNull(test.getCurrentDriver());
        Assert.assertNull(test.getDriver(driver2Name));
        Assert.assertNull(test.getDriver(driver1Name));


    }

    @org.junit.Test(expected = RuntimeException.class)
    public void doubleBuildTest() {

        RemoteWebDriverBuilderServiceRegistry driverFactory = mock(RemoteWebDriverBuilderServiceRegistry.class);

        when(driverFactory.buildRemoteWebDriver(anyString())).thenReturn(new TestRemoteWebDriver(DriverType.Default));

        SeleniumDriverManagerService test = spy(new SeleniumDriverManagerService());

        doReturn(driverFactory).when(test).buildRemoteWebDriverRegistry();

        String driver2Name = "coretestdriver2";

        test.buildDriver(driver2Name);
        test.buildDriver(driver2Name);

    }


    @org.junit.Test(expected = RuntimeException.class)
    public void unknownDriver() {

        SeleniumDriverManagerService test = new SeleniumDriverManagerService();

        test.buildDriver("someUnknown driver" + System.currentTimeMillis());

    }

    @org.junit.Test(expected = RuntimeException.class)
    public void incorrectCurrentDriver() {

        SeleniumDriverManagerService test = new SeleniumDriverManagerService();

        test.setCurrentDriver("someUnknown driver" + System.currentTimeMillis());
    }

    @org.junit.Test
    public void notRegisteredDriver() {

        SeleniumDriverManagerService test = new SeleniumDriverManagerService();

        Assert.assertNull(test.getCurrentDriver());
        Assert.assertNull(test.getDriver("someUnregisteredDriver"));
    }

    @org.junit.Test
    public void quitNonCurrentDriver() {

        RemoteWebDriverBuilderServiceRegistry driverFactory = mock(RemoteWebDriverBuilderServiceRegistry.class);

        when(driverFactory.buildRemoteWebDriver(anyString())).thenReturn(new TestRemoteWebDriver(DriverType.Default));

        SeleniumDriverManagerService test = spy(new SeleniumDriverManagerService());

        doReturn(driverFactory).when(test).buildRemoteWebDriverRegistry();

        String driver1Name = "coretestdriver1";
        String driver2Name = "coretestdriver2";

        test.buildDriver(driver2Name);
        RemoteWebDriver driver = test.buildDriver(driver1Name);
        RemoteWebDriver currentDriver = test.getCurrentDriver();
        RemoteWebDriver driver1ByName = test.getDriver(driver1Name);
        RemoteWebDriver driver2ByName = test.getDriver(driver2Name);


        Assert.assertNotNull(driver);
        Assert.assertNotNull(currentDriver);
        Assert.assertNotNull(driver1ByName);
        Assert.assertNotNull(driver2ByName);

        test.quitDriver(driver2Name);


        Assert.assertEquals(driver, currentDriver);
        Assert.assertEquals(driver, driver1ByName);
        Assert.assertNull(test.getDriver(driver2Name));


    }
}
