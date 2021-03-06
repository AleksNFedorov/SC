package com.scejtesting.core.runner;

import com.scejtesting.core.config.SuiteConfiguration;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.integration.GlobalTestContext;
import org.junit.runner.JUnitCore;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 * Date: 03.01.14
 * Time: 11:56
 * To change this template use File | Settings | File Templates.
 */
public class SuiteRunner {

    public static void main(String... args) {
        try {
            SuiteConfiguration.initConfiguration(args[0]);
            SuiteConfiguration suiteConfiguration = SuiteConfiguration.getInstance();
            for (Test test : suiteConfiguration.getSuiteTests()) {
                GlobalTestContext.createGlobalContext(test);
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
