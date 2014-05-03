package com.scejtesting.selenium;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by aleks on 5/3/14.
 */
public class WebTestFixtureTest extends CoreScejTest {

    private WebTestFixture currentTestFixture;

    private By divById = By.id("id1");
    private String textToLookup = "Div id 1 text";
    private By byTagName = By.tagName("div");
    private By unknownElementBy = By.id("unknownElementId");
    private By buttonById = By.id("button");
    private By inputText = By.id("text");


    @Before
    public void initTestCase() {

        URL testPageURL = getClass().getClassLoader().getResource("WebTestFixtureTest.html");

        currentTestFixture = new WebTestFixture();

        currentTestFixture.openDriver("fakeDriver");

        currentTestFixture.goToURL(testPageURL.toString());


    }

    @After
    public void finishTestCase() {
        currentTestFixture.closeCurrentDriver();
        currentTestFixture = null;
    }


    @Test
    public void getTitleTest() {

        String pageTitle = currentTestFixture.getTitle();

        assertNotNull(pageTitle);

        assertEquals("WebTestFixtureTest", pageTitle);

    }


    @Test
    public void documentNavigationTest() {


        List<WebElement> foundElements = currentTestFixture.findElements(byTagName);

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

}
