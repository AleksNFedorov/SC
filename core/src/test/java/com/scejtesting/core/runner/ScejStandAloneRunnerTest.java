package com.scejtesting.core.runner;

import com.scejtesting.core.Constants;
import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Suite;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.SpecificationResultRegistry;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.ResultSummary;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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

    private ScejStandAloneRunner runnerSpy;
    private Suite mockSuite;
    private Test mockTest;
    private ResultSummary testResultSummary;
    private InOrder inOrder;

    @Before
    public void initTest() {
        runnerSpy = spy(new ScejStandAloneRunner());

        SpecificationResultRegistry resultSummary = new SpecificationResultRegistry();

        resultSummary.addResult(new ResultSummaryAdapter(1, 2, 3, 4));
        resultSummary.processResults();

        this.testResultSummary = resultSummary;

        doReturn(testResultSummary).when(runnerSpy).runTest(any(Test.class));

        mockSuite = mock(Suite.class);
        mockTest = mock(Test.class);

        when(mockTest.getName()).thenReturn("testOne");
        when(mockSuite.getTests()).thenReturn(Arrays.asList(mockTest));

        //Default config test
        doReturn(mockSuite).when(runnerSpy).initTestSuite(anyString());

        inOrder = inOrder(runnerSpy);
    }

    @After
    public void finishTest() {
        System.clearProperty(Constants.TESTS_TO_RUN_PROPERTY_KEY);
        System.clearProperty(Constants.SUITE_CONFIG_PROPERTY_KEY);
    }

    @org.junit.Test
    public void testNoExceptions() {
        Test testTwo = mock(Test.class);
        when(testTwo.getName()).thenReturn("testTwo");

        when(mockSuite.getTests()).thenReturn(Arrays.asList(mockTest, testTwo));

        InOrder inOrder = inOrder(runnerSpy);

        ResultSummary result = runnerSpy.runSuite();

        Assert.assertEquals(2, result.getSuccessCount());
        Assert.assertEquals(4, result.getFailureCount());
        Assert.assertEquals(6, result.getExceptionCount());
        Assert.assertEquals(8, result.getIgnoredCount());

        inOrder.verify(runnerSpy, calls(2)).runTest(any(Test.class));
    }

    @org.junit.Test
    public void testRunnerException() {

        doThrow(new AssertionFailure("Suite runner")).when(runnerSpy).initTestSuite(anyString());

        ResultSummary result = runnerSpy.runSuite();

        Assert.assertEquals(0, result.getSuccessCount());
        Assert.assertEquals(0, result.getFailureCount());
        Assert.assertEquals(1, result.getExceptionCount());
        Assert.assertEquals(0, result.getIgnoredCount());
    }

    @org.junit.Test
    public void testSuiteTestFiltering() {

        System.setProperty(Constants.TESTS_TO_RUN_PROPERTY_KEY, "testOne");

        initTest();

        Test testTwo = mock(Test.class);

        when(testTwo.getName()).thenReturn("testTwo");

        when(mockSuite.getTests()).thenReturn(Arrays.asList(mockTest, testTwo));


        ResultSummary result = runnerSpy.runSuite();

        Assert.assertEquals(1, result.getSuccessCount());
        Assert.assertEquals(2, result.getFailureCount());
        Assert.assertEquals(3, result.getExceptionCount());
        Assert.assertEquals(4, result.getIgnoredCount());

        inOrder.verify(runnerSpy, calls(1)).runTest(mockTest);
        inOrder.verify(runnerSpy, never()).runTest(testTwo);


    }

    @org.junit.Test
    public void testUnknownSuiteConfig() {
        System.setProperty(Constants.SUITE_CONFIG_PROPERTY_KEY, "/unExistedSuiteConfig.xml");

        ResultSummary result = runnerSpy.runSuite();

        Assert.assertEquals(0, result.getSuccessCount());
        Assert.assertEquals(0, result.getFailureCount());
        Assert.assertEquals(1, result.getExceptionCount());
        Assert.assertEquals(0, result.getIgnoredCount());
    }

    @org.junit.Test
    public void testCustomConfig() {

        String customConfig = "customscejsuite.xml";
        String classPathResource = getClass().getClassLoader().getResource(customConfig).getFile();

        System.setProperty(Constants.SUITE_CONFIG_PROPERTY_KEY, classPathResource);

        ResultSummary result = runnerSpy.runSuite();

        Assert.assertEquals(1, result.getSuccessCount());
        Assert.assertEquals(2, result.getFailureCount());
        Assert.assertEquals(3, result.getExceptionCount());
        Assert.assertEquals(4, result.getIgnoredCount());
    }

    @org.junit.Test
    public void testRunTest() {

        Specification specificationMock = mock(Specification.class);
        when(mockTest.getSpecification()).thenReturn(specificationMock);

        doReturn(testResultSummary).when(runnerSpy).
                runJUnitTestsForTest(any(TestContext.class));

        ResultSummary result = runnerSpy.runSuite();

        try {
            new TestContextService().getCurrentTestContext();
            Assert.fail("No context exception expected");
        } catch (RuntimeException ex) {

        }

        Assert.assertEquals(1, result.getSuccessCount());
        Assert.assertEquals(2, result.getFailureCount());
        Assert.assertEquals(3, result.getExceptionCount());
        Assert.assertEquals(4, result.getIgnoredCount());
    }
}
