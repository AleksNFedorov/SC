package com.scejtesting.selenium;

import com.scejtesting.selenium.elements.WebElementWithAllAttributes;
import org.concordion.internal.util.Check;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by aleks on 27/3/14.
 */

public class CoreWebTestFixture {

    protected final static Logger LOG = LoggerFactory.getLogger(CoreWebTestFixture.class);

    //Created before file extension
    private final DriverHolderService driverHolderService = new DriverHolderService();

    public void goToURL(String URL) {
        LOG.debug("method invoked [{}]", URL);

        Check.notNull(URL, "URL can't be null");

        getCurrentDriver().get(URL);

        LOG.debug("method finished");
    }

    public String getCurrentURL() {
        LOG.debug("method invoked ");

        String currentURL = getCurrentDriver().getCurrentUrl();

        LOG.debug("method finished [{}]", currentURL);

        return currentURL;
    }

    public String getTitle() {
        LOG.debug("method invoked ");

        String title = getCurrentDriver().getTitle();

        LOG.debug("method finished [{}]", title);

        return title;
    }

    public WebElementWithAllAttributes findElementInParent(By parentBy, By targetBy) {
        LOG.debug("find element in parent by [{}][{}]", parentBy, targetBy);

        Check.notNull(parentBy, "By predicate can't be null");
        Check.notNull(targetBy, "By predicate can't be null");

        WebElement element = getCurrentDriver().findElement(parentBy);

        element = element.findElement(targetBy);

        LOG.debug("Found element [{}]", element);

        return wrapWebElement(element);
    }

    public WebElementWithAllAttributes findElementInParent(WebElement element, By targetBy) {
        LOG.debug("find element in parent by [{}][{}]", element, targetBy);

        Check.notNull(element, "Element can't be null");
        Check.notNull(targetBy, "By predicate can't be null");

        element = element.findElement(targetBy);

        LOG.debug("Found element [{}]", element);

        return wrapWebElement(element);
    }

    public WebElementWithAllAttributes findElement(By by) {
        LOG.debug("find element by [{}]", by);

        Check.notNull(by, "By predicate can't be null");

        WebElement element = getCurrentDriver().findElement(by);

        LOG.debug("Found element [{}]", element);

        return wrapWebElement(element);
    }

    public List<WebElementWithAllAttributes> findElements(By by) {
        LOG.debug("find element by [{}]", by);

        Check.notNull(by, "By predicate can't be null");

        List<WebElement> elements = getCurrentDriver().findElements(by);
        List<WebElementWithAllAttributes> resultList =
                new ArrayList<WebElementWithAllAttributes>(elements.size());

        for (WebElement element : elements) {
            resultList.add(wrapWebElement(element));
        }

        LOG.debug("Found elements [{}]", elements);
        return resultList;
    }

    private WebElementWithAllAttributes wrapWebElement(WebElement element) {
        return new WebElementWithAllAttributes(element);
    }

    public WebElementWithAllAttributes findElementByClassName(String className) {
        By byClassName = createByClassName(className);
        return findElement(byClassName);
    }

    public By createByClassName(String className) {

        LOG.info("create by class [{}]", className);

        Check.notEmpty(className, "Class name can't be empty");

        return By.className(className);
    }

    public WebElementWithAllAttributes findElementByCssSelector(String cssSelector) {
        By byCssSelector = createByCssSelector(cssSelector);
        return findElement(byCssSelector);
    }

    public By createByCssSelector(String ByCssSelector) {

        LOG.info("create by CssSelector [{}]", ByCssSelector);

        Check.notEmpty(ByCssSelector, "CSS Selector can't be empty");

        return By.cssSelector(ByCssSelector);
    }

    public WebElementWithAllAttributes findElementById(String id) {
        By byId = createById(id);
        return findElement(byId);
    }

    public By createById(String Id) {

        LOG.info("create by Id [{}]", Id);

        Check.notEmpty(Id, "Id can't be empty");

        return By.id(Id);
    }

    public WebElementWithAllAttributes findElementByLinkText(String linkText) {
        By byLinkText = createByLinkText(linkText);
        return findElement(byLinkText);
    }

    public By createByLinkText(String linkText) {

        LOG.info("create by link text [{}]", linkText);

        Check.notEmpty(linkText, "Link text can't be empty");

        return By.linkText(linkText);
    }

    public WebElementWithAllAttributes findElementByName(String name) {
        By byName = createByName(name);
        return findElement(byName);
    }

    public By createByName(String name) {

        LOG.info("create by name [{}]", name);

        Check.notEmpty(name, "Name can't be empty");

        return By.name(name);
    }

    public WebElementWithAllAttributes findElementByPartialLinkText(String partialLinkText) {
        By byPartialLinkText = createByPartialLinkText(partialLinkText);
        return findElement(byPartialLinkText);
    }

    public By createByPartialLinkText(String partialLinkText) {

        LOG.info("create by partial link text [{}]", partialLinkText);

        Check.notEmpty(partialLinkText, "Partial link can't be empty");

        return By.partialLinkText(partialLinkText);
    }

    public WebElementWithAllAttributes findElementByTagName(String tagName) {
        By byTagName = createByTagName(tagName);
        return findElement(byTagName);
    }

    public By createByTagName(String tagName) {

        LOG.info("create by tag name [{}]", tagName);

        Check.notEmpty(tagName, "Tag name name can't be empty");

        return By.tagName(tagName);
    }

    public WebElementWithAllAttributes findElementByXpath(String xpath) {
        By byXpath = createByXpath(xpath);
        return findElement(byXpath);
    }

    public By createByXpath(String xpath) {

        LOG.info("create by xpath [{}]", xpath);

        Check.notEmpty(xpath, "Xpath can't be empty");

        return By.xpath(xpath);
    }

    public RemoteWebDriver getCurrentDriver() {
        return driverHolderService.getCurrentDriver();
    }

    public RemoteWebDriver openDriver(String driverName) {
        return driverHolderService.openDriver(driverName);

    }

    public void closeCurrentDriver() {
        driverHolderService.closeCurrentDriver();

    }

    public List asList(By parentBy, By childBy) {
        return Arrays.asList(parentBy, childBy);
    }

    public List asList(WebElement parentElement, By childBy) {
        return Arrays.asList(parentElement, childBy);
    }
}
