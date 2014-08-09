package com.scejtesting.selenium;

import com.scejtesting.selenium.elements.WebElementWithAllAttributes;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by aleks on 5/3/14.
 */
public class WebTestFixtureTest extends CoreScejTest<WebTestFixture> {

    private By divById = By.id("id1");
    private String textToLookup = "Div id 1 text";
    private By byTagName = By.tagName("div");
    private By unknownElementBy = By.id("unknownElementId");
    private By buttonById = By.id("button");
    private By inputText = By.id("text");

    @Test
    public void testException_nullParameters() throws InterruptedException {

        WebElement mockElement = Mockito.mock(WebElement.class);

        try {
            currentTestFixture.waitSeconds(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.checkElementExist(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.checkChildExist((By) null, null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.checkChildExist((WebElement) null, null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.checkChildExist(divById, null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.checkChildExist(mockElement, null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.checkElementContainsText(divById, null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.checkElementContainsText((WebElement) null, null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.checkElementContainsText(mockElement, null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.checkTextOnPage(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.clickElement((By) null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.clickElement((WebElement) null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.setValueToElement((WebElement) null, "");
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.setValueToElement((By) null, "");
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.setValueToElement(mockElement, null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.clearElement((By) null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.clearElement((WebElement) null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.clearElement((WebElement) null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.checkElementDisplayed(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.checkElementEnabled(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.checkElementSelected(null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.submitElement((By) null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.submitElement((WebElement) null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.checkElementAttributeContainsText((By) null, "", "");
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.checkElementAttributeContainsText((WebElement) null, "", "");
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.checkElementAttributeContainsText(mockElement, null, "");
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.checkElementAttributeContainsText(mockElement, "", null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.checkElementCSSContainsText(mockElement, "", null);
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.checkElementCSSContainsText(mockElement, null, "");
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.checkElementCSSContainsText((WebElement) null, "", "");
            Assert.fail();
        } catch (RuntimeException ex) {
        }

        try {
            currentTestFixture.checkElementCSSContainsText((By) null, "", "");
            Assert.fail();
        } catch (RuntimeException ex) {
        }
    }


    @Test
    public void documentNavigationTest() {

        List<WebElementWithAllAttributes> foundElements = currentTestFixture.findElements(byTagName);

        assertEquals(2, foundElements.size());
        assertEquals(0, currentTestFixture.findElements(unknownElementBy).size());

        WebElement foundElement = currentTestFixture.findElement(divById);

        assertNotNull(foundElement);
        assertEquals("Div id 1 text", foundElement.getText());

        try {
            currentTestFixture.findElement(unknownElementBy);
            fail("No such element found exception");
        } catch (RuntimeException ex) {

        }

        assertTrue(currentTestFixture.checkChildExist(byTagName, divById));
        assertFalse(currentTestFixture.checkChildExist(byTagName, unknownElementBy));

        try {
            currentTestFixture.checkChildExist(unknownElementBy, divById);
            fail("Unknown parent exception expected");
        } catch (RuntimeException ex) {
        }

        assertTrue(currentTestFixture.checkElementExist(divById));
        assertFalse(currentTestFixture.checkElementExist(unknownElementBy));

    }

    @Test
    public void documentContentTest() {

        assertTrue(currentTestFixture.checkElementContainsText(divById, textToLookup));
        assertTrue(currentTestFixture.checkTextOnPage(textToLookup));

        assertFalse(currentTestFixture.checkTextOnPage(textToLookup + System.currentTimeMillis()));

        try {
            currentTestFixture.checkElementContainsText(unknownElementBy, textToLookup);
        } catch (RuntimeException ex) {

        }

    }

    @Test
    public void documentElementsStateTest() {

        assertTrue(currentTestFixture.checkElementDisplayed(divById));
        assertTrue(currentTestFixture.checkElementEnabled(divById));

        assertTrue(currentTestFixture.checkElementDisplayed(currentTestFixture.createById("buttonDisabled")));
        assertFalse(currentTestFixture.checkElementEnabled(currentTestFixture.createById("buttonDisabled")));

        assertTrue(currentTestFixture.checkElementSelected(currentTestFixture.createById("checkbox")));

        try {
            assertFalse(currentTestFixture.checkElementSelected(unknownElementBy));
            fail("Element not found exception expected");
        } catch (RuntimeException ex) {
        }

        try {
            assertFalse(currentTestFixture.checkElementDisplayed(unknownElementBy));
            fail("Element not found exception expected");
        } catch (RuntimeException ex) {
        }

        try {
            assertFalse(currentTestFixture.checkElementEnabled(unknownElementBy));
            fail("Element not found exception expected");
        } catch (RuntimeException ex) {

        }

        try {
            currentTestFixture.checkElementSelected(divById);
            fail("Unable to check exception expected");
        } catch (UnsupportedOperationException ex) {
        }

    }

    @Test
    public void elementManipulationTest() {

        currentTestFixture.clickElement(buttonById);
        currentTestFixture.clickElement(divById);

        try {
            currentTestFixture.clickElement(unknownElementBy);
            fail("Element not found exception expected");
        } catch (RuntimeException ex) {

        }

        currentTestFixture.setValueToElement(inputText, textToLookup);

        assertEquals(textToLookup, currentTestFixture.findElement(inputText).getAttribute("value"));

        currentTestFixture.clearElement(inputText);

        assertEquals("", currentTestFixture.findElement(inputText).getAttribute("value").trim());

    }

    @Override
    protected WebTestFixture buildTestFixture() {
        return new WebTestFixture();
    }
}
