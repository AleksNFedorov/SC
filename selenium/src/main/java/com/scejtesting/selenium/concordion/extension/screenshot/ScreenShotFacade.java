package com.scejtesting.selenium.concordion.extension.screenshot;

import com.scejtesting.selenium.DriverHolderService;
import org.concordion.ext.ScreenshotTaker;
import org.concordion.ext.screenshot.RobotScreenshotTaker;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

public class ScreenShotFacade implements ScreenshotTaker {

    private static final Logger LOG = LoggerFactory.getLogger(ScreenShotFacade.class);


    private final ScreenshotTaker standardScreenShotTaker = new RobotScreenshotTaker();

    private final DriverHolderService driverHolderService = new DriverHolderService();


    @Override
    public int writeScreenshotTo(OutputStream outputStream) throws IOException {
        LOG.debug("method invoked");
        ScreenshotTaker currentScreenShotTaker = resolveScreenShotTaker();
        LOG.info("Screen shot taker successfully resolved");

        return currentScreenShotTaker.writeScreenshotTo(outputStream);
    }

    private ScreenshotTaker resolveScreenShotTaker() {

        ScreenshotTaker screenshotTaker = standardScreenShotTaker;

        try {
            RemoteWebDriver driver = driverHolderService.getCurrentDriver();
            if (driver != null) {
                screenshotTaker = new SeleniumScreenshotTaker(driver);
                LOG.info("Screen shot taker resolved as Selenium based");
            } else {
                LOG.info("Screen shot taker resolved as default, no driver available");
            }
        } catch (Exception ex) {
            LOG.error("Exception during screenshot taker resolution", ex);
        }

        return screenshotTaker;

    }

    @Override
    public String getFileExtension() {
        return "png";
    }
}
