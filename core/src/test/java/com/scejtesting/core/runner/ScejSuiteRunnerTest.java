package com.scejtesting.core.runner;

import com.scejtesting.core.Constants;
import com.scejtesting.core.config.Suite;
import com.scejtesting.core.config.Test;
import org.mockito.InOrder;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;

/**
 * User: Fedorovaleks
 * Date: 3/19/14
 */
public class ScejSuiteRunnerTest {

    @org.junit.Test
    public void normalFlowTest() {

        ScejSuiteRunner runner = spy(new ScejSuiteRunner(null));

        Suite suite = mock(Suite.class);
        Test testOne = mock(Test.class);
        Test testTwo = mock(Test.class);

        when(suite.getTests()).thenReturn(Arrays.asList(testOne, testTwo));

        doReturn(suite).when(runner).initTestSuite(anyString());

        InOrder inOrder = inOrder(runner);

        doNothing().when(runner).runTest(any(Test.class));

        runner.run(null);

        inOrder.verify(runner, calls(2)).runTest(any(Test.class));

    }

    @org.junit.Test(expected = RuntimeException.class)
    public void noConfigFileTest() {
        ScejSuiteRunner runner = new ScejSuiteRunner(null);

        System.setProperty(Constants.SUITE_CONFIG_PROPERTY_KEY, "/unExistedSuiteConfig.xml");

        runner.run(null);

    }

    @org.junit.Test
    public void noTestsTest() {
        ScejSuiteRunner runner = spy(new ScejSuiteRunner(null));

        Suite suite = mock(Suite.class);

        when(suite.getTests()).thenReturn(Collections.<Test>emptyList());

        doReturn(suite).when(runner).initTestSuite(anyString());

        InOrder inOrder = inOrder(runner);

        doNothing().when(runner).runTest(any(Test.class));

        runner.run(null);

        inOrder.verify(runner, never()).runTest(any(Test.class));
    }

    @org.junit.Test
    public void excludedTest() {

        ScejSuiteRunner runner = spy(new ScejSuiteRunner(null));

        Suite suite = mock(Suite.class);
        Test testOne = mock(Test.class);
        Test testTwo = mock(Test.class);

        when(testOne.getName()).thenReturn("testOne");
        when(testTwo.getName()).thenReturn("testTwo");

        System.setProperty(Constants.TESTS_TO_RUN_PROPERTY_KEY, "testOne");

        when(suite.getTests()).thenReturn(Arrays.asList(testOne, testTwo));

        doReturn(suite).when(runner).initTestSuite(anyString());

        InOrder inOrder = inOrder(runner);

        doNothing().when(runner).runTest(any(Test.class));

        runner.run(null);

        inOrder.verify(runner, calls(1)).runTest(any(Test.class));

        System.clearProperty(Constants.TESTS_TO_RUN_PROPERTY_KEY);
    }

}
