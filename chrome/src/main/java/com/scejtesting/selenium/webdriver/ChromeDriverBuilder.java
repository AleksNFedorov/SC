package com.scejtesting.selenium.webdriver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by aleks on 27/3/14.
 */
public class ChromeDriverBuilder extends RemoteWebDriverBuilder {


    private final static Logger LOG = LoggerFactory.getLogger(ChromeDriverBuilder.class);

    private ChromeDriverService service;

    @Override
    public RemoteWebDriver buildDriver(Properties properties) {

        if (service == null) {
            LOG.info("Initializing chrome dirver service");
            startService(properties);
        }

        Capabilities chromeCapabilities = buildFromProperties(properties);

        LOG.info("Chrome capabilities built [{}]", chromeCapabilities.asMap().size());

        RemoteWebDriver driver = new RemoteWebDriver(service.getUrl(), chromeCapabilities);

        LOG.info("Remote web driver instance created");

        return driver;
    }

    private void startService(Properties driverProperties) {

        String pathToDriverBinary = resolveChromeDriverBinaryLocation(driverProperties);

        LOG.debug("Path to chrome driver binary resolved as [{}]", pathToDriverBinary);

        service = new ChromeDriverService.Builder().usingDriverExecutable(new File(pathToDriverBinary)).
                usingAnyFreePort().build();

        LOG.info("Chrome driver service created");

        try {
            service.start();
            LOG.info("Chrome driver service started");

        } catch (IOException e) {
            LOG.error("Exception during Chrome driver service start up", e);
            throw new RuntimeException("Chrome driver service startup exception ", e);
        }

    }

    private String resolveChromeDriverBinaryLocation(Properties driverProperties) {
        return driverProperties.getProperty("webdriver.chrome.driver");
    }

    @Override
    protected DesiredCapabilities getDriverSpecificCapabilities() {
        return DesiredCapabilities.chrome();
    }

    @Override
    public void onFinish() {

        service.stop();

        LOG.info("Chrome driver service stopped");

    }
}
