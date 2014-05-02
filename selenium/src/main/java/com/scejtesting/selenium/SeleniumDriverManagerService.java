package com.scejtesting.selenium;

import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import com.scejtesting.selenium.webdriver.ScejDriverService;
import com.scejtesting.selenium.webdriver.ScejDriverServiceFactory;
import org.concordion.internal.util.Check;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aleks on 27/3/14.
 */
public class SeleniumDriverManagerService {

    protected static final String SCEJ_DRIVER_SERVICE = "#SCEJ_SELENIUM_DRIVER_SERVICE";

    private final static Logger LOG = LoggerFactory.getLogger(SeleniumDriverManagerService.class);

    private final ScejDriverServiceFactory driverServiceFactory = new ScejDriverServiceFactory();


    public final RemoteWebDriver openDriver(String driverName) {
        LOG.debug("method invoked [{}]", driverName);

        Check.notNull(driverName, "Driver name must be specificed");

        Check.isTrue(getCurrentDriver() == null, "Some driver already open");

        ScejDriverService driverService = driverServiceFactory.buildDriverService(driverName);

        LOG.info("Driver [{}] has been built", driverName);

        getCurrentTestContext().addAttribute(SCEJ_DRIVER_SERVICE, driverService);

        LOG.debug("method finished");

        return driverService.openDriver();

    }

    public RemoteWebDriver getCurrentDriver() {

        LOG.debug("method invoked");

        ScejDriverService driverService = getCurrentTestContext().getAttribute(SCEJ_DRIVER_SERVICE);

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

        ScejDriverService service = getCurrentTestContext().getAttribute(SCEJ_DRIVER_SERVICE);

        Check.notNull(service, "No driver available");

        service.stopService();

        LOG.info("Current driver successfully quit");

        getCurrentTestContext().cleanAttribute(SCEJ_DRIVER_SERVICE);

    }

    protected TestContext getCurrentTestContext() {
        return new TestContextService().getCurrentTestContext();
    }


}
