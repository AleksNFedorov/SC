package com.scejtesting.selenium;

import com.scejtesting.core.context.Context;
import com.scejtesting.core.context.TestContextService;
import com.scejtesting.selenium.webdriver.RemoteWebDriverFactory;
import org.concordion.internal.util.Check;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by aleks on 27/3/14.
 */
public class SeleniumDriverManagerService {

    public static final String DRIVER_REGISTRY_ATTRIBUTE = "#SELENIUM_OPEN_DRIVERS_REGISTRY";
    public static final String CURRENT_DRIVER = "#SELENIUM_CURRENT";
    public static final String DRIVER_FACTORY = "#SELENIUM_DRIVER_FACTORY";

    private final static Logger LOG = LoggerFactory.getLogger(SeleniumDriverManagerService.class);

    private final RemoteWebDriverFactory driverFactory;
    private final TestContextService testContextService;

    public SeleniumDriverManagerService() {
        testContextService = buildTestContextService();
        driverFactory = getOrBuildRemoteWebDriverFactory();
    }

    public final RemoteWebDriver buildDriver(String driverName) {
        LOG.debug("method invoked [{}]", driverName);


        Map<String, RemoteWebDriverRegistryEntry> driverRegistry = getDriverRegistry();

        Check.isTrue(driverRegistry.get(driverName) == null, "Another [" + driverName + "] driver already active");

        RemoteWebDriver remoteDriver = driverFactory.buildRemoteWebDriver(driverName);

        LOG.info("Driver [{}] has been built", remoteDriver);


        RemoteWebDriverRegistryEntry driverRegistryEntry = new RemoteWebDriverRegistryEntry(remoteDriver, driverName);


        driverRegistry.put(CURRENT_DRIVER, driverRegistryEntry);

        driverRegistry.put(driverName, driverRegistryEntry);

        LOG.info("Driver [{}] set as current driver ", driverName);

        LOG.debug("method finished");

        return remoteDriver;

    }

    public RemoteWebDriver getCurrentDriver() {
        return getDriver(CURRENT_DRIVER);
    }

    public boolean setCurrentDriver(String driverName) {
        LOG.debug("method invoked [{}]", driverName);

        Check.notNull(driverName, "Driver name must be specified");

        Map<String, RemoteWebDriverRegistryEntry> driverRegistry = getDriverRegistry();

        RemoteWebDriverRegistryEntry driverRegistryEntry = driverRegistry.get(driverName);

        Check.notNull(driverRegistryEntry, "No driver [" + driverName + "] found");

        driverRegistry.put(CURRENT_DRIVER, driverRegistryEntry);

        LOG.info("Driver [{}] set as current", driverName);


        LOG.debug("method finished");

        return true;
    }

    public RemoteWebDriver getDriver(String driverName) {

        LOG.debug("method invoked [{}]", driverName);

        Map<String, RemoteWebDriverRegistryEntry> driverRegistry = getDriverRegistry();
        RemoteWebDriverRegistryEntry currentDriverEntry = driverRegistry.get(driverName);

        if (currentDriverEntry == null) {
            LOG.warn("No driver [{}] available", driverName);
            return null;
        }

        LOG.debug("Driver [{}] found", driverName);

        return currentDriverEntry.getDriver();

    }

    public boolean quitCurrentDriver() {
        return quitDriver(CURRENT_DRIVER);
    }

    public boolean quitDriver(String driverName) {

        RemoteWebDriver driver = getDriver(driverName);

        Check.notNull(driver, "No driver [" + driverName + "] available");

        driver.quit();

        LOG.info("Driver [{}] successfully quit", driverName);


        Map<String, RemoteWebDriverRegistryEntry> driverRegistry = getDriverRegistry();

        // if driver name is CURRENT_DRIVER
        RemoteWebDriverRegistryEntry tmpRegistryEntry = driverRegistry.get(driverName);

        RemoteWebDriverRegistryEntry driverByNameEntry = driverRegistry.get(tmpRegistryEntry.getDriverName());
        RemoteWebDriverRegistryEntry currentDriverRegistryEntry = driverRegistry.get(CURRENT_DRIVER);

        driverRegistry.remove(driverByNameEntry.getDriverName());

        LOG.info("Driver [{}] removed from driver list", driverByNameEntry.getDriverName());

        // must be the same instance, == correct
        if (driverByNameEntry == currentDriverRegistryEntry) {
            LOG.warn("Driver for quit also current driver, no current driver will be available");
            driverRegistry.remove(CURRENT_DRIVER);
        }


        return true;

    }

    protected Map<String, RemoteWebDriverRegistryEntry> getDriverRegistry() {
        Context currentTestContext = testContextService.getCurrentTestContext();
        Map<String, RemoteWebDriverRegistryEntry> driverRegistry = currentTestContext.getAttribute(DRIVER_REGISTRY_ATTRIBUTE);
        if (driverRegistry == null) {
            driverRegistry = new TreeMap<String, RemoteWebDriverRegistryEntry>();
            currentTestContext.addAttribute(DRIVER_REGISTRY_ATTRIBUTE, driverRegistry);

            LOG.info("New driver registry has been created");
        }
        return driverRegistry;

    }

    private RemoteWebDriverFactory getOrBuildRemoteWebDriverFactory() {

        Context currentTestContext = testContextService.getCurrentTestContext();

        RemoteWebDriverFactory factory = currentTestContext.getAttribute(DRIVER_FACTORY);
        if (factory == null) {
            LOG.info("No driver factory found, creating new one ");
            factory = buildRemoteWebDriverFactory();
            currentTestContext.addAttribute(DRIVER_FACTORY, factory);
            LOG.info("Driver factory has been built");
        }
        return factory;
    }

    protected RemoteWebDriverFactory buildRemoteWebDriverFactory() {
        return new RemoteWebDriverFactory();
    }

    protected TestContextService buildTestContextService() {
        return new TestContextService();
    }

    private class RemoteWebDriverRegistryEntry {
        private final RemoteWebDriver driver;
        private final String driverName;

        private RemoteWebDriverRegistryEntry(RemoteWebDriver driver, String driverName) {
            this.driver = driver;
            this.driverName = driverName;
        }

        public RemoteWebDriver getDriver() {
            return driver;
        }

        public String getDriverName() {
            return driverName;
        }
    }

}
