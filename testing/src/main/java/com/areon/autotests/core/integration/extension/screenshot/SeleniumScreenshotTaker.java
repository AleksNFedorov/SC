package com.areon.autotests.core.integration.extension.screenshot;


import org.concordion.ext.ScreenshotTaker;
import org.concordion.ext.ScreenshotUnavailableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 * Date: 1/23/14
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class SeleniumScreenshotTaker implements ScreenshotTaker{

    private Logger LOG = LoggerFactory.getLogger(ScreenShotWrapper.class);
    {
        LOG.info("Initialized");
    }


    private final WebDriver driver;

    public SeleniumScreenshotTaker(WebDriver driver) {
        LOG.debug("constructor invoked");
        WebDriver baseDriver = driver;
        while (baseDriver instanceof EventFiringWebDriver) {
            LOG.info("driver instance is not expected");
            baseDriver = ((EventFiringWebDriver)baseDriver).getWrappedDriver();
        }
        LOG.info("Driver instance found");
        this.driver = baseDriver;
    }

    @Override
    public int writeScreenshotTo(OutputStream outputStream) throws IOException {
        LOG.debug("method invoked");
        byte[] screenshot;
        try {
            screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
            LOG.info("Screen shot sucessfully created");
        } catch (ClassCastException e) {
            LOG.error("Exception on screen shot ",e);
            throw new ScreenshotUnavailableException("driver does not implement TakesScreenshot");
        }
        outputStream.write(screenshot);
        LOG.debug("method finished");
        return ((Long)((JavascriptExecutor)driver).executeScript("return document.body.clientWidth")).intValue() + 2; //window.outerWidth"));
    }

    @Override
    public String getFileExtension() {
        return "png";
    }

}
