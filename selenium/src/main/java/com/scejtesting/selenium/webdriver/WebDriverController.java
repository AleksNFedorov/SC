package com.scejtesting.selenium.webdriver;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by aleks on 27/3/14.
 */
public abstract class WebDriverController {

    public static final String DRIVER_NAME_PROPERTY = "#SCEJ_DRIVER_NAME";

    private final static Logger LOG = LoggerFactory.getLogger(WebDriverController.class);
    private final Properties driverProperties;
    private final String driverName;
    private RemoteWebDriver openDriver;


    protected WebDriverController(Properties driverProperties) {
        this.driverProperties = driverProperties;

        driverName = getDriverName(driverProperties);

        LOG.info("Driver service crated for driver [{}]", driverName);
    }

    public static String getDriverName(Properties driverProperties) {
        return String.valueOf(driverProperties.get(DRIVER_NAME_PROPERTY));
    }

    public static void saveDriverName(String driverName, Properties driverProperties) {
        driverProperties.put(DRIVER_NAME_PROPERTY, driverName);
    }

    protected abstract DesiredCapabilities getDriverSpecificCapabilities();

    protected abstract RemoteWebDriver buildDriver(DesiredCapabilities driverCapabilities);

    protected abstract void onDriverClose();

    public RemoteWebDriver openDriver() {

        DesiredCapabilities capabilities = buildFromProperties(driverProperties);

        openDriver = buildDriver(capabilities);

        LOG.info("Driver [{}] has been opened", driverName);

        return openDriver;

    }

    protected DesiredCapabilities buildFromProperties(Properties properties) {

        LOG.debug("method invoked");

        DesiredCapabilities driverCapabilities = getDriverSpecificCapabilities();
        driverCapabilities.merge(new DesiredCapabilities(new HashMap<String, Object>((Map) properties)));

        LOG.info("Driver [{}] capabilities has been built", driverName);

        LOG.debug("method finished [{}]", driverCapabilities);

        return driverCapabilities;
    }

    public void stopService() {
        LOG.debug("method invoked");

        openDriver.close();

        onDriverClose();

        LOG.info("Service stopped");

        LOG.debug("method finished");

    }

    public RemoteWebDriver getOpenDriver() {
        return openDriver;
    }

    protected Properties getDriverProperties() {
        return driverProperties;
    }
}
