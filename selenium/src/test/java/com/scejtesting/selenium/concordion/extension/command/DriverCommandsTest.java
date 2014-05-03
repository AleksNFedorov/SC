package com.scejtesting.selenium.concordion.extension.command;

/**
 * User: Fedorovaleks
 * Date: 3/31/14
 */
public class DriverCommandsTest {
//
//    public static final String TEST_DRIVER = "fakedriver";
//    public static final String TEST_DRIVER_2 = "fakedriver_default";
//
//    @Before
//    public void init() {
//
//        com.scejtesting.core.config.Test specTest = mock(com.scejtesting.core.config.Test.class);
//        when(specTest.getSpecification()).thenReturn(new Specification());
//        new TestContextService().createNewTestContext(specTest);
//
//    }
//
//    @After
//    public void finish() {
//        new TestContextService().getCurrentTestContext().destroyCurrentSpecificationContext();
//    }
//
//    @Test
//    public void buildDriverTest() {
//
//        CommandCall commandCall = mock(CommandCall.class);
//
//
//        when(commandCall.hasChildCommands()).thenReturn(Boolean.FALSE);
//        when(commandCall.getExpression()).thenReturn(TEST_DRIVER);
//
//        CreateAndOpenDriverCommand buildDriverCommand = new CreateAndOpenDriverCommand();
//
//        buildDriverCommand.setUp(commandCall, null, null);
//
//        SeleniumDriverManagerService helper = new SeleniumDriverManagerService();
//
//        TestRemoteWebDriver driverByName = (TestRemoteWebDriver) helper.getDriver(TEST_DRIVER);
//        TestRemoteWebDriver currentDriver = (TestRemoteWebDriver) helper.getCurrentDriver();
//
//        Assert.assertNotNull(driverByName);
//        Assert.assertEquals(currentDriver, driverByName);
//
//        Assert.assertEquals(DriverType.FromClassPath, driverByName.getType());
//
//    }
//
//    @Test
//    public void quiteDriverTest() {
//
//        CommandCall commandCall = mock(CommandCall.class);
//
//        when(commandCall.hasChildCommands()).thenReturn(Boolean.FALSE);
//        when(commandCall.getExpression()).thenReturn(TEST_DRIVER);
//
//        SeleniumDriverManagerService helper = new SeleniumDriverManagerService();
//
//        helper.buildDriver(TEST_DRIVER);
//
//        Assert.assertNotNull(helper.getCurrentDriver());
//
//        AbstractSeleniumDriverCommand command = new QuiteDriverCommand();
//
//        command.setUp(commandCall, null, null);
//
//
//        TestRemoteWebDriver driverByName = (TestRemoteWebDriver) helper.getDriver(TEST_DRIVER);
//        TestRemoteWebDriver currentDriver = (TestRemoteWebDriver) helper.getCurrentDriver();
//
//        Assert.assertNull(driverByName);
//        Assert.assertNull(currentDriver);
//
//    }
//
//    @Test
//    public void closeCurrentDriver() {
//        CommandCall commandCall = mock(CommandCall.class);
//
//        when(commandCall.hasChildCommands()).thenReturn(Boolean.FALSE);
//        when(commandCall.getExpression()).thenReturn(TEST_DRIVER);
//
//        SeleniumDriverManagerService helper = new SeleniumDriverManagerService();
//
//        helper.buildDriver(TEST_DRIVER);
//
//        Assert.assertNotNull(helper.getCurrentDriver());
//
//        AbstractSeleniumDriverCommand command = new CloseDriverCommand();
//
//        command.setUp(commandCall, null, null);
//
//
//        TestRemoteWebDriver driverByName = (TestRemoteWebDriver) helper.getDriver(TEST_DRIVER);
//        TestRemoteWebDriver currentDriver = (TestRemoteWebDriver) helper.getCurrentDriver();
//
//        Assert.assertNull(driverByName);
//        Assert.assertNull(currentDriver);
//
//    }
//
//    @Test
//    public void setCurrentDriver() {
//        CommandCall commandCall = mock(CommandCall.class);
//
//        when(commandCall.hasChildCommands()).thenReturn(Boolean.FALSE);
//        when(commandCall.getExpression()).thenReturn(TEST_DRIVER_2);
//
//        SeleniumDriverManagerService helper = new SeleniumDriverManagerService();
//
//        helper.buildDriver(TEST_DRIVER_2);
//        helper.buildDriver(TEST_DRIVER);
//
//        Assert.assertNotNull(helper.getCurrentDriver());
//
//        helper.quitDriver(TEST_DRIVER);
//
//        new SetCurrentDriverCommand().setUp(commandCall, null, null);
//
//
//        TestRemoteWebDriver driverByName = (TestRemoteWebDriver) helper.getDriver(TEST_DRIVER_2);
//        TestRemoteWebDriver currentDriver = (TestRemoteWebDriver) helper.getCurrentDriver();
//
//        Assert.assertNotNull(driverByName);
//        Assert.assertEquals(currentDriver, driverByName);
//
//        Assert.assertEquals(DriverType.Default, driverByName.getType());
//
//        Assert.assertNull(helper.getDriver(TEST_DRIVER));
//
//    }


}
