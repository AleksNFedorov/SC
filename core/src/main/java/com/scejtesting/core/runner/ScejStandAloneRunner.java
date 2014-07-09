package com.scejtesting.core.runner;

import com.scejtesting.core.Constants;
import com.scejtesting.core.Utils;
import com.scejtesting.core.config.SpecificationLocatorService;
import com.scejtesting.core.config.Suite;
import com.scejtesting.core.config.SuiteConfiguration;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by aleks on 7/4/14.
 */
public class ScejStandAloneRunner {

    private static final Logger LOG = LoggerFactory.getLogger(ScejStandAloneRunner.class);

    private final Set<String> testsToRun;

    private final Result suiteResult;
    private final RunListener suiteResultRunListener;

    public ScejStandAloneRunner() {
        suiteResult = new Result();
        suiteResultRunListener = suiteResult.createListener();
        testsToRun = resolveTestsToRunName();
    }

    public Result runSuite() {
        LOG.debug("method invoked");

        Description suiteDescription = Description.createSuiteDescription(this.getClass());

        try {
            suiteResultRunListener.testRunStarted(suiteDescription);

            String pathToSuiteConfigFile = getPathToConfig();

            LOG.info("Running tests");

            Suite currentSuite = initTestSuite(pathToSuiteConfigFile);

            runSuiteTests(currentSuite);

            suiteResultRunListener.testRunFinished(suiteResult);
        } catch (Throwable ex) {
            LOG.error("Exception during test running ", ex.getMessage());
            ex.printStackTrace();
            suiteResultRunListener.testFailure(new Failure(suiteDescription, ex));
        } finally {
            LOG.info("Suite finished");
            return suiteResult;
        }
    }

    Suite initTestSuite(String pathToSuiteConfigFile) {
        SuiteConfiguration.initConfiguration(pathToSuiteConfigFile);
        SuiteConfiguration suiteConfiguration = SuiteConfiguration.getInstance();
        return suiteConfiguration.getSuite();
    }

    private void runSuiteTests(Suite currentSuite) throws Exception {
        Result testResult;
        for (Test test : currentSuite.getTests()) {
            if (needRunTest(test)) {
                testResult = runTest(test);
                LOG.info("Test [{}] finished with success [{}]", test.getName(), testResult.wasSuccessful());
                mergeResult(test, testResult);
            } else {
                LOG.info("Test [{}] skipped", test.getName());
            }
        }
    }

    Result runTest(Test testToRun) {
        TestContextService testContextService = buildTestContextService();

        TestContext testContext = testContextService.createNewTestContext(testToRun);
        LOG.info("Test context created");

        Result result = runJUnitTestsForTest(testToRun);

        testContext.destroyTestContext();
        LOG.info("Test context destroyed");

        return result;
    }

    Result runJUnitTestsForTest(Test testToRun) {
        return JUnitCore.runClasses(
                getSpecificationLocationService().
                        resolveSpecificationClassByContext(testToRun.getSpecification(), testToRun)
        );
    }

    private boolean needRunTest(Test test) {
        return testsToRun == null || testsToRun.contains(test.getName());
    }

    private Set<String> resolveTestsToRunName() {
        LOG.debug("method invoked");
        String testsToRunLine = System.getProperty(Constants.TESTS_TO_RUN_PROPERTY_KEY);
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

        String pathToConfig = System.getProperty(Constants.SUITE_CONFIG_PROPERTY_KEY);
        if (pathToConfig != null) {
            String fullPathConfig = new Utils().resolveResourcePath(pathToConfig);
            if (fullPathConfig != null) {
                LOG.info("Config have been taken from system property");
                return fullPathConfig;
            }
        } else {
            LOG.info("No system property with path to config");
            URL classPathResource = getClass().getClassLoader().getResource(Constants.DEFAULT_CONFIG_NAME);
            if (classPathResource != null) {
                pathToConfig = classPathResource.getFile();
                LOG.info("Config have been taken from system property");
                return pathToConfig;
            }
        }
        LOG.error("Can't resolve path to suite configuration file");
        throw new RuntimeException("Suite config not found, put [" + Constants.DEFAULT_CONFIG_NAME + "] to classpath or " +
                "specify [" + Constants.SUITE_CONFIG_PROPERTY_KEY + "] system property");
    }

    private void mergeResult(Test test, Result testResult) throws Exception {

        LOG.debug("merging test results [{}][{}]", test.getName(), testResult);

        Description testDescription = Description.createSuiteDescription(test.getName());

        //Add run count
        for (int i = 0; i < testResult.getRunCount(); ++i) {
            suiteResultRunListener.testFinished(testDescription);
        }
        LOG.info("[{}] tests run added to results ", testResult.getRunCount());

        //merge fails
        if (!testResult.wasSuccessful()) {
            for (Failure failure : testResult.getFailures()) {
                suiteResultRunListener.testFailure(failure);
            }
        }
        LOG.info("[{}] tests fails added to result", testResult.getFailureCount());
        LOG.debug("Test [{}] result result merge finished ", test.getName());
    }

    protected SpecificationLocatorService getSpecificationLocationService() {
        return new SpecificationLocatorService();
    }

    protected TestContextService buildTestContextService() {
        return new TestContextService();
    }

    public static void main(String... args) {
        Result result = new ScejStandAloneRunner().runSuite();
        System.exit(result.getFailureCount() == 0 ? 0 : 1);
    }


}
