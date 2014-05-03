package com.scejtesting.selenium;

import com.scejtesting.core.concordion.extension.ScejCoreExtensions;
import org.concordion.api.extension.Extensions;
import org.concordion.integration.junit4.ConcordionRunner;
import org.concordion.internal.util.Check;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by aleks on 5/2/14.
 */

@RunWith(ConcordionRunner.class)
@Extensions(value = ScejCoreExtensions.class)
public class WebTestFixture extends SeleniumDriverManagerService {

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

        boolean elementExist = getCurrentDriver().findElement(by) != null;

        LOG.debug("Element exist [{}]", elementExist);

        return elementExist;
    }

    public boolean checkChildExist(By parent, By child) {

        LOG.debug("method invoked [{}][{}]", parent, child);

        Check.notNull(parent, "Search predicate [parent] can't be null");

        WebElement parentElement = getCurrentDriver().findElement(parent);

        if (parentElement != null) {
            LOG.info("Parent element exist");
            return checkChildExist(parentElement, child);

        }

        LOG.info("Parent element did not find");

        LOG.debug("method finished");

        return false;
    }

    public boolean checkChildExist(WebElement parent, By child) {

        LOG.debug("method invoked [{}][{}]", parent, child);

        Check.notNull(parent, "Parent element can't be null");
        Check.notNull(child, "Search predicate [child] can't be null");

        boolean childExist = parent.findElement(child) != null;

        LOG.debug("Child element exist [{}]", childExist);

        return childExist;
    }

    public boolean checkElementContainsText(By element, String text) {

        LOG.debug("method invoked [{}][{}]", element, text);

        Check.notNull(element, "Element search predicate can't be null");

        WebElement foundElement = getCurrentDriver().findElement(element);

        if (foundElement != null) {
            LOG.info("Element found");
            return checkElementContainsText(foundElement, text);

        }

        LOG.info("Element did not find");

        LOG.debug("method finished");

        return false;
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

        if (elementToClick != null) {
            LOG.info("Element to click found");
            clickElement(elementToClick);
            return;
        }

        LOG.info("Element to click did not find");

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

        if (elementToSetValue != null) {
            LOG.info("Element to click found");
            setValueToElement(elementToSetValue, value);
            return;
        }

        LOG.info("Element to save value did not find");

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

        if (elementToClear != null) {
            LOG.info("Element to clear found");
            clearElement(elementToClear);
            return;
        }

        LOG.info("Element to click did not find");

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

        if (elementToCheck != null) {
            LOG.info("Element to check found");
            return checkElementDisplayed(elementToCheck);
        }

        LOG.info("Element to check did not find");

        LOG.debug("method finished");

        return false;
    }

    public boolean checkElementDisplayed(WebElement element) {
        LOG.debug("method invoked [{}]", element);

        Check.notNull(element, "Element to clear can't be null");

        boolean elementDisplayed = element.isDisplayed();

        LOG.info("Element displayed [{}]", elementDisplayed);

        LOG.debug("method finished");

        return elementDisplayed;

    }

    public boolean checkElementEnabled(By by) {
        LOG.debug("method invoked [{}]", by);

        Check.notNull(by, "Element search predicate can't be null");

        WebElement elementToCheck = getCurrentDriver().findElement(by);

        if (elementToCheck != null) {
            LOG.info("Element to check enabled found");
            return checkElementDisplayed(elementToCheck);
        }

        LOG.info("Element to check enabled did not find");

        LOG.debug("method finished");

        return false;
    }

    public boolean checkElementEnabled(WebElement element) {
        LOG.debug("method invoked [{}]", element);

        Check.notNull(element, "Element to check enabled can't be null");

        boolean elementDisplayed = element.isEnabled();

        LOG.info("Element displayed [{}]", elementDisplayed);

        LOG.debug("method finished");

        return elementDisplayed;

    }

    public boolean checkElementSelected(By by) {

        LOG.debug("method invoked [{}]", by);

        Check.notNull(by, "Element search predicate can't be null");

        WebElement elementToCheck = getCurrentDriver().findElement(by);

        if (elementToCheck != null) {
            LOG.info("Element to check selected found");
            return checkElementDisplayed(elementToCheck);
        }

        LOG.info("Element to check selected did not find");

        LOG.debug("method finished");

        return false;
    }

    public boolean checkElementSelected(WebElement element) {

        LOG.debug("method invoked [{}]", element);

        Check.notNull(element, "Element to check selected can't be null");

        boolean elementSelected = element.isSelected();

        LOG.info("Element selected [{}]", elementSelected);

        LOG.debug("method finished");

        return elementSelected;

    }


}
