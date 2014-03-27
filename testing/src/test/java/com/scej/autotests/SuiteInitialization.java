package com.scejtesting.autotests;

import com.scejtesting.core.WebCoreTest;
import com.scejtesting.core.config.SuiteConfiguration;
import com.scejtesting.core.config.Test;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 * Date: 27.01.14
 * Time: 19:45
 * To change this template use File | Settings | File Templates.
 */
public class SuiteInitialization {


    @BeforeClass
    public static final void initSuite() {
        String file = Thread.currentThread().getContextClassLoader().getResource("testsuite.xml").getFile();
        SuiteConfiguration.initConfiguration(file);
    }

    @Ignore
    @org.junit.Test
    public void testSuiteLocation() {
        Collection<Test> tests = SuiteConfiguration.getInstance().getSuiteTests();
        Assert.assertEquals(2, tests.size());

        Test[] testsArray = tests.toArray(new Test[2]);

        Test firstTest = testsArray[0];
        Test secondTest = testsArray[1];

        Assert.assertNotNull(firstTest.getSpecification());
        Assert.assertNotNull(secondTest.getSpecification());
        Assert.assertEquals(WebCoreTest.class, firstTest.getClazz());
        Assert.assertEquals(String.class, secondTest.getClazz());
    }


}
