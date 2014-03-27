package com.scejtesting.core.concordion;

import com.scejtesting.core.CoreTest;
import com.scejtesting.core.config.*;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.Resource;
import org.concordion.api.Result;
import org.concordion.api.RunnerResult;
import org.junit.Assert;

import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class ChildSpecificationRunnerTest extends TestContextService {


    @org.junit.Test
    public void positiveFlow() throws Exception {

        ChildSpecificationRunner runner = spy(new ChildSpecificationRunner());

        Suite suite = mock(Suite.class);
        when(suite.getThrownException()).thenReturn(null);

        Specification specification = mock(Specification.class);

        Test test = mock(Test.class);
        when(test.getThrownException()).thenReturn(null);
        when(test.getSpecification()).thenReturn(specification);


        TestContextService testContextService = new TestContextService();
        testContextService.createNewTestContext(test);


        RunnerResult successResult = new RunnerResult(Result.SUCCESS);

        doReturn(specification).when(runner).resolveSpecification(anyString());
        doReturn(suite).when(runner).getSuite();
        doReturn(successResult).when(runner).executeSpecification(any(Specification.class), any(Resource.class), anyString());


        RunnerResult executionResult = runner.execute(new Resource("/somePath"), "Some href");

        Assert.assertEquals(successResult, executionResult);


        testContextService.getCurrentTestContext().destroyCurrentSpecificationContext();
    }

    @org.junit.Test
    public void suiteFailFastException() throws Exception {


        ChildSpecificationRunner runner = spy(new ChildSpecificationRunner());

        Suite suite = mock(Suite.class);
        when(suite.getThrownException()).thenReturn(new RuntimeException());

        Specification specification = mock(Specification.class);

        Test test = mock(Test.class);
        when(test.getThrownException()).thenReturn(null);
        when(test.getSpecification()).thenReturn(specification);


        TestContextService testContextService = new TestContextService();
        testContextService.createNewTestContext(test);


        RunnerResult successResult = new RunnerResult(Result.SUCCESS);

        doReturn(specification).when(runner).resolveSpecification(anyString());
        doReturn(suite).when(runner).getSuite();
        doReturn(successResult).when(runner).executeSpecification(any(Specification.class), any(Resource.class), anyString());


        RunnerResult executionResult = runner.execute(new Resource("/somePath"), "Some href");

        Assert.assertEquals(Result.IGNORED, executionResult.getResult());


        testContextService.getCurrentTestContext().destroyCurrentSpecificationContext();

    }

    @org.junit.Test
    public void unknownSpecification() throws Exception {

        ChildSpecificationRunner runner = spy(new ChildSpecificationRunner());

        Suite suite = mock(Suite.class);
        when(suite.getThrownException()).thenReturn(null);

        Specification specification = mock(Specification.class);

        Test test = mock(Test.class);
        when(test.getThrownException()).thenReturn(null);
        when(test.getSpecification()).thenReturn(specification);


        TestContextService testContextService = new TestContextService();
        testContextService.createNewTestContext(test);


        RunnerResult successResult = new RunnerResult(Result.SUCCESS);

        doReturn(null).when(runner).resolveSpecification(anyString());
        doReturn(suite).when(runner).getSuite();
        doReturn(successResult).when(runner).executeSpecification(any(Specification.class), any(Resource.class), anyString());


        RunnerResult executionResult = runner.execute(new Resource("/somePath"), "Some href");

        Assert.assertEquals(Result.IGNORED, executionResult.getResult());


        testContextService.getCurrentTestContext().destroyCurrentSpecificationContext();
    }

    @org.junit.Test
    public void testFailFastException() throws Exception {


        ChildSpecificationRunner runner = spy(new ChildSpecificationRunner());

        Suite suite = mock(Suite.class);
        when(suite.getThrownException()).thenReturn(null);

        Specification specification = mock(Specification.class);

        Test test = mock(Test.class);
        when(test.getThrownException()).thenReturn(new RuntimeException());
        when(test.getSpecification()).thenReturn(specification);


        TestContextService testContextService = new TestContextService();
        testContextService.createNewTestContext(test);


        RunnerResult successResult = new RunnerResult(Result.SUCCESS);

        doReturn(specification).when(runner).resolveSpecification(anyString());
        doReturn(suite).when(runner).getSuite();
        doReturn(successResult).when(runner).executeSpecification(any(Specification.class), any(Resource.class), anyString());


        RunnerResult executionResult = runner.execute(new Resource("/somePath"), "Some href");

        Assert.assertEquals(Result.IGNORED, executionResult.getResult());


        testContextService.getCurrentTestContext().destroyCurrentSpecificationContext();

    }

    @org.junit.Test
    public void testIncludeAll() {


        Specification rootSpecification = new Specification("/includeAllSpecification.html");
        Test testMock = mock(Test.class);
        when(testMock.getSpecification()).thenReturn(rootSpecification);
        when(testMock.getDefaultTestClass()).thenReturn(CoreTest.class);

        createNewTestContext(testMock);

        ChildSpecificationRunner testRunner = new ChildSpecificationRunner();

        String childSpecificationOrig = "childSpecification.html";

        Specification specification = testRunner.resolveSpecification(
                SpecificationTest.appendUniqueSuffix(childSpecificationOrig));

        Assert.assertEquals(childSpecificationOrig, specification.getLocation());

        getCurrentTestContext().destroyCurrentSpecificationContext();
    }


    @org.junit.Test
    public void testExcludeOnlyAll() {


        Specification fakeSpecificationTwo = new Specification("FakeTwo.html");
        Specification fakeSpecificationThree = new Specification("FakeThree.html");

        Excludes excludes = new Excludes(Arrays.asList(fakeSpecificationTwo, fakeSpecificationThree));

        Test mockTest = mock(Test.class);
        Specification excludeOnly = mock(Specification.class);

        when(mockTest.getDefaultTestClass()).thenCallRealMethod();
        when(mockTest.getSpecification()).thenReturn(excludeOnly);
        when(excludeOnly.getExcludes()).thenReturn(excludes);

        createNewTestContext(mockTest);

        ChildSpecificationRunner testRunner = new ChildSpecificationRunner();

        String notExcludedSpecPath = "someNotExludedSpec.html";

        Specification currentSpec = testRunner.resolveSpecification(SpecificationTest.appendUniqueSuffix(notExcludedSpecPath));

        Assert.assertNotNull("Not excluded spec can't be null", currentSpec);
        Assert.assertEquals(notExcludedSpecPath, currentSpec.getLocation());

        currentSpec = testRunner.resolveSpecification("FakeTwo.html");
        Assert.assertNull("Excluded spec must be null", currentSpec);

        currentSpec = testRunner.resolveSpecification("FakeThree.html");
        Assert.assertNull("Excluded spec must be null", currentSpec);

        getCurrentTestContext().destroyCurrentSpecificationContext();


    }


    @org.junit.Test
    public void testIncludeOnly() {

        Specification fakeSpecificationOne = new Specification("FakeOne.html");
        Specification fakeSpecificationTwo = new Specification("FakeTwo.html");
        Specification fakeSpecificationThree = new Specification("FakeThree.html");

        Includes includes = new Includes(Arrays.asList(fakeSpecificationOne,
                fakeSpecificationTwo,
                fakeSpecificationThree));

        Test test = mock(Test.class);
        when(test.getDefaultTestClass()).thenCallRealMethod();

        Specification includeOnly = mock(Specification.class);
        when(includeOnly.getIncludes()).thenReturn(includes);
        when(test.getSpecification()).thenReturn(includeOnly);

        createNewTestContext(test);

        ChildSpecificationRunner testRunner = new ChildSpecificationRunner();

        Specification currentSpec = testRunner.resolveSpecification("someSpec.html");
        Assert.assertNull("Not included spec must be null", currentSpec);

        String specLocationForTest = "FakeTwo.html";
        currentSpec = testRunner.resolveSpecification(SpecificationTest.appendUniqueSuffix(specLocationForTest));
        Assert.assertNotNull("Included spec can't be null", currentSpec);
        Assert.assertEquals(specLocationForTest, currentSpec.getLocation());

        specLocationForTest = "FakeOne.html";
        currentSpec = testRunner.resolveSpecification(SpecificationTest.appendUniqueSuffix(specLocationForTest));
        Assert.assertNotNull("Included spec can't be null", currentSpec);
        Assert.assertEquals(specLocationForTest, currentSpec.getLocation());

        specLocationForTest = "FakeThree.html";
        currentSpec = testRunner.resolveSpecification(SpecificationTest.appendUniqueSuffix(specLocationForTest));
        Assert.assertNotNull("Included spec can't be null", currentSpec);
        Assert.assertEquals(specLocationForTest, currentSpec.getLocation());

        getCurrentTestContext().destroyCurrentSpecificationContext();
    }


    @org.junit.Test
    public void testIncludeExclude() {
        Specification fakeSpecificationOne = new Specification("FakeOne.html");
        Specification fakeSpecificationTwo = new Specification("FakeTwo.html");
        Specification fakeSpecificationThree = new Specification("FakeThree.html");

        Includes includes = new Includes(Arrays.asList(fakeSpecificationOne,
                fakeSpecificationTwo));
        Excludes excludes = new Excludes(Arrays.asList(fakeSpecificationOne, fakeSpecificationThree));

        Test test = mock(Test.class);
        when(test.getDefaultTestClass()).thenCallRealMethod();

        Specification includeExclude = mock(Specification.class);

        when(includeExclude.getExcludes()).thenReturn(excludes);
        when(includeExclude.getIncludes()).thenReturn(includes);

        when(test.getSpecification()).thenReturn(includeExclude);

        createNewTestContext(test);

        ChildSpecificationRunner testRunner = new ChildSpecificationRunner();

        Specification currentSpec = testRunner.resolveSpecification("someNotExludedSpec.html");
        Assert.assertNull("Not included spec must be null", currentSpec);

        String currentSpecLocation = "FakeTwo.html";
        currentSpec = testRunner.resolveSpecification(SpecificationTest.appendUniqueSuffix(currentSpecLocation));
        Assert.assertNotNull("Included spec can't be null", currentSpec);
        Assert.assertEquals(currentSpecLocation, currentSpec.getLocation());


        currentSpec = testRunner.resolveSpecification("FakeOne.html");
        Assert.assertNull("Excluded spec must be null", currentSpec);

        currentSpec = testRunner.resolveSpecification("FakeThree.html");
        Assert.assertNull("Excluded spec must be null", currentSpec);

        getCurrentTestContext().destroyCurrentSpecificationContext();
    }

    @org.junit.Test
    public void findTestClassFromSpecification() throws ClassNotFoundException {

        Test test = mock(Test.class);
        when(test.getDefaultTestClass()).thenReturn(Integer.class);

        Specification specification = mock(Specification.class);

        when(test.getSpecification()).thenReturn(specification);
        when(specification.getTestClass()).thenReturn(String.class);

        createNewTestContext(test);

        Class testClass = new ChildSpecificationRunner().findTestClass(null, null);

        Assert.assertEquals(String.class, testClass);

        getCurrentTestContext().destroyCurrentSpecificationContext();

    }

    @org.junit.Test
    public void findTestClassFromTest() throws ClassNotFoundException {

        Test test = mock(Test.class);
        when(test.getDefaultTestClass()).thenReturn(Integer.class);

        Specification specification = mock(Specification.class);

        when(test.getSpecification()).thenReturn(specification);
        when(specification.getTestClass()).thenReturn(null);

        createNewTestContext(test);

        Class testClass = new ChildSpecificationRunner().findTestClass(null, null);

        Assert.assertEquals(Integer.class, testClass);

        getCurrentTestContext().destroyCurrentSpecificationContext();

    }


}
