package com.scejtesting.selenium.webdriver;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Properties;

/**
 * Created by aleks on 27/3/14.
 */
public class RemoteWebDriverBuilderService {

    public static final String DRIVER_PROPERTIES_FILE_SUFFIX = ".driver.properties";
    public static final String DRIVER_DEFAULT_PROPERTIES_FILE_SUFFIX = ".driver.default.properties";
    private final static Logger LOG = LoggerFactory.getLogger(RemoteWebDriverBuilderService.class);
    private final Properties driverProperties = new Properties();
    private final String driverName;
    private RemoteWebDriverBuilder driverBuilder;

    public RemoteWebDriverBuilderService(String driverName) {
        this.driverName = driverName;
    }

    void init() {
        LOG.debug("init method invoked");

        loadDriverProperties();

        driverBuilder = createDriverBuilder();

        LOG.info("init successfully finished");
        LOG.debug("method finished");
    }


    private RemoteWebDriverBuilder createDriverBuilder() {
        LOG.debug("method invoked");

        String builderImplementationClass = driverProperties.getProperty(RemoteWebDriverBuilder.class.getCanonicalName());
        LOG.info("Driver [{}] builder implementation resolved as [{}]", getDriverName(), builderImplementationClass);

        RemoteWebDriverBuilder driverBuilder;
        try {
            Class<RemoteWebDriverBuilder> remoteWebDriverBuilderClass = (Class<RemoteWebDriverBuilder>) Class.forName(builderImplementationClass);
            driverBuilder = remoteWebDriverBuilderClass.newInstance();

            LOG.debug("Driver [{}] builder instance successfully created", getDriverName());

        } catch (Exception ex) {
            LOG.error("Can't instanciate driver [{}] builder ", getDriverName(), ex);
            throw new RuntimeException("Can't instanciate class ", ex);
        }

        LOG.debug("method finished");

        return driverBuilder;


    }

    private void loadDriverProperties() {
        LOG.debug("method invoked");

        String pathToPropertyFile = getDriverPropertyFilePath();
        LOG.debug("path to driver property file [{}]", pathToPropertyFile);

        try {
            if (pathToPropertyFile != null) {
                driverProperties.load(new FileInputStream(pathToPropertyFile));
                driverProperties.putAll(System.getProperties());

            } else {
                throw new FileNotFoundException("Driver [" + getDriverName() +
                        "] property files [" + pathToPropertyFile + "] not found");
            }

        } catch (Exception e) {
            LOG.error("Exception during driver initialization ", e);
            throw new RuntimeException("Can't load driver [" + getDriverName() + "]", e);
        }


        LOG.debug("method finished");
    }

    private String getDriverPropertyFilePath() {
        String driverPropertyFile = getDriverName() + DRIVER_PROPERTIES_FILE_SUFFIX;
        String pathToDriverPropertyFile = System.getProperty(driverPropertyFile);
        if (pathToDriverPropertyFile != null) {
            LOG.info("Driver [{}] property file path [{}] has been fetched from system property", getDriverName(), pathToDriverPropertyFile);
            return pathToDriverPropertyFile;
        } else {
            if (getClass().getClassLoader().getResource(driverPropertyFile) != null) {
                pathToDriverPropertyFile = getClass().getClassLoader().getResource(driverPropertyFile).getFile();
                LOG.info("Driver [{}] property file found in class path [{}]", getDriverName(), pathToDriverPropertyFile);
                return pathToDriverPropertyFile;
            }
        }

        LOG.info("No driver [{}] property files found, default will be used", getDriverName());

        URL defaultPropertyFileURL = getClass().getClassLoader().getResource(getDriverName() + DRIVER_DEFAULT_PROPERTIES_FILE_SUFFIX);

        return defaultPropertyFileURL == null ? null : defaultPropertyFileURL.getFile();

    }

    public RemoteWebDriver getRemoteWebDriver() {

        RemoteWebDriver remoteWebDriver = driverBuilder.buildDriver(driverProperties);
        LOG.info("Driver instance [{}] acquired ", getDriverName());

        return remoteWebDriver;
    }


    public void stopService() {
        LOG.debug("method invoked");
        driverBuilder.onFinish();
        LOG.debug("method finished");
    }

    public String getDriverName() {
        return driverName;
    }
}
