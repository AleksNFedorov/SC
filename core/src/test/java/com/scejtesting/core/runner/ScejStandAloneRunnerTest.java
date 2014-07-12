package com.scejtesting.core.runner;

import com.scejtesting.core.Constants;
import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Suite;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.junit.After;
import org.junit.Assert;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.mockito.InOrder;
import sun.jvm.hotspot.utilities.AssertionFailure;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by aleks on 7/4/14.
 */
public class ScejStandAloneRunnerTest {


    @After
    public void finishTest() {
        System.clearProperty(Constants.TESTS_TO_RUN_PROPERTY_KEY);
        System.clearProperty(Constants.SUITE_CONFIG_PROPERTY_KEY);
    }

    @org.junit.Test
    public void testNoExceptions() {

        ScejStandAloneRunner runner = spy(new ScejStandAloneRunner());

        Suite suite = mock(Suite.class);
        Test testOne = mock(Test.class);
        Test testTwo = mock(Test.class);

        when(testOne.getName()).thenReturn("testOne");
        when(testTwo.getName()).thenReturn("testTwo");

        String classPathResource = getClass().getClassLoader().getResource(Constants.DEFAULT_CONFIG_NAME).getFile();

        when(suite.getTests()).thenReturn(Arrays.asList(testOne, testTwo));

        //Default config test
        doReturn(suite).when(runner).initTestSuite(classPathResource);

        Result resultMock = mock(Result.class);
        when(resultMock.getRunCount()).thenReturn(1);
        when(resultMock.wasSuccessful()).thenReturn(true);

        doReturn(resultMock).when(runner).runTest(any(Test.class));

        InOrder inOrder = inOrder(runner);

        Result result = runner.runSuite();

        Assert.assertEquals(2, result.getRunCount());
        Assert.assertTrue(result.wasSuccessful());

        inOrder.verify(runner, calls(2)).runTest(any(Test.class));
    }

    @org.junit.Test
    public void testSuiteRunException() {

        ScejStandAloneRunner runner = spy(new ScejStandAloneRunner());

        doThrow(new AssertionFailure("Suite runner")).when(runner).initTestSuite(anyString());

        Result result = runner.runSuite();

        Assert.assertEquals(0, result.getRunCount());
        Assert.assertFalse(result.wasSuccessful());
        Assert.assertEquals(1, result.getFailureCount());
    }

    @org.junit.Test
    public void testSuiteTestFiltering() {
        System.setProperty(Constants.TESTS_TO_RUN_PROPERTY_KEY, "testOne");

        ScejStandAloneRunner runner = spy(new ScejStandAloneRunner());

        Suite suite = mock(Suite.class);
        Test testOne = mock(Test.class);
        Test testTwo = mock(Test.class);

        when(testOne.getName()).thenReturn("testOne");
        when(testTwo.getName()).thenReturn("testTwo");

        when(suite.getTests()).thenReturn(Arrays.asList(testOne, testTwo));

        //Default config test
        doReturn(suite).when(runner).initTestSuite(anyString());

        Result resultMock = mock(Result.class);
        when(resultMock.getRunCount()).thenReturn(1);
        when(resultMock.wasSuccessful()).thenReturn(true);

        doReturn(resultMock).when(runner).runTest(any(Test.class));

        InOrder inOrder = inOrder(runner);

        Result result = runner.runSuite();

        Assert.assertEquals(1, result.getRunCount());
        Assert.assertTrue(result.wasSuccessful());

        inOrder.verify(runner, calls(1)).runTest(testOne);
        inOrder.verify(runner, never()).runTest(testTwo);


    }

    @org.junit.Test
    public void testUnknownSuiteConfig() {
        ScejStandAloneRunner runner = spy(new ScejStandAloneRunner());

        System.setProperty(Constants.SUITE_CONFIG_PROPERTY_KEY, "/unExistedSuiteConfig.xml");

        Result result = runner.runSuite();

        Assert.assertFalse(result.wasSuccessful());
        Assert.assertEquals(1, result.getFailureCount());
        Assert.assertEquals(RuntimeException.class, result.getFailures().get(0).getException().getClass());
    }

    @org.junit.Test
    public void testFailedTests() {
        ScejStandAloneRunner runner = spy(new ScejStandAloneRunner());

        Suite suite = mock(Suite.class);
        Test testOne = mock(Test.class);
        Test testTwo = mock(Test.class);

        when(testOne.getName()).thenReturn("testOne");
        when(testTwo.getName()).thenReturn("testTwo");

        when(suite.getTests()).thenReturn(Arrays.asList(testOne, testTwo));

        //Default config test
        doReturn(suite).when(runner).initTestSuite(anyString());

        Result successResultMock = mock(Result.class);
        when(successResultMock.getRunCount()).thenReturn(1);
        when(successResultMock.wasSuccessful()).thenReturn(true);
        doReturn(successResultMock).when(runner).runTest(any(Test.class));

        Result failResultMock = mock(Result.class);
        Failure fail = new Failure(Description.createSuiteDescription("test"), new RuntimeException());
        when(failResultMock.getRunCount()).thenReturn(1);
        when(failResultMock.wasSuccessful()).thenReturn(false);
        when(failResultMock.getFailureCount()).thenReturn(1);
        when(failResultMock.getFailures()).thenReturn(Arrays.asList(fail));

        doReturn(successResultMock).when(runner).runTest(testOne);
        doReturn(failResultMock).when(runner).runTest(testTwo);

        InOrder inOrder = inOrder(runner);

        Result result = runner.runSuite();

        Assert.assertEquals(2, result.getRunCount());
        Assert.assertFalse(result.wasSuccessful());
        Assert.assertEquals(1, result.getFailureCount());
        Assert.assertSame(fail, result.getFailures().get(0));

        inOrder.verify(runner, calls(2)).runTest(any(Test.class));
    }

    @org.junit.Test
    public void testCustomConfig() {

        String customConfig = "customscejsuite.xml";
        String classPathResource = getClass().getClassLoader().getResource(customConfig).getFile();

        System.setProperty(Constants.SUITE_CONFIG_PROPERTY_KEY, classPathResource);

        ScejStandAloneRunner runner = spy(new ScejStandAloneRunner());

        Suite suite = mock(Suite.class);
        Test testOne = mock(Test.class);

        when(testOne.getName()).thenReturn("testOne");


        when(suite.getTests()).thenReturn(Arrays.asList(testOne));

        //Default config test
        doReturn(suite).when(runner).initTestSuite(classPathResource);

        Result successResultMock = mock(Result.class);
        when(successResultMock.getRunCount()).thenReturn(1);
        when(successResultMock.wasSuccessful()).thenReturn(true);
        doReturn(successResultMock).when(runner).runTest(any(Test.class));

        doReturn(successResultMock).when(runner).runTest(testOne);

        InOrder inOrder = inOrder(runner);

        Result result = runner.runSuite();

        Assert.assertEquals(1, result.getRunCount());
        Assert.assertTrue(result.wasSuccessful());

        inOrder.verify(runner, calls(1)).runTest(any(Test.class));
    }

    @org.junit.Test
    public void testRunTest() {

        ScejStandAloneRunner runner = spy(new ScejStandAloneRunner());

        Suite suite = mock(Suite.class);
        Test testOne = mock(Test.class);
        Specification specificationMock = mock(Specification.class);

        when(testOne.getName()).thenReturn("testOne");
        when(testOne.getSpecification()).thenReturn(specificationMock);


        when(suite.getTests()).thenReturn(Arrays.asList(testOne));

        //Default config test
        doReturn(suite).when(runner).initTestSuite(anyString());

        Result successResultMock = mock(Result.class);
        when(successResultMock.getRunCount()).thenReturn(1);
        when(successResultMock.wasSuccessful()).thenReturn(true);
        doReturn(successResultMock).when(runner).
                runJUnitTestsForTest(any(TestContext.class));

        Result result = runner.runSuite();

        try {
            new TestContextService().getCurrentTestContext();
            Assert.fail("No context exception expected");
        } catch (RuntimeException ex) {

        }
        Assert.assertEquals(1, result.getRunCount());
        Assert.assertTrue(result.wasSuccessful());


    }
}
