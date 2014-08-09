package com.scejtesting.selenium;

import org.concordion.internal.util.Check;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by aleks on 5/2/14.
 */

public class WebTestFixture extends CoreWebTestFixture {

    public final static String PAGE_ROOT_ELEMENT_XPATH = "//html";
    private final static Logger LOG = LoggerFactory.getLogger(WebTestFixture.class);

    public void waitSeconds(Long seconds) throws InterruptedException {
        LOG.debug("method invoked");

        Check.notNull(seconds, "Timeout interval can't be null");

        TimeUnit.SECONDS.sleep(seconds);

        LOG.info("method finished");
    }

    public boolean checkElementExist(By by) {

        LOG.debug("method invoked [{}]", by);

        Check.notNull(by, "Search predicate can't be null");

        try {
            getCurrentDriver().findElement(by);
        } catch (RuntimeException ex) {
            LOG.info("Element [{}] does not exist", by);
            LOG.debug("Element [{}] lookup exception", by, ex);
            return false;
        }

        LOG.debug("Element [{}] exist", by);

        return true;
    }

    public boolean checkChildExist(By parent, By child) {

        LOG.debug("method invoked [{}][{}]", parent, child);

        Check.notNull(parent, "Search predicate [parent] can't be null");

        WebElement parentElement;
        try {
            parentElement = getCurrentDriver().findElement(parent);
        } catch (RuntimeException ex) {
            LOG.info("Parent element [{}] does not exist", parent);
            return false;
        }

        LOG.info("Parent element exist");
        return checkChildExist(parentElement, child);

    }

    public boolean checkChildExist(WebElement parent, By child) {

        LOG.debug("method invoked [{}][{}]", parent, child);

        Check.notNull(parent, "Parent element can't be null");
        Check.notNull(child, "Search predicate [child] can't be null");

        try {
            parent.findElement(child);
        } catch (RuntimeException ex) {
            LOG.debug("Child element [{}] exist ", child);
            LOG.debug("Child element lookup exception ", ex);
            return false;
        }

        LOG.debug("Child element exist [{}]", child);

        return true;
    }

    public boolean checkElementContainsText(By element, String text) {

        LOG.debug("method invoked [{}][{}]", element, text);

        Check.notNull(element, "Element search predicate can't be null");

        WebElement foundElement = getCurrentDriver().findElement(element);

        LOG.info("Element found");
        return checkElementContainsText(foundElement, text);
    }

    public boolean checkElementContainsText(WebElement element, String text) {
        LOG.debug("method invoked [{}][{}]", element, text);

        Check.notNull(element, "Element search predicate can't be null");
        Check.notEmpty(text, "Text to search can't be empty");

        boolean contains = element.getText().contains(text);

        LOG.info("Element contains text [{}]", contains);

        LOG.debug("method finished");

        return contains;
    }

    public boolean checkTextOnPage(String text) {
        LOG.debug("method invoked [{}]", text);

        Check.notNull(text, "Text to search can't be null");

        WebElement rootElement = getCurrentDriver().findElement(createByXpath(PAGE_ROOT_ELEMENT_XPATH));

        return checkElementContainsText(rootElement, text);

    }

    public void clickElement(By element) {
        LOG.debug("method invoked [{}]", element);

        Check.notNull(element, "Element search predicate can't be null");

        WebElement elementToClick = getCurrentDriver().findElement(element);

        LOG.info("Element to click found");
        clickElement(elementToClick);

        LOG.debug("method finished");

    }

    public void clickElement(WebElement element) {
        LOG.debug("method invoked [{}]", element);

        Check.notNull(element, "Element to click can't be null");

        element.click();

        LOG.info("Element successfully clicked");

        LOG.debug("method finished");

    }

    public void setValueToElement(By element, String value) {
        LOG.debug("method invoked [{}][{}]", element, value);

        Check.notNull(element, "Element search predicate can't be null");

        WebElement elementToSetValue = getCurrentDriver().findElement(element);

        LOG.info("Element to click found");
        setValueToElement(elementToSetValue, value);
        LOG.debug("method finished");

    }

    public void setValueToElement(WebElement element, String value) {
        LOG.debug("method invoked [{}][{}]", element, value);

        Check.notNull(element, "Element search predicate can't be null");
        Check.notEmpty(value, "Value to set can't be null");

        element.sendKeys(value);

        LOG.info("Value to element successfully set");

        LOG.debug("method finished");
    }

    public void clearElement(By element) {
        LOG.debug("method invoked [{}]", element);

        Check.notNull(element, "Element search predicate can't be null");

        WebElement elementToClear = getCurrentDriver().findElement(element);

        LOG.info("Element to clear found");
        clearElement(elementToClear);
        LOG.debug("method finished");
    }

    public void clearElement(WebElement element) {
        LOG.debug("method invoked [{}]", element);

        Check.notNull(element, "Element to clear can't be null");

        element.clear();

        LOG.info("Element successfully cleared");

        LOG.debug("method finished");

    }

    public boolean checkElementDisplayed(By by) {
        LOG.debug("method invoked [{}]", by);

        Check.notNull(by, "Element search predicate can't be null");

        WebElement elementToCheck = getCurrentDriver().findElement(by);

        LOG.info("Element to check found");
        return elementToCheck.isDisplayed();

    }

    public boolean checkElementEnabled(By by) {
        LOG.debug("method invoked [{}]", by);

        Check.notNull(by, "Element search predicate can't be null");

        WebElement elementToCheck = getCurrentDriver().findElement(by);

        LOG.info("Element to check enabled found");
        return elementToCheck.isEnabled();
    }

    public boolean checkElementSelected(By by) {

        LOG.debug("method invoked [{}]", by);

        Check.notNull(by, "Element search predicate can't be null");

        WebElement elementToCheck = getCurrentDriver().findElement(by);

        LOG.info("Element to check selected found");
        return elementToCheck.isSelected();

    }

    public void submitElement(By elementToSubmitBy) {
        LOG.debug("method invoked [{}]", elementToSubmitBy);

        Check.notNull(elementToSubmitBy, "Element to check selected can't be null");

        WebElement elementToSubmit = getCurrentDriver().findElement(elementToSubmitBy);

        submitElement(elementToSubmit);

        LOG.info("Element to submit [{}]", elementToSubmit);

        LOG.debug("method finished");
    }

    public void submitElement(WebElement elementToSubmit) {

        LOG.debug("method invoked [{}]", elementToSubmit);

        Check.notNull(elementToSubmit, "Element to submit can't be null");

        elementToSubmit.submit();

        LOG.debug("method finished");

    }

    public boolean checkElementAttributeContainsText(By element, String attribute, String text) {

        LOG.debug("method invoked [{}][{}]", element, text);

        Check.notNull(element, "Element search predicate can't be null");

        WebElement foundElement = getCurrentDriver().findElement(element);

        LOG.info("Element found");
        return checkElementAttributeContainsText(foundElement, attribute, text);
    }

    public boolean checkElementAttributeContainsText(WebElement element, String attribute, String text) {
        LOG.debug("method invoked [{}][{}]", element, text);

        Check.notNull(element, "Element search predicate can't be null");
        Check.notEmpty(attribute, "Attribute can't be empty");
        Check.notEmpty(text, "Text to search can't be empty");

        boolean contains = element.getAttribute(attribute).contains(text);
        LOG.info("Element contains text [{}]", contains);

        LOG.debug("method finished");

        return contains;
    }

    public boolean checkElementCSSContainsText(By element, String css, String text) {

        LOG.debug("method invoked [{}][{}]", element, text);

        Check.notNull(element, "Element search predicate can't be null");

        WebElement foundElement = getCurrentDriver().findElement(element);

        LOG.info("Element found");
        return checkElementCSSContainsText(foundElement, css, text);
    }

    public boolean checkElementCSSContainsText(WebElement element, String css, String text) {
        LOG.debug("method invoked [{}][{}]", element, text);

        Check.notNull(element, "Element search predicate can't be null");
        Check.notEmpty(css, "CSS can't be empty");
        Check.notEmpty(text, "Text to search can't be empty");

        boolean contains = element.getAttribute(css).contains(text);
        LOG.info("Element contains text [{}]", contains);

        LOG.debug("method finished");

        return contains;
    }

}