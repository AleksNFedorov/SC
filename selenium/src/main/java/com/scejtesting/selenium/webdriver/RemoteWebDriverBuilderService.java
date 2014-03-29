package com.scejtesting.selenium.webdriver;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * Created by aleks on 27/3/14.
 */
public final class RemoteWebDriverBuilderService {

    public static final String DRIVER_PROPERTIES_FILE_SUFFIX = ".driver.properties";
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

        File driverPropertyFile = new File(pathToPropertyFile);

        try {
            if (driverPropertyFile.exists()) {
                driverProperties.load(new FileInputStream(driverPropertyFile));
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
        String pathToDriverPropertyFiles = System.getProperty(driverPropertyFile);
        if (pathToDriverPropertyFiles != null) {
            LOG.info("Driver [{}] property file path [{}] has been fetched from system property", getDriverName(), pathToDriverPropertyFiles);
            return pathToDriverPropertyFiles;
        }
        return getClass().getClassLoader().getResource(driverPropertyFile).getFile();
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
