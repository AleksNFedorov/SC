package com.scejtesting.selenium.concordion.extension.screenshot;


import org.concordion.ext.ScreenshotTaker;
import org.concordion.ext.ScreenshotUnavailableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

/**
 * User: Fedorovaleks
 * Date: 5/3/14
 */
public class SeleniumScreenshotTaker implements ScreenshotTaker {

    private static final Logger LOG = LoggerFactory.getLogger(ScreenShotFacade.class);


    private final RemoteWebDriver driver;

    public SeleniumScreenshotTaker(RemoteWebDriver remoteWebDriver) {
        LOG.debug("constructor invoked");
        LOG.info("Driver instance found");
        this.driver = remoteWebDriver;
    }

    @Override
    public int writeScreenshotTo(OutputStream outputStream) throws IOException {
        LOG.debug("method invoked");
        byte[] screenshot;
        try {
            screenshot = driver.getScreenshotAs(OutputType.BYTES);
            LOG.info("Screen shot sucessfully created");
        } catch (ClassCastException e) {
            LOG.error("Exception on screen shot ", e);
            throw new ScreenshotUnavailableException("driver does not implement TakesScreenshot");
        }
        outputStream.write(screenshot);
        LOG.debug("method finished");
        return ((Long) ((JavascriptExecutor) driver).executeScript("return document.body.clientWidth")).intValue() + 2; //window.outerWidth"));
    }

    @Override
    public String getFileExtension() {
        return "png";
    }

}
