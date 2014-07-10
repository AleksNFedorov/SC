package com.scejtesting.core.concordion;

import com.scejtesting.core.context.TestContextService;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class ChildSpecificationRunnerTest extends TestContextService {

 /*   @org.junit.Test
    public void saveResultOnException() throws Exception {

        ChildSpecificationRunner runner = spy(new ChildSpecificationRunner());

        Suite suite = mock(Suite.class);
        when(suite.getThrownException()).thenReturn(null);

        Specification specification = mock(Specification.class);

        Test test = mock(Test.class);
        when(test.getThrownException()).thenReturn(null);
        when(test.getSpecification()).thenReturn(specification);
        doThrow((new IllegalStateException("Thrown for test purpose"))).when(runner).executeSpecification(eq(specification),
                any(Resource.class), anyString());

        createNewTestContext(test);

        runner.setTestContext(getCurrentTestContext());

        doReturn(specification).when(runner).resolveSpecification(anyString());
        doReturn(suite).when(runner).getSuite();

        try {
            runner.execute(new Resource("/somePath"), "Some href");
            Assert.fail();
        } catch (IllegalStateException ex) {

        }

        SpecificationResultRegistry registry = getCurrentTestContext().getCurrentSpecificationContext().getResultRegistry();

        Assert.assertEquals(1, (int) registry.getResultsAmount(Result.EXCEPTION));

        destroyTestContextService();

    }

    @org.junit.Test
    public void positiveFlow() throws Exception {

        ChildSpecificationRunner runner = spy(new ChildSpecificationRunner());

        Suite suite = mock(Suite.class);
        when(suite.getThrownException()).thenReturn(null);

        Specification specification = mock(Specification.class);

        Test test = mock(Test.class);
        when(test.getThrownException()).thenReturn(null);
        when(test.getSpecification()).thenReturn(specification);

        createNewTestContext(test);
        runner.setTestContext(getCurrentTestContext());

        RunnerResult successResult = new RunnerResult(Result.SUCCESS);

        doReturn(specification).when(runner).resolveSpecification(anyString());
        doReturn(suite).when(runner).getSuite();
        doReturn(successResult).when(runner).executeSpecification(any(Specification.class), any(Resource.class), anyString());

        RunnerResult executionResult = runner.execute(new Resource("/somePath"), "Some href");

        Assert.assertEquals(successResult, executionResult);

        SpecificationResultRegistry registry = getCurrentTestContext().getCurrentSpecificationContext().getResultRegistry();

        Assert.assertEquals(1, (int) registry.getResultsAmount(successResult.getResult()));

        destroyTestContextService();
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

        createNewTestContext(test);
        runner.setTestContext(getCurrentTestContext());

        RunnerResult successResult = new RunnerResult(Result.SUCCESS);

        doReturn(specification).when(runner).resolveSpecification(anyString());
        doReturn(suite).when(runner).getSuite();
        doReturn(successResult).when(runner).executeSpecification(any(Specification.class), any(Resource.class), anyString());

        RunnerResult executionResult = runner.execute(new Resource("/somePath"), "Some href");

        Assert.assertEquals(Result.IGNORED, executionResult.getResult());

        SpecificationResultRegistry registry = getCurrentTestContext().getCurrentSpecificationContext().getResultRegistry();

        Assert.assertEquals(1, (int) registry.getResultsAmount(executionResult.getResult()));

        destroyTestContextService();

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

        createNewTestContext(test);
        runner.setTestContext(getCurrentTestContext());

        RunnerResult successResult = new RunnerResult(Result.SUCCESS);

        doReturn(null).when(runner).resolveSpecification(anyString());
        doReturn(suite).when(runner).getSuite();
        doReturn(successResult).when(runner).executeSpecification(any(Specification.class), any(Resource.class), anyString());

        RunnerResult executionResult = runner.execute(new Resource("/somePath"), "Some href");

        Assert.assertEquals(Result.IGNORED, executionResult.getResult());

        destroyTestContextService();
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

        createNewTestContext(test);

        runner.setTestContext(getCurrentTestContext());


        RunnerResult successResult = new RunnerResult(Result.SUCCESS);

        doReturn(specification).when(runner).resolveSpecification(anyString());
        doReturn(suite).when(runner).getSuite();
        doReturn(successResult).when(runner).executeSpecification(any(Specification.class), any(Resource.class), anyString());

        RunnerResult executionResult = runner.execute(new Resource("/somePath"), "Some href");

        Assert.assertEquals(Result.IGNORED, executionResult.getResult());

        destroyTestContextService();

    }

    @org.junit.Test
    public void testIncludeAll() {

        Specification rootSpecification = new Specification("/includeAllSpecification.html");
        Test testMock = mock(Test.class);
        when(testMock.getSpecification()).thenReturn(rootSpecification);
        when(testMock.getDefaultTestClass()).thenReturn(CoreTestFixture.class);

        createNewTestContext(testMock);

        ChildSpecificationRunner testRunner = new ChildSpecificationRunner();
        testRunner.setTestContext(getCurrentTestContext());

        String childSpecificationOrig = "childSpecification.html";

        Specification specification = testRunner.resolveSpecification(
                SpecificationTest.appendUniqueSuffix(childSpecificationOrig));

        Assert.assertEquals(childSpecificationOrig, specification.getLocation());

        destroyTestContextService();
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

        testRunner.setTestContext(getCurrentTestContext());

        String notExcludedSpecPath = "someNotExludedSpec.html";

        Specification currentSpec = testRunner.resolveSpecification(SpecificationTest.appendUniqueSuffix(notExcludedSpecPath));

        Assert.assertNotNull("Not excluded spec can't be null", currentSpec);
        Assert.assertEquals(notExcludedSpecPath, currentSpec.getLocation());

        currentSpec = testRunner.resolveSpecification("FakeTwo.html");
        Assert.assertNull("Excluded spec must be null", currentSpec);

        currentSpec = testRunner.resolveSpecification("FakeThree.html");
        Assert.assertNull("Excluded spec must be null", currentSpec);

        destroyTestContextService();

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
        testRunner.setTestContext(getCurrentTestContext());

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

        destroyTestContextService();
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
        testRunner.setTestContext(getCurrentTestContext());

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

        destroyTestContextService();
    }

    @org.junit.Test
    public void findTestClassFromSpecification() throws ClassNotFoundException {

        Test test = mock(Test.class);
        when(test.getDefaultTestClass()).thenReturn(Integer.class);

        Specification specification = mock(Specification.class);

        when(test.getSpecification()).thenReturn(specification);
        when(specification.getTestClass()).thenReturn(String.class);

        createNewTestContext(test);

        ChildSpecificationRunner runner = new ChildSpecificationRunner();
        runner.specification = specification;
        runner.setTestContext(getCurrentTestContext());


        Class testClass = runner.findTestClass(null, null);

        Assert.assertEquals(String.class, testClass);

        destroyTestContextService();

    }

    @org.junit.Test
    public void findTestClassFromTest() throws ClassNotFoundException {

        Test test = mock(Test.class);
        when(test.getDefaultTestClass()).thenReturn(Integer.class);

        Specification specification = mock(Specification.class);

        when(test.getSpecification()).thenReturn(specification);
        when(specification.getTestClass()).thenReturn(null);

        createNewTestContext(test);

        ChildSpecificationRunner runner = new ChildSpecificationRunner();
        runner.setTestContext(getCurrentTestContext());
        runner.specification = specification;

        Class testClass = runner.findTestClass(null, null);

        Assert.assertEquals(Integer.class, testClass);

        destroyTestContextService();

    }
*/
}
