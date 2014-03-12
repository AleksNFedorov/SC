package com.areon.autotests.core.integration.extension.screenshot;

import org.concordion.ext.ScreenshotTaker;
import org.concordion.ext.screenshot.RobotScreenshotTaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 * Date: 1/23/14
 * Time: 6:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScreenShotWrapper implements ScreenshotTaker {

    private Logger LOG = LoggerFactory.getLogger(ScreenShotWrapper.class);

    {
        LOG.info("Initialized");
    }


    private final ScreenshotTaker standardScreenShotTaker = new RobotScreenshotTaker();

    private ScreenshotTaker activeScreenShotTaker = standardScreenShotTaker;

    @Override
    public int writeScreenshotTo(OutputStream outputStream) throws IOException {
        LOG.debug("method invoked");
        try{
            return activeScreenShotTaker.writeScreenshotTo(outputStream);
        } catch (RuntimeException ex) {
            LOG.error("Exception on screen shot taking, switching to default ", ex);
            switchToStandartScreenShotTaker();
            return activeScreenShotTaker.writeScreenshotTo(outputStream);
        }
    }


    public void switchToSeleniumScreenShotTaker(SeleniumScreenshotTaker screenshotTaker) {
        LOG.debug("method invoked");
        activeScreenShotTaker = screenshotTaker;
        LOG.debug("method finished");
    }

    public void switchToStandartScreenShotTaker() {
        LOG.debug("method invoked");
        activeScreenShotTaker = standardScreenShotTaker;
        LOG.debug("method finished");
    }

    @Override
    public String getFileExtension() {
        return activeScreenShotTaker.getFileExtension();
    }
}
