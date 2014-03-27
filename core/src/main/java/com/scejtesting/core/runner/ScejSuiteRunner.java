package com.scejtesting.core.runner;

import com.scejtesting.core.config.SpecificationLocatorService;
import com.scejtesting.core.config.Suite;
import com.scejtesting.core.config.SuiteConfiguration;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContextService;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class ScejSuiteRunner extends Runner {


    public static final String SCEJ_TESTING_NAME_SPACE = "http://www.scejtesting.com/2014";
    public static final String SUITE_CONFIG_PROPERTY_KEY = "scejtesting.suite";
    public static final String TESTS_TO_RUN_PROPERTY_KEY = "scejtesting.run.tests";
    private static final String DEFAULT_CONFIG_NAME = "scejsuite.xml";

    private static final Logger LOG = LoggerFactory.getLogger(ScejSuiteRunner.class);

    private final Class testClass;
    private Set<String> testsToRun;

    public ScejSuiteRunner(Class targetTestClass) {
        this.testClass = targetTestClass;
    }

    public static void main(String... args) {
        new ScejSuiteRunner(null).run(null);
    }

    private void runTests() {
        LOG.debug("method invoked");
        try {
            String pathToSuiteConfigFile = getPathToConfig();

            LOG.info("Running tests");

            Suite currentSuite = initTestSuite(pathToSuiteConfigFile);

            for (Test test : currentSuite.getTests()) {
                if (needRunTest(test)) {
                    runTest(test);
                    LOG.info("Test [{}] finished", test.getName());
                } else {
                    LOG.info("Test [{}] skipped", test.getName());
                }
            }
        } catch (RuntimeException ex) {
            LOG.error("Exception during test running ", ex.getMessage());
            ex.printStackTrace();
            throw ex;
        } finally {
            LOG.info("Suite successfully finished");
        }
    }

    protected Suite initTestSuite(String pathToSuiteConfigFile) {
        SuiteConfiguration.initConfiguration(pathToSuiteConfigFile);
        SuiteConfiguration suiteConfiguration = SuiteConfiguration.getInstance();

        return suiteConfiguration.getSuite();
    }

    protected void runTest(Test testToRun) {
        buildTestContextService().createNewTestContext(testToRun);
        JUnitCore.runClasses(getSpecificationLocationService().resolveSpecificationClassByContext());
    }

    protected SpecificationLocatorService getSpecificationLocationService() {
        return SpecificationLocatorService.getService();
    }

    protected TestContextService buildTestContextService() {
        return new TestContextService();
    }

    private boolean needRunTest(Test test) {
        return testsToRun == null || testsToRun.contains(test.getName());
    }

    private Set<String> resolveTestsToRunName() {
        LOG.debug("method invoked");
        String testsToRunLine = System.getProperty(TESTS_TO_RUN_PROPERTY_KEY);
        Set<String> testsToRunNames = null;
        if (testsToRunLine != null) {
            LOG.info("Tests to run property found [{}]", testsToRunLine);
            String[] testsToRun = testsToRunLine.split("\\,");
            testsToRunNames = new TreeSet<String>();
            for (String testName : testsToRun) {
                String testNameNoSpaces = testName.trim();
                testsToRunNames.add(testNameNoSpaces);
                LOG.info("Test [{}] has been added to run list ", testNameNoSpaces);
            }
            LOG.info("There are [{}] test in run list", testsToRunNames.size());
        }

        LOG.debug("method finished");
        return testsToRunNames;


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
        testsToRun = resolveTestsToRunName();
        runTests();
    }
}
