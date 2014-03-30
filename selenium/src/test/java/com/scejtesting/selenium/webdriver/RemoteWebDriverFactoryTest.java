package com.scejtesting.selenium.webdriver;


import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created by aleks on 30/3/14.
 */
public class RemoteWebDriverFactoryTest {

    @Test(expected = RuntimeException.class)
    public void unknownDriverTest() {

        RemoteWebDriverFactory factory = new RemoteWebDriverFactory();

        factory.buildRemoteWebDriver("someUnknownDriver" + System.currentTimeMillis());

    }


    @Test
    public void positiveFlowTest() {

        System.setProperty("p1", "pv1");

        RemoteWebDriverFactory factory = new RemoteWebDriverFactory();

        TestRemoteWebDriver remoteDriver = (TestRemoteWebDriver) factory.buildRemoteWebDriver("fakedriver");

        Assert.assertEquals(DriverType.FromClassPath, remoteDriver.getType());


        Assert.assertEquals(1, remoteDriver.getBuildDriverInvokedTimes());
        Assert.assertEquals(0, remoteDriver.getGetCapabilitiesInvokedTimes());

        Assert.assertEquals("pv1", remoteDriver.getProperties().getProperty("p1"));
    }

    @Test
    public void checkSingleLoad() {


        RemoteWebDriverBuilderService driverBuilderService = mock(RemoteWebDriverBuilderService.class);
        when(driverBuilderService.getRemoteWebDriver()).thenReturn(new TestRemoteWebDriver(DriverType.Default));

        RemoteWebDriverFactory factory = spy(new RemoteWebDriverFactory());

        doReturn(driverBuilderService).when(factory).createDriverBuilderService(anyString());

        factory.buildRemoteWebDriver("someDriver");
        TestRemoteWebDriver remoteWebDriver = (TestRemoteWebDriver) factory.buildRemoteWebDriver("someDriver");

        Assert.assertEquals(DriverType.Default, remoteWebDriver.getType());

        verify(factory, times(1)).createDriverBuilderService(anyString());


    }
}
