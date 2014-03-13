package com.scej.core.runner;

import com.scej.core.concordion.TestContext;
import com.scej.core.config.SuiteConfiguration;
import com.scej.core.config.Test;
import org.junit.runner.JUnitCore;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class SuiteRunner {

    public static void main(String... args) {
        try {
            SuiteConfiguration.initConfiguration(args[0]);
            SuiteConfiguration suiteConfiguration = SuiteConfiguration.getInstance();
            for (Test test : suiteConfiguration.getSuiteTests()) {
                TestContext.createTestContext(test);
                JUnitCore.runClasses(test.getClazz());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        } finally {
            System.exit(0);
        }
    }

}
