package com.scej.autotests.core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 * Date: 01.01.14
 * Time: 21:35
 * To change this template use File | Settings | File Templates.
 */
public class WebCoreTest extends BrowserCoreTest {

    private static final Logger LOG = LoggerFactory.getLogger(WebCoreTest.class);

    public boolean setValueToElementByXPath(String xpath, String value) {
        return setValueToElementBy(By.xpath(xpath), value);

    }

    public boolean setValueToElementByName(String name, String value) {
        return setValueToElementBy(By.name(name), value);
    }


    private boolean setValueToElementBy(By by, String value) {
        WebElement element = getCurrentTestDriver().findElement(by);
        element.clear();
        element.sendKeys(value);
        return true;
    }

    public boolean clickElementByName(String name) {
        return clickElementBy(By.name(name));
    }

    public boolean clickElementByXPath(String xpath) throws InterruptedException {
        return clickElementBy(By.xpath(xpath));
    }

    private boolean clickElementBy(By by) {
        WebElement element = getCurrentTestDriver().findElement(by);
        element.click();
        return true;
    }

    public boolean checkElementByXpathClickable(String xpath) {
        return checkElementByClickable(By.xpath(xpath));
    }

    private boolean checkElementByClickable(By by) {
        WebElement element = getCurrentTestDriver().findElement(by);
        if (element.isDisplayed()) {
            LOG.info("Element is displayed");
            element.click();
            LOG.info("Element clicked");
            return true;
        } else {
            LOG.info("Element is not displayed");
        }
        return false;
    }

    public boolean checkTextOnPage(String text) {
        if ((getCurrentTestDriver().findElement(By.xpath(Locators.pageXpath))).getText().contains(text)) {
            return true;
        }
        return false;
    }

    public boolean goToAddress(String address) {
        getCurrentTestDriver().get(address);
        return true;
    }

    public String getTitle() {
        return getCurrentTestDriver().getTitle();
    }

    public boolean findTextInInputs(String text){
        List<WebElement> inputs = getCurrentTestDriver().findElementsByTagName("input");
        for(WebElement element:inputs){
            if(element.getAttribute("value").equals(text)||element.getAttribute("title").equals(text)){
                return true;
            }
        }
        return false;
    }




    /**
     * Метод проверяет нажат ли компонент меню, и если не нажат - кликает
     * @param mainBy - By меню, где нужно проверить елемент на нажатие
     * @param name - название пункта меню
     * @param path - патч к елементам меню (главный патч + патч = полный патч к елементу)
     * */
    public boolean clickIfNotClicked(By mainBy, String name, String path){
        LOG.debug("method invoked [{}], [{}]", mainBy, name);
        RemoteWebDriver driver = getCurrentTestDriver();
        WebElement mainMenu =  driver.findElement(mainBy);
        try {
            WebElement test = mainMenu.findElement(By.xpath("./"+path+"[contains(.,'"+name+"')]"));
            if(test!=null){
                test.click();
                LOG.debug("Нажат елемент");
                return true;
            } else {
                LOG.debug("Елемент не найден");
                return false;
            }
        } catch (RuntimeException e) {
            LOG.error("Елемент не найден [{}]", e.getMessage(), e);
            throw e;
        } finally {
            LOG.debug("method finished");
        }


    }

    public By createByXpath(String xpath){
        return By.xpath(xpath);
    }
    public By createByName(String name){
        return By.name(name);
    }

    public By createById(String id){
        return By.id(id);
    }



}
