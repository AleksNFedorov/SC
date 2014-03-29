package com.scejtesting.selenium;

import com.scejtesting.core.context.Context;
import com.scejtesting.core.context.TestContextService;
import com.scejtesting.selenium.webdriver.RemoteWebDriverFactory;
import org.concordion.internal.util.Check;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aleks on 27/3/14.
 */
public class SeleniumCoreTest {

    public static final String CURRENT_DRIVER_GLOBAL_VARIABLE = "#CURRENT_DRIVER";
    public static final String CURRENT_DRIVER_NAME_VARIABLE = "#CURRENT_DRIVER_NAME";
    private final static Logger LOG = LoggerFactory.getLogger(SeleniumCoreTest.class);
    private final RemoteWebDriverFactory driverFactory = new RemoteWebDriverFactory();
    private final TestContextService testContextService = new TestContextService();

    public final RemoteWebDriver buildDriver(String driverName) {
        LOG.debug("method invoked [{}]", driverName);

        Context context = testContextService.getCurrentTestContext();

        Check.isTrue(context.getAttribute(CURRENT_DRIVER_GLOBAL_VARIABLE) == null, "Another driver already open");

        RemoteWebDriver remoteDriver = driverFactory.buildRemoteWebDriver(driverName);

        LOG.info("Driver [{}] has been built", remoteDriver);

        context.addAttribute(CURRENT_DRIVER_GLOBAL_VARIABLE, remoteDriver);
        context.addAttribute(CURRENT_DRIVER_NAME_VARIABLE, driverName);

        LOG.debug("method finished");

        return remoteDriver;

    }

    public RemoteWebDriver getCurrentDriver() {
        return testContextService.getCurrentTestContext().getAttribute(CURRENT_DRIVER_GLOBAL_VARIABLE);
    }

    public boolean quitCurrentDriver() {

        RemoteWebDriver currentDriver = getCurrentDriver();

        Context context = testContextService.getCurrentTestContext();

        String driverName = context.getAttribute(CURRENT_DRIVER_NAME_VARIABLE);

        Check.notNull(currentDriver, "There is no active driver, driver name [{}]", driverName);

        currentDriver.quit();

        LOG.info("Driver [{}] successfully quit", driverName);

        return true;

    }


//    public final boolean
}
