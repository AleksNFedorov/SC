package com.scej.core;

import com.scej.core.integration.GlobalTestContext;
import com.scej.core.integration.extension.*;
import com.scej.core.integration.extension.screenshot.ScreenShotWrapper;
import com.scej.core.integration.extension.screenshot.SeleniumScreenshotTaker;
import org.concordion.api.FailFast;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.api.extension.Extension;
import org.concordion.api.extension.Extensions;
import org.concordion.ext.ScreenshotExtension;
import org.concordion.integration.junit4.ConcordionRunner;
import org.concordion.internal.util.Check;
import org.junit.After;
import org.junit.runner.RunWith;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 * Date: 31.12.13
 * Time: 15:29
 * To change this template use File | Settings | File Templates.
 */
@RunWith(ConcordionRunner.class)
@FailFast(onExceptionType = {UnreachableBrowserException.class, NoSuchWindowException.class})
@Extensions({ExceptionLogger.class})
public abstract class BrowserCoreTest {

    private Logger LOG = LoggerFactory.getLogger(BrowserCoreTest.class);

    private static final ScreenShotWrapper screenShotWrapper = new ScreenShotWrapper();

    @Extension
    public final ConcordionExtension genericExtension = new ConcordionExtension() {
        @Override
        public void addTo(ConcordionExtender concordionExtender) {
            concordionExtender.withSpecificationLocator(new TestContextSpecificationLocator());
            concordionExtender.withTarget(new FileTargetWithDateTimePrefix());
            concordionExtender.withDocumentParsingListener(new IncludeSpecificationDocumentUpdater());
            concordionExtender.withSource(new ClassPathSpecificationSource());
        }
    };


    @Extension
    public final ConcordionExtension screenShotExtension = new ScreenshotExtension() {
        {
            setScreenshotTaker(screenShotWrapper);
        }
    };

    public boolean openBrowser(String browserCapture) throws Exception {
        LOG.debug("method invoked [{}]", browserCapture);
        Check.notNull(browserCapture, "Browser can' t be null");
        Locators.Browser browser = Locators.Browser.valueOf(browserCapture);
        RemoteWebDriver driver = BrowserDriverService.getBrowserDriverService().createDriver(browser);
        LOG.info("Initializing context with selenium driver");
        GlobalTestContext.getInstance().initWithSeleniumDriver(driver);
        screenShotWrapper.switchToSeleniumScreenShotTaker(new SeleniumScreenshotTaker(driver));
        LOG.debug("method invoked");
        return true;
    }


    @After
    public void onTestEnd() {
        LOG.debug("method invoked");
        GlobalTestContext.getInstance().destroyCurrentTestContext();
        LOG.debug("method finished");
    }


    public boolean close() {
        LOG.debug("method invoked");
        screenShotWrapper.switchToStandartScreenShotTaker();
        getCurrentTestDriver().close();
        BrowserDriverService.getBrowserDriverService().stopDriver(getCurrentTestDriver());
        try {
            Thread.sleep(3 * 1000);
        } catch (InterruptedException e) {
            LOG.error("Exception ", e);
        }

        LOG.debug("method finished");
        return true;
    }

    public String getBrowserState() {
        LOG.debug("method invoked");
        try {

            LOG.info("Trying to get browser title as a fact it is still open");
            getCurrentTestDriver().getTitle();
            return Locators.BrowserWindowState.Open.toString();
        } catch (RuntimeException e) {
            LOG.info("Expected exception, seems everything is good");
            return Locators.BrowserWindowState.Closed.toString();
        } catch (Exception ex) {
            LOG.error("Unexpected exception ", ex);
            return null;
        }
    }

    protected RemoteWebDriver getCurrentTestDriver() {
        LOG.debug("method invoked");
        return GlobalTestContext.getInstance().getDriver();
    }

}
