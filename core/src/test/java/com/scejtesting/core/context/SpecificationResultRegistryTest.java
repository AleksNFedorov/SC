package com.scejtesting.core.context;

/**
 * Created by aleks on 4/26/14.
 */
public class SpecificationResultRegistryTest extends TestContextService {

    /*
    @Test
    public void duplicateResultTest() {

        SpecificationResultRegistry childRegistry = new SpecificationResultRegistry();
        SpecificationResultRegistry parentRegistry = new SpecificationResultRegistry();

        RunnerResult successResult = new RunnerResult(Result.SUCCESS);
        RunnerResult successResult2 = new RunnerResult(Result.SUCCESS);
        RunnerResult failResult = new RunnerResult(Result.FAILURE);

        childRegistry.addResult(successResult);
        childRegistry.addResult(failResult);

        try {

            childRegistry.addResult(successResult);
            Assert.fail("Result already stored exception expected");

        } catch (RuntimeException ex) {

        }

        parentRegistry.addResult(successResult2);
        parentRegistry.addResult(failResult);

        try {
            parentRegistry.addAll(childRegistry);
            Assert.fail("Result already stored exception expected");

        } catch (RuntimeException ex) {

        }
    }

    @Test
    public void registryResultsAppending() {

        SpecificationResultRegistry childRegistry = new SpecificationResultRegistry();
        SpecificationResultRegistry parentRegistry = new SpecificationResultRegistry();

        RunnerResult successResult = new RunnerResult(Result.SUCCESS);
        RunnerResult successResult2 = new RunnerResult(Result.SUCCESS);

        RunnerResult failResult = new RunnerResult(Result.FAILURE);

        childRegistry.addResult(successResult);
        childRegistry.addResult(failResult);

        parentRegistry.addResult(successResult2);

        parentRegistry.addAll(childRegistry);

        Assert.assertEquals(Integer.valueOf(2), parentRegistry.getResultsAmount(Result.SUCCESS));

        Assert.assertEquals(Integer.valueOf(1), parentRegistry.getResultsAmount(Result.FAILURE));

    }

    @Test
    public void positiveFlow() {

        SpecificationResultRegistry registry = new SpecificationResultRegistry();

        RunnerResult successResult = new RunnerResult(Result.SUCCESS);
        RunnerResult successResult2 = new RunnerResult(Result.SUCCESS);

        RunnerResult failResult = new RunnerResult(Result.FAILURE);
        RunnerResult ignoreResult = new RunnerResult(Result.IGNORED);

        RunnerResult exceptionResult = new RunnerResult(Result.EXCEPTION);

        registry.addResult(successResult);
        registry.addResult(successResult2);

        registry.addResult(failResult);

        registry.addResult(ignoreResult);

        registry.addResult(exceptionResult);

        Assert.assertEquals(Integer.valueOf(2), registry.getResultsAmount(Result.SUCCESS));

        Assert.assertEquals(Integer.valueOf(1), registry.getResultsAmount(Result.FAILURE));

        Assert.assertEquals(Integer.valueOf(1), registry.getResultsAmount(Result.IGNORED));

        Assert.assertEquals(Integer.valueOf(1), registry.getResultsAmount(Result.EXCEPTION));

        Set<RunnerResult> successList = new TreeSet<RunnerResult>(new Comparator<RunnerResult>() {
            @Override
            public int compare(RunnerResult o, RunnerResult o2) {
                return o.hashCode() - o2.hashCode();
            }
        });

        successList.addAll(registry.getResultsList(Result.SUCCESS));

        Assert.assertEquals(2, successList.size());

        Assert.assertEquals(1, registry.getResultsList(Result.EXCEPTION).size());

        Assert.assertEquals(1, registry.getResultsList(Result.IGNORED).size());

        Assert.assertEquals(1, registry.getResultsList(Result.FAILURE).size());

        Assert.assertTrue(successList.contains(successResult));
        Assert.assertTrue(successList.contains(successResult2));

    }

    */
}
