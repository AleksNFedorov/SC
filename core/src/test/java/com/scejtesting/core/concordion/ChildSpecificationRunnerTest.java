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
        service.revertContextSwitch();
        TestContext currentContext = service.getCurrentTestContext();
        service.dropContext(currentContext);
    }

    @org.junit.Test
    public void testResultsSaved_specificationExecutionException() throws Exception {

        when(mockSuite.getThrownException()).thenReturn(null);
        when(mockTest.getThrownException()).thenReturn(null);

        TestContextService service = new TestContextService();
        TestContext currentTestContext = service.getCurrentTestContext();
        TestContext clonedContext = service.cloneContext(currentTestContext);
        service.switchContext(clonedContext);

        doThrow((new IllegalStateException("Thrown for test purpose"))).when(runnerSpy).
                executeSpecification(eq(mockSpecification),
                        any(Resource.class), anyString());

        runnerSpy.execute(new Resource("/somePath"), "Some href");

        Assert.assertSame(currentTestContext, service.getCurrentTestContext());
        checkResults(0, 0, 0, 1);
    }

    @org.junit.Test
    public void testResultsSaved_findTestClassException() throws Exception {

        when(mockSuite.getThrownException()).thenReturn(null);
        when(mockTest.getThrownException()).thenReturn(null);

        TestContextService service = new TestContextService();
        TestContext currentTestContext = service.getCurrentTestContext();
        TestContext clonedContext = service.cloneContext(currentTestContext);
        service.switchContext(clonedContext);

        SpecificationLocatorService serviceMock = spy(new SpecificationLocatorService());

        doThrow((new IllegalStateException("Thrown for test purpose"))).when(serviceMock).
                resolveSpecificationClassByContext(any(Specification.class), any(Test.class));

        doReturn(serviceMock).when(runnerSpy).getSpecificationLocationService();

        runnerSpy.execute(new Resource("/somePath"), "Some href");

        Assert.assertSame(currentTestContext, service.getCurrentTestContext());
        checkResults(0, 0, 0, 1);
    }

    @org.junit.Test
    public void testResultsSaved_successResult() throws Exception {

        when(mockSuite.getThrownException()).thenReturn(null);
        when(mockTest.getThrownException()).thenReturn(null);

        TestContextService service = new TestContextService();
        TestContext currentTestContext = service.getCurrentTestContext();
        TestContext clonedContext = service.cloneContext(currentTestContext);
        service.switchContext(clonedContext);

        RunnerResult successResult = new RunnerResult(Result.SUCCESS);

        doReturn(successResult).when(runnerSpy).executeSpecificationParent(any(Resource.class), anyString());

        RunnerResult executionResult = runnerSpy.execute(new Resource("/somePath"), "Some href");

        Assert.assertEquals(successResult, executionResult);
        Assert.assertSame(clonedContext, service.getCurrentTestContext());

        checkResults(-1, 0, 0, 0);
    }

    @org.junit.Test
    public void testIgnoredResults_canRunSpecificationOnSuiteException() throws Exception {

        when(mockSuite.getThrownException()).thenReturn(new RuntimeException());
        when(mockTest.getThrownException()).thenReturn(null);

        TestContextService service = new TestContextService();
        TestContext currentTestContext = service.getCurrentTestContext();
        TestContext clonedContext = service.cloneContext(currentTestContext);
        service.switchContext(clonedContext);

        RunnerResult executionResult = runnerSpy.execute(new Resource("/somePath"), "Some href");

        Assert.assertEquals(Result.IGNORED, executionResult.getResult());
        Assert.assertSame(currentTestContext, service.getCurrentTestContext());

        checkResults(0, 0, 1, 0);
    }

    @org.junit.Test
    public void testIgnoredResults_unknownSpecification() throws Exception {

        when(mockSuite.getThrownException()).thenReturn(null);
        when(mockTest.getThrownException()).thenReturn(null);

        TestContextService service = new TestContextService();
        TestContext currentTestContext = service.getCurrentTestContext();
        TestContext clonedContext = service.cloneContext(currentTestContext);
        service.switchContext(clonedContext);

        doReturn(null).when(runnerSpy).resolveSpecification(anyString());
        RunnerResult executionResult = runnerSpy.execute(new Resource("/somePath"), "Some href");

        Assert.assertSame(currentTestContext, service.getCurrentTestContext());
        Assert.assertEquals(Result.IGNORED, executionResult.getResult());

        checkResults(0, 0, 1, 0);
    }

    @org.junit.Test
    public void testIgnoredResults_testFailFastException() throws Exception {

        when(mockSuite.getThrownException()).thenReturn(null);
        when(mockTest.getThrownException()).thenReturn(new RuntimeException());

        TestContextService service = new TestContextService();
        TestContext currentTestContext = service.getCurrentTestContext();
        TestContext clonedContext = service.cloneContext(currentTestContext);
        service.switchContext(clonedContext);

        RunnerResult executionResult = runnerSpy.execute(new Resource("/somePath"), "Some href");

        Assert.assertEquals(Result.IGNORED, executionResult.getResult());

        Assert.assertEquals(Result.IGNORED, executionResult.getResult());

        checkResults(0, 0, 1, 0);
    }

    @org.junit.Test
    public void testSpecificationResolved_norExcludesOrIncludes() {

        when(mockTest.getDefaultTestClass()).thenReturn(CoreTestFixture.class);

        String childSpecificationOrig = "childSpecification.html";

        doCallRealMethod().when(runnerSpy).resolveSpecification(anyString());

        Specification specification = runnerSpy.resolveSpecification(
                SpecificationTest.appendUniqueSuffix(childSpecificationOrig));

        Assert.assertEquals(childSpecificationOrig, specification.getLocation());
    }

    @org.junit.Test
    public void testSpecificationResolved_excludesOnly() {

        Specification fakeSpecificationTwo = new Specification("FakeTwo.html");
        Specification fakeSpecificationThree = new Specification("FakeThree.html");

        Excludes excludes = new Excludes(Arrays.asList(fakeSpecificationTwo, fakeSpecificationThree));

        when(mockSpecification.getExcludes()).thenReturn(excludes);

        String notExcludedSpecPath = "someNotExludedSpec.html";

        doCallRealMethod().when(runnerSpy).resolveSpecification(anyString());

        Specification currentSpec = runnerSpy.resolveSpecification(SpecificationTest.appendUniqueSuffix(notExcludedSpecPath));

        Assert.assertNotNull("Not excluded spec can't be null", currentSpec);
        Assert.assertEquals(notExcludedSpecPath, currentSpec.getLocation());

        currentSpec = runnerSpy.resolveSpecification(SpecificationTest.appendUniqueSuffix("FakeTwo.html"));
        Assert.assertNull("Excluded spec must be null", currentSpec);

        currentSpec = runnerSpy.resolveSpecification(SpecificationTest.appendUniqueSuffix("FakeThree.html"));
        Assert.assertNull("Excluded spec must be null", currentSpec);

    }

    @org.junit.Test
    public void testSpecificationResolved_includesOnly() {

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
    public void testSpecificationResolved_includesAndExcludes() {
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

        currentSpec = runnerSpy.resolveSpecification(SpecificationTest.appendUniqueSuffix("FakeOne.html"));
        Assert.assertNull("Excluded spec must be null", currentSpec);

        currentSpec = runnerSpy.resolveSpecification(SpecificationTest.appendUniqueSuffix("FakeThree.html"));
        Assert.assertNull("Excluded spec must be null", currentSpec);

    }

    @org.junit.Test
    public void testTestClassResolved_classFromSpecification() throws ClassNotFoundException {

        when(mockTest.getDefaultTestClass()).thenReturn(Integer.class);
        when(mockSpecification.getTestClass()).thenReturn(String.class);

        runnerSpy.specification = mockSpecification;

        Class testClass = runnerSpy.findTestClass(null, null);

        Assert.assertEquals(String.class, testClass);
    }

    @org.junit.Test
    public void testTestClassResolved_classFromTest() throws ClassNotFoundException {

        when(mockTest.getDefaultTestClass()).thenReturn(Integer.class);

        when(mockSpecification.getTestClass()).thenReturn(null);

        runnerSpy.specification = mockSpecification;

        Class testClass = runnerSpy.findTestClass(null, null);

        Assert.assertEquals(Integer.class, testClass);
    }

    private void checkResults(int success, int fail, int ignore, int exception) {
        TestContext context = new TestContextService().getCurrentTestContext();
        SpecificationResultRegistry registry = context.getCurrentSpecificationContext().getResultRegistry();
        Assert.assertEquals(success, registry.getSuccessCount());
        Assert.assertEquals(fail, registry.getFailureCount());
        Assert.assertEquals(ignore, registry.getIgnoredCount());
        Assert.assertEquals(exception, registry.getExceptionCount());
    }


}
