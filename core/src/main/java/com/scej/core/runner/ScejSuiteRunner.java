package com.scej.core.runner;

import com.scej.core.TestContext;
import com.scej.core.concordion.ChildSpecificationRunner;
import com.scej.core.config.SuiteConfiguration;
import com.scej.core.config.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class ScejSuiteRunner extends Runner {

    private static final String SUITE_CONFIG_PROPERTY_KEY = "scej.suite";
    private static final String DEFAULT_CONFIG_NAME = "scejsuite.xml";

    private static final Logger LOG = LoggerFactory.getLogger(ScejSuiteRunner.class);

    private final Class testClass;

    public ScejSuiteRunner(Class targetTestClass) {
        this.testClass = targetTestClass;
    }

    private void runTests() {
        LOG.debug("method invoked");
        try {
            String pathToSuiteConfigFile = getPathToConfig();

            LOG.info("Running tests");

            SuiteConfiguration.initConfiguration(pathToSuiteConfigFile);
            SuiteConfiguration suiteConfiguration = SuiteConfiguration.getInstance();
            for (Test test : suiteConfiguration.getSuite().getTests()) {
                TestContext.createTestContext(test);
                JUnitCore.runClasses(ChildSpecificationRunner.resolveSpecificationClassByContext());
                System.out.println("Suite finished");
            }
        } catch (Exception ex) {
            LOG.error("Exception during test running ", ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        } finally {
            System.exit(0);
        }
    }

    private String getPathToConfig() {
        LOG.debug("method invoked");
        String pathToConfig = System.getProperty(SUITE_CONFIG_PROPERTY_KEY);
        LOG.info("Getting path to config from system property");
        if (pathToConfig == null) {
            LOG.info("No system property with path to config");
            URL classPathResource = getClass().getClassLoader().getResource(DEFAULT_CONFIG_NAME);
            if (classPathResource != null) {
                pathToConfig = classPathResource.getFile();
            } else {
                LOG.error("Can't resolve path to suite configuration file");
                throw new RuntimeException("Suite config not found, put [" + DEFAULT_CONFIG_NAME + "] to classpath or " +
                        "specify [" + SUITE_CONFIG_PROPERTY_KEY + "] system property");
            }
        }
        LOG.info("Suite configuration file [{}]", pathToConfig);
        LOG.debug("method finished");
        return pathToConfig;
    }

    @Override
    public Description getDescription() {
        return Description.createSuiteDescription(testClass);
    }

    @Override
    public void run(RunNotifier notifier) {
        runTests();
    }

    public static void main(String... args) {
        new ScejSuiteRunner(null).runTests();
    }
}
