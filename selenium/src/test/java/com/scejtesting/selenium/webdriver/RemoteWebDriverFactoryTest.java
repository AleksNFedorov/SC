package com.scejtesting.selenium.webdriver;


/**
 * Created by aleks on 30/3/14.
 */
public class RemoteWebDriverFactoryTest {

//    @Test(expected = RuntimeException.class)
//    public void unknownDriverTest() {
//
//        RemoteWebDriverBuilderServiceRegistry factory = new RemoteWebDriverBuilderServiceRegistry();
//
//        factory.buildRemoteWebDriver("someUnknownDriver" + System.currentTimeMillis());
//
//    }
//
//    @Test
//    public void incorrectDriverName() {
//
//        RemoteWebDriverBuilderServiceRegistry factory = new RemoteWebDriverBuilderServiceRegistry();
//
//        try {
//            factory.buildRemoteWebDriver("#incorrectDrvierName");
//            Assert.fail("Incorrect driver name exception expected");
//        } catch (RuntimeException ex) {
//
//        }
//
//        try {
//            factory.buildRemoteWebDriver(null);
//            Assert.fail("Incorrect driver name exception expected");
//        } catch (RuntimeException ex) {
//
//        }
//
//        try {
//            factory.buildRemoteWebDriver("");
//            Assert.fail("Incorrect driver name exception expected");
//        } catch (RuntimeException ex) {
//
//        }
//
//    }
//
//
//    @Test
//    public void positiveFlowTest() {
//
//        System.setProperty("p1", "pv1");
//
//        RemoteWebDriverBuilderServiceRegistry factory = new RemoteWebDriverBuilderServiceRegistry();
//
//        TestRemoteWebDriver remoteDriver = (TestRemoteWebDriver) factory.buildRemoteWebDriver("fakedriver");
//
//        Assert.assertEquals(DriverType.FromClassPath, remoteDriver.getType());
//
//
//        Assert.assertEquals(1, remoteDriver.getBuildDriverInvokedTimes());
//        Assert.assertEquals(0, remoteDriver.getGetCapabilitiesInvokedTimes());
//
//        Assert.assertEquals("pv1", remoteDriver.getProperties().getProperty("p1"));
//    }
//
//    @Test
//    public void checkSingleLoad() {
//
//
//        ScejDriverServiceFactory driverBuilderService = mock(ScejDriverServiceFactory.class);
//        when(driverBuilderService.getRemoteWebDriver()).thenReturn(new TestRemoteWebDriver(DriverType.Default));
//
//        RemoteWebDriverBuilderServiceRegistry factory = spy(new RemoteWebDriverBuilderServiceRegistry());
//
//        doReturn(driverBuilderService).when(factory).createDriverBuilderService(anyString());
//
//        factory.buildRemoteWebDriver("someDriver");
//        TestRemoteWebDriver remoteWebDriver = (TestRemoteWebDriver) factory.buildRemoteWebDriver("someDriver");
//
//        Assert.assertEquals(DriverType.Default, remoteWebDriver.getType());
//
//        verify(factory, times(1)).createDriverBuilderService(anyString());
//
//
//    }
}
