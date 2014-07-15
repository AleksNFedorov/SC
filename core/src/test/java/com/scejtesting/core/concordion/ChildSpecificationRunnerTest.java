package com.scejtesting.core.concordion;

import com.scejtesting.core.CoreTestFixture;
import com.scejtesting.core.config.*;
import com.scejtesting.core.context.SpecificationResultRegistry;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.Resource;
import org.concordion.api.Result;
import org.concordion.api.RunnerResult;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class ChildSpecificationRunnerTest {


    private Test mockTest;
    private Specification mockSpecification;
    private Suite mockSuite;
    private ChildSpecificationRunner runnerSpy;

    @Before
    public void initTest() {
        Specification specification = mock(Specification.class);
        when(specification.getLocation()).thenReturn("/rootSpecificaiton.html");

        Test test = mock(Test.class);
        when(test.getSpecification()).thenReturn(specification);
        when(test.getDefaultTestClass()).thenCallRealMethod();

        Suite suite = mock(Suite.class);
        List<Test> tests = Arrays.asList(test);
        when(suite.getTests()).thenReturn(tests);

        new TestContextService().createNewTestContext(test);

        ChildSpecificationRunner runner = spy(new ChildSpecificationRunner());

        doReturn(suite).when(runner).getSuite();
        doReturn(specification).when(runner).resolveSpecification(anyString());

        mockTest = test;
        mockSpecification = specification;
        mockSuite = suite;
        runnerSpy = runner;

    }

    @After
    public void finishTest() {
        TestContextService service = new TestContextService();
        TestContext currentContext = service.getCurrentTestContext();
        service.dropContext(currentContext.getContextId());
    }

    @org.junit.Test
    public void runnerInitializationTest() {
        new TestContextService().waitForInitialization();
    }

    @org.junit.Test
    public void saveResultOnException() throws Exception {

        when(mockSuite.getThrownException()).thenReturn(null);
        when(mockTest.getThrownException()).thenReturn(null);

        doThrow((new IllegalStateException("Thrown for test purpose"))).when(runnerSpy).
                executeSpecification(eq(mockSpecification),
                        any(Resource.class), anyString());

        try {
            runnerSpy.execute(new Resource("/somePath"), "Some href");
            Assert.fail();
        } catch (IllegalStateException ex) {

        }

        checkResults(0, 0, 0, 0);

    }

    @org.junit.Test
    public void positiveFlow() throws Exception {

        when(mockSuite.getThrownException()).thenReturn(null);
        when(mockTest.getThrownException()).thenReturn(null);

        RunnerResult successResult = new RunnerResult(Result.SUCCESS);

        doReturn(successResult).when(runnerSpy).executeSpecification(any(Specification.class), any(Resource.class), anyString());

        RunnerResult executionResult = runnerSpy.execute(new Resource("/somePath"), "Some href");

        Assert.assertEquals(successResult, executionResult);

        checkResults(0, 0, 0, 0);
    }

    private void checkResults(int success, int fail, int ignore, int exception) {
        TestContext context = new TestContextService().getCurrentTestContext();
        SpecificationResultRegistry registry = context.getCurrentSpecificationContext().getResultRegistry();
        Assert.assertEquals(success, registry.getSuccessCount());
        Assert.assertEquals(fail, registry.getIgnoredCount());
        Assert.assertEquals(ignore, registry.getFailureCount());
        Assert.assertEquals(exception, registry.getExceptionCount());
    }

    @org.junit.Test
    public void suiteFailFastException() throws Exception {

        when(mockSuite.getThrownException()).thenReturn(new RuntimeException());
        when(mockTest.getThrownException()).thenReturn(null);

        RunnerResult successResult = new RunnerResult(Result.SUCCESS);

        doReturn(successResult).when(runnerSpy).executeSpecification(any(Specification.class), any(Resource.class), anyString());

        RunnerResult executionResult = runnerSpy.execute(new Resource("/somePath"), "Some href");

        Assert.assertEquals(Result.IGNORED, executionResult.getResult());

        checkResults(0, 0, 0, 0);

    }


    @org.junit.Test
    public void unknownSpecification() throws Exception {

        when(mockSuite.getThrownException()).thenReturn(null);
        when(mockTest.getThrownException()).thenReturn(null);

        RunnerResult successResult = new RunnerResult(Result.SUCCESS);

        doReturn(null).when(runnerSpy).resolveSpecification(anyString());
        doReturn(successResult).when(runnerSpy).executeSpecification(any(Specification.class), any(Resource.class), anyString());

        RunnerResult executionResult = runnerSpy.execute(new Resource("/somePath"), "Some href");

        Assert.assertEquals(Result.IGNORED, executionResult.getResult());
    }

    @org.junit.Test
    public void testFailFastException() throws Exception {

        when(mockSuite.getThrownException()).thenReturn(null);
        when(mockTest.getThrownException()).thenReturn(new RuntimeException());

        RunnerResult successResult = new RunnerResult(Result.SUCCESS);

        doReturn(successResult).when(runnerSpy).executeSpecification(any(Specification.class), any(Resource.class), anyString());

        RunnerResult executionResult = runnerSpy.execute(new Resource("/somePath"), "Some href");

        Assert.assertEquals(Result.IGNORED, executionResult.getResult());

    }

    @org.junit.Test
    public void testIncludeAll() {

        when(mockTest.getDefaultTestClass()).thenReturn(CoreTestFixture.class);

        String childSpecificationOrig = "childSpecification.html";

        doCallRealMethod().when(runnerSpy).resolveSpecification(anyString());

        Specification specification = runnerSpy.resolveSpecification(
                SpecificationTest.appendUniqueSuffix(childSpecificationOrig));

        Assert.assertEquals(childSpecificationOrig, specification.getLocation());
    }

    @org.junit.Test
    public void testExcludeOnlyAll() {

        Specification fakeSpecificationTwo = new Specification("FakeTwo.html");
        Specification fakeSpecificationThree = new Specification("FakeThree.html");

        Excludes excludes = new Excludes(Arrays.asList(fakeSpecificationTwo, fakeSpecificationThree));

        when(mockSpecification.getExcludes()).thenReturn(excludes);

        String notExcludedSpecPath = "someNotExludedSpec.html";

        doCallRealMethod().when(runnerSpy).resolveSpecification(anyString());

        Specification currentSpec = runnerSpy.resolveSpecification(SpecificationTest.appendUniqueSuffix(notExcludedSpecPath));

        Assert.assertNotNull("Not excluded spec can't be null", currentSpec);
        Assert.assertEquals(notExcludedSpecPath, currentSpec.getLocation());

        currentSpec = runnerSpy.resolveSpecification("FakeTwo.html");
        Assert.assertNull("Excluded spec must be null", currentSpec);

        currentSpec = runnerSpy.resolveSpecification("FakeThree.html");
        Assert.assertNull("Excluded spec must be null", currentSpec);

    }

    @org.junit.Test
    public void testIncludeOnly() {

        Specification fakeSpecificationOne = new Specification("FakeOne.html");
        Specification fakeSpecificationTwo = new Specification("FakeTwo.html");
        Specification fakeSpecificationThree = new Specification("FakeThree.html");

        Includes includes = new Includes(Arrays.asList(fakeSpecificationOne,
                fakeSpecificationTwo,
                fakeSpecificationThree));

        doCallRealMethod().when(runnerSpy).resolveSpecification(anyString());

        when(mockSpecification.getIncludes()).thenReturn(includes);

        Specification currentSpec = runnerSpy.resolveSpecification("someSpec.html");
        Assert.assertNull("Not included spec must be null", currentSpec);

        String specLocationForTest = "FakeTwo.html";
        currentSpec = runnerSpy.resolveSpecification(SpecificationTest.appendUniqueSuffix(specLocationForTest));
        Assert.assertNotNull("Included spec can't be null", currentSpec);
        Assert.assertEquals(specLocationForTest, currentSpec.getLocation());

        specLocationForTest = "FakeOne.html";
        currentSpec = runnerSpy.resolveSpecification(SpecificationTest.appendUniqueSuffix(specLocationForTest));
        Assert.assertNotNull("Included spec can't be null", currentSpec);
        Assert.assertEquals(specLocationForTest, currentSpec.getLocation());

        specLocationForTest = "FakeThree.html";
        currentSpec = runnerSpy.resolveSpecification(SpecificationTest.appendUniqueSuffix(specLocationForTest));
        Assert.assertNotNull("Included spec can't be null", currentSpec);
        Assert.assertEquals(specLocationForTest, currentSpec.getLocation());
    }

    @org.junit.Test
    public void testIncludeExclude() {
        Specification fakeSpecificationOne = new Specification("FakeOne.html");
        Specification fakeSpecificationTwo = new Specification("FakeTwo.html");
        Specification fakeSpecificationThree = new Specification("FakeThree.html");

        Includes includes = new Includes(Arrays.asList(fakeSpecificationOne,
                fakeSpecificationTwo));
        Excludes excludes = new Excludes(Arrays.asList(fakeSpecificationOne, fakeSpecificationThree));

        when(mockSpecification.getExcludes()).thenReturn(excludes);
        when(mockSpecification.getIncludes()).thenReturn(includes);

        doCallRealMethod().when(runnerSpy).resolveSpecification(anyString());

        Specification currentSpec = runnerSpy.resolveSpecification("someNotExludedSpec.html");
        Assert.assertNull("Not included spec must be null", currentSpec);

        String currentSpecLocation = "FakeTwo.html";
        currentSpec = runnerSpy.resolveSpecification(SpecificationTest.appendUniqueSuffix(currentSpecLocation));
        Assert.assertNotNull("Included spec can't be null", currentSpec);
        Assert.assertEquals(currentSpecLocation, currentSpec.getLocation());

        currentSpec = runnerSpy.resolveSpecification("FakeOne.html");
        Assert.assertNull("Excluded spec must be null", currentSpec);

        currentSpec = runnerSpy.resolveSpecification("FakeThree.html");
        Assert.assertNull("Excluded spec must be null", currentSpec);

    }

    @org.junit.Test
    public void findTestClassFromSpecification() throws ClassNotFoundException {

        when(mockTest.getDefaultTestClass()).thenReturn(Integer.class);
        when(mockSpecification.getTestClass()).thenReturn(String.class);

        runnerSpy.specification = mockSpecification;

        Class testClass = runnerSpy.findTestClass(null, null);

        Assert.assertEquals(String.class, testClass);
    }

    @org.junit.Test
    public void findTestClassFromTest() throws ClassNotFoundException {

        when(mockTest.getDefaultTestClass()).thenReturn(Integer.class);

        when(mockSpecification.getTestClass()).thenReturn(null);

        runnerSpy.specification = mockSpecification;

        Class testClass = runnerSpy.findTestClass(null, null);

        Assert.assertEquals(Integer.class, testClass);
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void unknownTestClass() throws ClassNotFoundException {

        SpecificationLocatorService serviceMock = mock(SpecificationLocatorService.class);
        when(serviceMock.resolveSpecificationClassByContext(any(Specification.class), any(Test.class))).
                thenThrow(RuntimeException.class);

        ChildSpecificationRunner runner = spy(new ChildSpecificationRunner());
        doReturn(serviceMock).when(runner).getSpecificationLocationService();
        runner.findTestClass(null, null);
    }

}
