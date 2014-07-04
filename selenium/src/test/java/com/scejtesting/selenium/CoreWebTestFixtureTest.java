package com.scejtesting.selenium;

import com.scejtesting.core.context.TestContextService;
import com.scejtesting.selenium.webdriver.FakeClassPathRemoteWebDriverService;
import com.scejtesting.selenium.webdriver.TestRemoteWebDriver;
import com.scejtesting.selenium.webdriver.WebDriverController;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by aleks on 30/3/14.
 */
public class CoreWebTestFixtureTest extends CoreScejTest<CoreWebTestFixture> {

    @Ignore //Does not work with html unit driver
    @Test
    public void getCurrentURL() {
        Assert.assertEquals(operateURL.toString(), currentTestFixture.getCurrentURL());
    }

    @Test
    public void getTitleTest() {

        String pageTitle = currentTestFixture.getTitle();

        assertNotNull(pageTitle);

        assertEquals("WebTestFixtureTest", pageTitle);

    }

    @Test
    public void positiveFlowTest() {

        currentTestFixture.closeCurrentDriver();

        RemoteWebDriver webDriver = currentTestFixture.openDriver("fakedriver");

        Assert.assertNotNull(webDriver);

        Assert.assertSame(TestRemoteWebDriver.class, webDriver.getClass());

        RemoteWebDriver webDriverByGet = currentTestFixture.getCurrentDriver();

        Assert.assertSame(webDriver, webDriverByGet);

        currentTestFixture.closeCurrentDriver();

        RemoteWebDriver unExistDriver = currentTestFixture.getCurrentDriver();

        Assert.assertNull(unExistDriver);

        //work around bacause of test initialization in parent
        currentTestFixture.openDriver("fakedriver");

    }

    @org.junit.Test(expected = RuntimeException.class)
    public void doubleBuildTest() {

        currentTestFixture.openDriver("fakedriver");

    }

    @org.junit.Test(expected = RuntimeException.class)
    public void unknownDriver() {

        CoreWebTestFixture test = new CoreWebTestFixture();

        test.openDriver("someUnknown driver" + System.currentTimeMillis());

    }

    @org.junit.Test(expected = RuntimeException.class)
    public void doubleQuitDriver() {

        currentTestFixture = new CoreWebTestFixture();

        currentTestFixture.openDriver("fakedriver");

        currentTestFixture.closeCurrentDriver();
        currentTestFixture.closeCurrentDriver();

    }

    @org.junit.Test
    public void noDriverAtDriverServer() {

        currentTestFixture.closeCurrentDriver();

        currentTestFixture.openDriver("fakedriver");

        WebDriverController currentController = new TestContextService().getCurrentTestContext().getAttribute(DriverHolderService.SCEJ_DRIVER_SERVICE);

        new TestContextService().getCurrentTestContext().
                addAttribute(DriverHolderService.SCEJ_DRIVER_SERVICE, new FakeClassPathRemoteWebDriverService(new Properties()));

        try {
            currentTestFixture.getCurrentDriver();
            Assert.fail("No driver available exception expected");
        } catch (RuntimeException ex) {

        }

        new TestContextService().getCurrentTestContext().addAttribute(DriverHolderService.SCEJ_DRIVER_SERVICE,
                currentController);

    }

    @Override
    protected CoreWebTestFixture buildTestFixture() {
        return new CoreWebTestFixture();
    }
}
