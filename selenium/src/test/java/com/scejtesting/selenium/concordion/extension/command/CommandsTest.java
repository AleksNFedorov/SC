package com.scejtesting.selenium.concordion.extension.command;

import com.scejtesting.selenium.WebTestFixture;
import org.concordion.api.Element;
import org.concordion.api.listener.AssertFailureEvent;
import org.concordion.api.listener.AssertListener;
import org.concordion.api.listener.AssertSuccessEvent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.mockito.Mockito.*;

/**
 * User: Fedorovaleks
 * Date: 3/31/14
 */
public class CommandsTest {

    private MonitoringAssertListener listener;

    @Before
    public void init() {
        listener = new MonitoringAssertListener();
    }

    @After
    public void finishTest() {
        listener = null;
    }

    @Test
    public void checkElementSelected() {
        WebTestFixture fixture = mock(WebTestFixture.class);

        CheckElementSelected command = spy(new CheckElementSelected(listener));

        doReturn(fixture).when(command).getTestFixture();


        when(fixture.checkElementSelected(any(By.class))).thenReturn(Boolean.TRUE);
        command.processDriverCommand(By.id("someID"), new Element("div"));
        Assert.assertEquals(1, listener.getSuccessCount());


        when(fixture.checkElementSelected(any(WebElement.class))).thenReturn(Boolean.TRUE);
        command.processDriverCommand(mock(WebElement.class), new Element("div"));
        Assert.assertEquals(2, listener.getSuccessCount());


        when(fixture.checkElementSelected(any(WebElement.class))).thenReturn(Boolean.FALSE);
        command.processDriverCommand(mock(WebElement.class), new Element("div"));
        Assert.assertEquals(1, listener.getFailCount());


        try {
            command.processDriverCommand("someDriver", new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }

        Assert.assertEquals(2, listener.getSuccessCount());
        Assert.assertEquals(1, listener.getFailCount());


    }

    @Test
    public void checkTextOnPageCommandTest() {

        WebTestFixture fixture = mock(WebTestFixture.class);

        CheckTextOnPage command = spy(new CheckTextOnPage(listener));

        doReturn(fixture).when(command).getTestFixture();

        when(fixture.checkTextOnPage(anyString())).thenReturn(Boolean.TRUE);
        command.processDriverCommand("someDriver", new Element("div"));
        Assert.assertEquals(1, listener.getSuccessCount());


        when(fixture.checkTextOnPage(anyString())).thenReturn(Boolean.FALSE);
        command.processDriverCommand("someDriver", new Element("div"));
        Assert.assertEquals(1, listener.getFailCount());

        when(fixture.checkTextOnPage(anyString())).thenThrow(RuntimeException.class);


        try {
            command.processDriverCommand("someDriver", new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }

        Assert.assertEquals(1, listener.getSuccessCount());
        Assert.assertEquals(1, listener.getFailCount());


    }

    @Test
    public void closeDriverCommand() {
        WebTestFixture fixture = mock(WebTestFixture.class);

        CloseDriver command = spy(new CloseDriver(listener));

        doReturn(fixture).when(command).getTestFixture();

        command.processDriverCommand("someDriver", new Element("div"));

        Assert.assertEquals(1, listener.getSuccessCount());

        doThrow(RuntimeException.class).when(fixture).closeCurrentDriver();

        try {
            command.processDriverCommand("someDriver", new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }

        Assert.assertEquals(1, listener.getSuccessCount());

    }

    @Test
    public void openDriverCommandTest() {

        WebTestFixture fixture = mock(WebTestFixture.class);

        OpenDriver command = spy(new OpenDriver(listener));

        doReturn(fixture).when(command).getTestFixture();

        command.processDriverCommand("someDriver", new Element("div"));

        Assert.assertEquals(1, listener.getSuccessCount());

        when(fixture.openDriver(anyString())).thenThrow(RuntimeException.class);

        try {
            command.processDriverCommand("someDriver", new Element("div"));
            Assert.fail();
        } catch (Exception ex) {
        }

        Assert.assertEquals(1, listener.getSuccessCount());


    }

    private class MonitoringAssertListener implements AssertListener {

        private int successCount;
        private int failCount;

        @Override
        public void successReported(AssertSuccessEvent event) {
            successCount++;
        }

        @Override
        public void failureReported(AssertFailureEvent event) {
            failCount++;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public int getFailCount() {
            return failCount;
        }
    }
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
