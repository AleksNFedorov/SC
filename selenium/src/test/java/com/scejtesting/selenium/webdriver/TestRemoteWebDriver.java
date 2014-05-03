package com.scejtesting.selenium.webdriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

/**
 * Created by aleks on 30/3/14.
 */
public class TestRemoteWebDriver extends RemoteWebDriver {

    private final WebDriver realWebDriver;

    public TestRemoteWebDriver() {
        realWebDriver = new HtmlUnitDriver();
    }

    @Override
    public void close() {
        realWebDriver.close();
    }

    @Override
    public void get(String url) {
        realWebDriver.get(url);
    }

    @Override
    public String getTitle() {
        return realWebDriver.getTitle();
    }

    @Override
    public WebElement findElement(By by) {
        return realWebDriver.findElement(by);
    }

    @Override
    public List<WebElement> findElements(By by) {
        return realWebDriver.findElements(by);
    }
}
