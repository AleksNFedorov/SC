package com.scejtesting.selenium;

import com.scejtesting.core.context.TestContextService;
import com.scejtesting.selenium.elements.WebElementWithAllAttributes;
import com.scejtesting.selenium.webdriver.FakeClassPathRemoteWebDriverService;
import com.scejtesting.selenium.webdriver.TestRemoteWebDriver;
import com.scejtesting.selenium.webdriver.WebDriverController;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * Created by aleks on 30/3/14.
 */
public class CoreWebTestFixtureTest extends CoreScejTest<CoreWebTestFixture> {


    @Test(expected = RuntimeException.class)
    public void testException_nullUrlForGoToUrl() {
        currentTestFixture.goToURL(null);
    }

    @Test(expected = RuntimeException.class)
    public void testWentToURLSuccess_ValidURL() {
        currentTestFixture.goToURL(OPERATE_URL2);
        Assert.assertEquals(OPERATE_URL2, currentTestFixture.getCurrentURL());
    }

    @Test
    public void testTitleAcquired_driverPointedToURL() {

        currentTestFixture.goToURL(OPERATE_URL2);
        String pageTitle = currentTestFixture.getTitle();
        assertNotNull(pageTitle);
        assertEquals(OPERATE_URL2_TITLE, pageTitle);
    }

    @Test
    public void testExceptionsOnFindInParent_nullParameters() {

        By testBy = currentTestFixture.createByClassName("fakeClass");
        WebElement testElement = mock(WebElement.class);

        try {
            currentTestFixture.findElementInParent((By) null, testBy);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.findElementInParent((WebElement) null, testBy);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.findElementInParent(testElement, null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.findElementInParent(testBy, null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionOnFindInParentWithBy_parentDoesNotExist() {
        By parentIdBy = currentTestFixture.createById("" + System.currentTimeMillis());
        currentTestFixture.findElementInParent(parentIdBy, parentIdBy);
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionOnFindInParent_withBy_childDoesNotExist() {
        By parentIdBy = currentTestFixture.createById("mainDiv");
        By childIdBy = currentTestFixture.createById("" + System.currentTimeMillis());
        currentTestFixture.findElementInParent(parentIdBy, childIdBy);
    }

    @Test
    public void testElementFoundOnFindWithParent_withBy_childExist() {
        By parentIdBy = currentTestFixture.createById("mainDiv");
        By childIdBy = currentTestFixture.createById("text");
        WebElementWithAllAttributes element = currentTestFixture.findElementInParent(parentIdBy, childIdBy);

        Assert.assertEquals("input", element.getTagName());
        Assert.assertEquals("text", element.getId());
    }

    @Test
    public void testElementFoundOnFindInParent_withWebElement_childExist() {
        By parentIdBy = currentTestFixture.createById("mainDiv");
        WebElement parentElement = currentTestFixture.findElement(parentIdBy);

        By childIdBy = currentTestFixture.createById("text");
        WebElementWithAllAttributes element = currentTestFixture.findElementInParent(parentElement, childIdBy);

        Assert.assertEquals("input", element.getTagName());
        Assert.assertEquals("text", element.getId());
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionOnFindElement_nullParameter() {
        currentTestFixture.findElement(null);
    }

    @Test
    public void testElementFoundOnFindElement_elementExist() {
        By parentIdBy = currentTestFixture.createById("mainDiv");
        WebElementWithAllAttributes element = currentTestFixture.findElement(parentIdBy);

        Assert.assertEquals("div", element.getTagName());
        Assert.assertEquals("mainDiv", element.getId());
    }

    @Test(expected = RuntimeException.class)
    public void testExceptionOnFindElement_elementDoesNotExist() {
        By childIdBy = currentTestFixture.createById("" + System.currentTimeMillis());
        currentTestFixture.findElement(childIdBy);
    }


    @Test(expected = RuntimeException.class)
    public void testExceptionFindElement_nullParameter() {
        currentTestFixture.findElements(null);
    }


    @Test
    public void testException_findElements_elementsDoNotExist() {
        By parentIdBy = currentTestFixture.createById("mainDiv1" + System.currentTimeMillis());
        List<WebElementWithAllAttributes> elements = currentTestFixture.findElements(parentIdBy);
        Assert.assertTrue(elements.isEmpty());
    }

    @Test
    public void testElementsFoundFindElements_elementsExist() {
        By elementsBy = currentTestFixture.createByXpath("//div");
        List<WebElementWithAllAttributes> elements = currentTestFixture.findElements(elementsBy);

        Assert.assertEquals(2, elements.size());

        Assert.assertEquals("div", elements.get(0).getTagName());
        Assert.assertEquals("div", elements.get(1).getTagName());
    }

    @Test
    public void testException_findElementBy_nullParameter() {

        try {
            currentTestFixture.findElementByClassName(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.findElementByCssSelector(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }
        try {
            currentTestFixture.findElementById(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }
        try {
            currentTestFixture.findElementByLinkText(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }
        try {
            currentTestFixture.findElementByName(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }
        try {
            currentTestFixture.findElementByPartialLinkText(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }
        try {
            currentTestFixture.findElementByTagName(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }
        try {
            currentTestFixture.findElementByXpath(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }
    }

    @Test
    public void testException_createBy_nullParameter() {

        try {
            currentTestFixture.createByClassName(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.createByCssSelector(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }
        try {
            currentTestFixture.createById(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }
        try {
            currentTestFixture.createByLinkText(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }
        try {
            currentTestFixture.createByName(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }
        try {
            currentTestFixture.createByPartialLinkText(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }
        try {
            currentTestFixture.createByTagName(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }
        try {
            currentTestFixture.createByXpath(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }
    }

    @Test
    public void testElementFound_findElementBy_elementExist() {
        WebElementWithAllAttributes element = currentTestFixture.findElementByClassName("aClass");

        Assert.assertEquals("a", element.getTagName());
        Assert.assertEquals("aSomeText", element.getId());

        WebElementWithAllAttributes foundElement = currentTestFixture.findElementByPartialLinkText("Some");
        Assert.assertEquals(element.getId(), foundElement.getId());

        foundElement = currentTestFixture.findElementByPartialLinkText(" text");
        Assert.assertEquals(element.getId(), foundElement.getId());

        foundElement = currentTestFixture.findElementByCssSelector("#aSomeText");
        Assert.assertEquals(element.getId(), foundElement.getId());

        foundElement = currentTestFixture.findElementById("aSomeText");
        Assert.assertEquals(element.getId(), foundElement.getId());

        foundElement = currentTestFixture.findElementByLinkText("Some text");
        Assert.assertEquals(element.getId(), foundElement.getId());

        foundElement = currentTestFixture.findElementByName("anElementName");
        Assert.assertEquals(element.getId(), foundElement.getId());

        foundElement = currentTestFixture.findElementByTagName("a");
        Assert.assertEquals(element.getId(), foundElement.getId());

        foundElement = currentTestFixture.findElementByXpath("//a");
        Assert.assertEquals(element.getId(), foundElement.getId());
    }

    @Test(expected = RuntimeException.class)
    public void testException_openDriver_driverAlreadyOpen() {
        currentTestFixture.openDriver("fakedriver");
    }

    @Test(expected = RuntimeException.class)
    public void testException_openDriver_unknownDriver() {
        CoreWebTestFixture test = new CoreWebTestFixture();
        test.openDriver("someUnknown driver" + System.currentTimeMillis());
    }

    @Test(expected = RuntimeException.class)
    public void testException_quiteDriver_driverAlreadyClosed() {

        currentTestFixture = new CoreWebTestFixture();

        currentTestFixture.openDriver("fakedriver");

        currentTestFixture.closeCurrentDriver();
        currentTestFixture.closeCurrentDriver();

    }

    @Test
    public void testException_getCurrentDriver_noDriverAvailable() {

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

    @Test
    public void testListReturned_asList_allValues() {
        By testBy = currentTestFixture.createById("Id");
        WebElement element = mock(WebElement.class);

        List result = currentTestFixture.asList((By) null, null);
        Assert.assertEquals(2, result.size());
        Assert.assertNull(result.get(0));
        Assert.assertNull(result.get(1));

        result = currentTestFixture.asList((WebElement) null, null);
        Assert.assertEquals(2, result.size());
        Assert.assertNull(result.get(0));
        Assert.assertNull(result.get(1));

        result = currentTestFixture.asList((By) null, testBy);
        Assert.assertEquals(2, result.size());
        Assert.assertNull(result.get(0));
        Assert.assertEquals(testBy, result.get(1));

        result = currentTestFixture.asList(testBy, testBy);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(testBy, result.get(0));
        Assert.assertEquals(testBy, result.get(1));

        result = currentTestFixture.asList(testBy, null);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(testBy, result.get(0));
        Assert.assertNull(result.get(1));

        result = currentTestFixture.asList(element, null);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(element, result.get(0));
        Assert.assertNull(result.get(1));

        result = currentTestFixture.asList(element, testBy);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(element, result.get(0));
        Assert.assertEquals(testBy, result.get(1));
    }

    @Test
    public void testOpenCloseDriver_noTroubles() {

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

    @Override
    protected CoreWebTestFixture buildTestFixture() {
        return new CoreWebTestFixture();
    }


}
