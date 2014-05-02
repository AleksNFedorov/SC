package com.scejtesting.selenium.webdriver;

import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Properties;

/**
 * Created by aleks on 27/3/14.
 */
public class ScejDriverServiceFactory {

    public static final String DRIVER_PROPERTIES_FILE_SUFFIX = ".driver.properties";
    public static final String DRIVER_DEFAULT_PROPERTIES_FILE_SUFFIX = ".driver.default.properties";


    private final static Logger LOG = LoggerFactory.getLogger(ScejDriverServiceFactory.class);


    public ScejDriverService buildDriverService(String driverName) {
        LOG.debug("init method invoked");

        Check.notNull(driverName, "Driver name must be specified");

        Properties driverProperties = loadDriverProperties(driverName);

        ScejDriverService.saveDriverName(driverName, driverProperties);

        ScejDriverService driverBuilder = createDriverService(driverName, driverProperties);

        LOG.info("init successfully finished");
        LOG.debug("method finished");

        return driverBuilder;
    }


    private ScejDriverService createDriverService(String driverName, Properties driverProperties) {
        LOG.debug("method invoked");

        String builderImplementationClass = driverProperties.getProperty(ScejDriverService.class.getCanonicalName());
        LOG.info("Driver [{}] builder implementation resolved as [{}]", driverName, builderImplementationClass);

        ScejDriverService driverBuilder;
        try {
            Class<ScejDriverService> remoteWebDriverBuilderClass = (Class<ScejDriverService>) Class.forName(builderImplementationClass);
            driverBuilder = (ScejDriverService) remoteWebDriverBuilderClass.getDeclaredConstructors()[0].newInstance(driverProperties);

            LOG.debug("Driver [{}] builder instance successfully created", driverName);

        } catch (Exception ex) {
            LOG.error("Can't instanciate driver [{}] builder ", driverName, ex);
            throw new RuntimeException("Can't instanciate class ", ex);
        }

        LOG.debug("method finished");

        return driverBuilder;
    }


    private Properties loadDriverProperties(String driverName) {
        LOG.debug("method invoked");

        Properties driverProperties = new Properties();

        try {
            String pathToPropertyFile = getDriverPropertyFilePath(driverName);
            LOG.debug("path to driver property file [{}]", pathToPropertyFile);

            if (pathToPropertyFile != null) {
                driverProperties.load(new FileInputStream(pathToPropertyFile));
                driverProperties.putAll(System.getProperties());

            } else {
                throw new FileNotFoundException("Driver [" + driverName +
                        "] property file not found");
            }

        } catch (Exception e) {
            LOG.error("Exception during driver initialization ", e);
            throw new RuntimeException("Can't load driver [" + driverName + "]", e);
        }


        LOG.debug("method finished");

        return driverProperties;
    }

    private String getDriverPropertyFilePath(String driverName) {
        String driverPropertyFile = driverName + DRIVER_PROPERTIES_FILE_SUFFIX;
        String pathToDriverPropertyFile = System.getProperty(driverPropertyFile);
        if (pathToDriverPropertyFile != null) {
            LOG.info("Driver [{}] property file path [{}] has been fetched from system property", driverName, pathToDriverPropertyFile);
            return pathToDriverPropertyFile;
        } else {
            if (getClass().getClassLoader().getResource(driverPropertyFile) != null) {
                pathToDriverPropertyFile = getClass().getClassLoader().getResource(driverPropertyFile).getFile();
                LOG.info("Driver [{}] property file found in class path [{}]", driverName, pathToDriverPropertyFile);
                return pathToDriverPropertyFile;
            }
        }

        LOG.info("No driver [{}] property files found, default will be used", driverName);

        URL defaultPropertyFileURL = getClass().getClassLoader().getResource(driverName + DRIVER_DEFAULT_PROPERTIES_FILE_SUFFIX);

        return defaultPropertyFileURL == null ? null : defaultPropertyFileURL.getFile();

    }

}
