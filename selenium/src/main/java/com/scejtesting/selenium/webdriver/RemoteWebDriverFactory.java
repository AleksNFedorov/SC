package com.scejtesting.selenium.webdriver;

import org.concordion.internal.util.Check;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by aleks on 27/3/14.
 */
public class RemoteWebDriverFactory {

    private final static Logger LOG = LoggerFactory.getLogger(RemoteWebDriverFactory.class);


    private Map<String, RemoteWebDriverBuilderService> allDriverBuilderServices = new TreeMap<String, RemoteWebDriverBuilderService>();

    public RemoteWebDriverFactory() {
        registerStopAllServicesOnExit();
    }

    private void registerStopAllServicesOnExit() {
        LOG.debug("method invoked");

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                LOG.debug("Stopping driver builder services");
                for (Map.Entry<String, RemoteWebDriverBuilderService> builderServiceEntry : allDriverBuilderServices.entrySet()) {
                    builderServiceEntry.getValue().stopService();
                    LOG.info("Driver service [{}] has been stopped", builderServiceEntry.getKey());

                }

                LOG.debug("[{}] services were stopped ", allDriverBuilderServices.size());
            }
        }));

        LOG.debug("method finished");
    }

    public RemoteWebDriver buildRemoteWebDriver(String driverName) {
        LOG.debug("method invoked");

        Check.notEmpty(driverName, "Driver name can't be empty");


        RemoteWebDriverBuilderService builderService = allDriverBuilderServices.get(driverName);
        if (builderService == null) {
            LOG.info("Driver [{}] not initialized yet", driverName);
            builderService = createDriverBuilderService(driverName);

            allDriverBuilderServices.put(driverName, builderService);

            LOG.debug("Driver [{}] builder service initialized ", driverName);
        }

        LOG.debug("method finished");
        return builderService.getRemoteWebDriver();
    }

    RemoteWebDriverBuilderService createDriverBuilderService(String driverName) {
        LOG.debug("method invoked");

        RemoteWebDriverBuilderService newDriverBuilderService = new RemoteWebDriverBuilderService(driverName);
        newDriverBuilderService.init();


        LOG.debug("method finished");

        return newDriverBuilderService;
    }


}
