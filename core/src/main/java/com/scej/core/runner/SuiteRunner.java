package com.scej.core.runner;

import com.scej.core.concordion.TestContext;
import com.scej.core.concordion.extension.ScejExtensions;
import com.scej.core.config.SuiteConfiguration;
import com.scej.core.config.Test;
import org.junit.runner.JUnitCore;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class SuiteRunner {

    private void init() {
        addExtensionProperty();
    }

    private void addExtensionProperty() {
        String extensionProp = System.getProperty("concordion.extensions");
        if(extensionProp != null) {
            extensionProp = extensionProp+",";
        } else {
            extensionProp = "";
        }
        extensionProp += ScejExtensions.class.getCanonicalName();
        System.setProperty("concordion.extensions", extensionProp);

    }


    private void runTests(String pathToSpecification) {
        try {
            SuiteConfiguration.initConfiguration(pathToSpecification);
            SuiteConfiguration suiteConfiguration = SuiteConfiguration.getInstance();
            for (Test test : suiteConfiguration.getSuiteTests()) {
                TestContext.createTestContext(test);
                JUnitCore.runClasses(test.getClazz());
                System.out.println("Suite finished");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        } finally {
            System.exit(0);
        }
    }

    public static void main(String... args) {
        SuiteRunner newRunner = new SuiteRunner();
//        newRunner.init();
        newRunner.runTests(args[0]);
    }

}
