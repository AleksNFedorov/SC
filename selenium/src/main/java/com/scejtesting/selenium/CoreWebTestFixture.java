package com.scejtesting.selenium;

import com.scejtesting.core.CoreTestFixture;
import org.concordion.internal.util.Check;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by aleks on 27/3/14.
 */
public class CoreWebTestFixture extends CoreTestFixture {

    protected final static Logger LOG = LoggerFactory.getLogger(CoreWebTestFixture.class);

    private DriverHolderService driverHolderService = new DriverHolderService();

    public void goToURL(String URL) {
        LOG.debug("method invoked [{}]", URL);

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

    public WebElement findElement(By by) {
        LOG.debug("find element by [{}]", by);

        Check.notNull(by, "By predicate can't be null");

        WebElement element = getCurrentDriver().findElement(by);

        LOG.debug("Found element [{}]", element);

        return element;
    }

    public List<WebElement> findElements(By by) {
        LOG.debug("find element by [{}]", by);

        Check.notNull(by, "By predicate can't be null");

        List<WebElement> elements = getCurrentDriver().findElements(by);

        LOG.debug("Found elements [{}]", elements);

        return elements;

    }

    public By createByClassName(String className) {

        LOG.info("create by class [{}]", className);

        Check.notEmpty(className, "Class name can't be empty");

        return By.className(className);
    }

    public By createByCssSelector(String ByCssSelector) {

        LOG.info("create by CssSelector [{}]", ByCssSelector);

        Check.notEmpty(ByCssSelector, "CSS Selector can't be empty");

        return By.cssSelector(ByCssSelector);
    }

    public By createById(String Id) {

        LOG.info("create by Id [{}]", Id);

        Check.notEmpty(Id, "Id can't be empty");

        return By.id(Id);
    }

    public By createByLinkText(String linkText) {

        LOG.info("create by link text [{}]", linkText);

        Check.notEmpty(linkText, "Link text can't be empty");

        return By.linkText(linkText);
    }

    public By createByName(String name) {

        LOG.info("create by name [{}]", name);

        Check.notEmpty(name, "Name can't be empty");

        return By.name(name);
    }

    public By createByPartialLinkText(String partialLinkText) {

        LOG.info("create by partial link text [{}]", partialLinkText);

        Check.notEmpty(partialLinkText, "Partial link can't be empty");

        return By.partialLinkText(partialLinkText);
    }

    public By createByTagName(String tagName) {

        LOG.info("create by tag name [{}]", tagName);

        Check.notEmpty(tagName, "Tag name name can't be empty");

        return By.tagName(tagName);
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

}
