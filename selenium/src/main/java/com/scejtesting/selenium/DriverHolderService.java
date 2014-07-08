package com.scejtesting.selenium;

import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import com.scejtesting.selenium.webdriver.WebDriverController;
import com.scejtesting.selenium.webdriver.WebDriverControllerFactory;
import org.concordion.internal.util.Check;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aleks on 5/3/14.
 */
public class DriverHolderService {

    protected static final String SCEJ_DRIVER_SERVICE = "#SCEJ_SELENIUM_DRIVER_SERVICE";

    protected final static Logger LOG = LoggerFactory.getLogger(DriverHolderService.class);

    protected final WebDriverControllerFactory driverServiceFactory = new WebDriverControllerFactory();

    private final TestContext currentTestContext = new TestContextService().getCurrentTestContext();

    public final RemoteWebDriver openDriver(String driverName) {
        LOG.debug("method invoked [{}]", driverName);

        Check.notNull(driverName, "Driver name must be specificed");

        Check.isTrue(getCurrentDriver() == null, "Some driver already open");

        WebDriverController driverService = driverServiceFactory.buildDriverService(driverName);

        LOG.info("Driver [{}] has been built", driverName);

        getCurrentTestContext().addAttribute(SCEJ_DRIVER_SERVICE, driverService);

        LOG.debug("method finished");

        return driverService.openDriver();

    }

    public RemoteWebDriver getCurrentDriver() {

        LOG.debug("method invoked");

        WebDriverController driverService = getCurrentTestContext().getAttribute(SCEJ_DRIVER_SERVICE);

        if (driverService == null) {
            LOG.warn("No driver service");
            return null;
        }

        RemoteWebDriver currentOpenDriver = driverService.getOpenDriver();

        if (currentOpenDriver == null) {
            LOG.warn("No open driver");
            throw new IllegalStateException("No driver service");
        }

        LOG.debug("method finished");
        return currentOpenDriver;
    }

    public void closeCurrentDriver() {

        WebDriverController service = getCurrentTestContext().getAttribute(SCEJ_DRIVER_SERVICE);

        Check.notNull(service, "No driver available");

        service.stopService();

        LOG.info("Current driver successfully quit");

        getCurrentTestContext().cleanAttribute(SCEJ_DRIVER_SERVICE);

    }

    protected TestContext getCurrentTestContext() {
        return currentTestContext;
    }
}
