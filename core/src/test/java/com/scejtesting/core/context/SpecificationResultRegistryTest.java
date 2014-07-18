package com.scejtesting.core.context;

import org.concordion.api.Result;
import org.concordion.api.ResultSummary;
import org.concordion.api.RunnerResult;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by aleks on 4/26/14.
 */
public class SpecificationResultRegistryTest {

    @Test
    public void doubleProcessingTest() {
        ResultSummary resultSummary1 = buildResultSummary(1, 2, 3, 4);

        SpecificationResultRegistry resultRegistry = new SpecificationResultRegistry();

        resultRegistry.addResult(resultSummary1);
        resultRegistry.processResults();
        resultRegistry.processResults();

        Assert.assertEquals(1, resultRegistry.getSuccessCount());
        Assert.assertEquals(2, resultRegistry.getFailureCount());
        Assert.assertEquals(3, resultRegistry.getExceptionCount());
        Assert.assertEquals(4, resultRegistry.getIgnoredCount());
    }

    @Test
    public void addResultSummaryTest() {

        ResultSummary resultSummary1 = buildResultSummary(1, 2, 3, 4);
        ResultSummary resultSummary2 = buildResultSummary(2, 3, 4, 0);

        SpecificationResultRegistry resultRegistry = new SpecificationResultRegistry();

        resultRegistry.addResult(resultSummary1);
        resultRegistry.processResults();
        resultRegistry.addResult(resultSummary2);
        resultRegistry.processResults();

        Assert.assertEquals(3, resultRegistry.getSuccessCount());
        Assert.assertEquals(5, resultRegistry.getFailureCount());
        Assert.assertEquals(7, resultRegistry.getExceptionCount());
        Assert.assertEquals(4, resultRegistry.getIgnoredCount());
    }

    @Test
    public void addResultSummaryWithResultTest() {
        RunnerResult result = new RunnerResult(Result.SUCCESS);
        ResultSummary resultSummary1 = buildResultSummary(2, 1, 0, 0);

        SpecificationResultRegistry resultRegistry = new SpecificationResultRegistry();

        resultRegistry.addResult(resultSummary1, result);

        Assert.assertEquals(1, resultRegistry.getSuccessCount());
        Assert.assertEquals(1, resultRegistry.getFailureCount());
        Assert.assertEquals(0, resultRegistry.getExceptionCount());
        Assert.assertEquals(0, resultRegistry.getIgnoredCount());

    }

    @Test(expected = RuntimeException.class)
    public void noResultSummaryTest() {
        new SpecificationResultRegistry().addResult(null);
    }

    @Test(expected = RuntimeException.class)
    public void noResultTest() {
        new SpecificationResultRegistry().addResult(buildResultSummary(0, 0, 0, 0), null);
    }

    @Test(expected = RuntimeException.class)
    public void noResultWithNoResultSummaryTest() {
        new SpecificationResultRegistry().addResult(null, new RunnerResult(Result.SUCCESS));
    }

    private ResultSummary buildResultSummary(long success, long fail, long exception, long ignore) {
        ResultSummary summaryMock = mock(ResultSummary.class);

        when(summaryMock.getExceptionCount()).thenReturn(exception);
        when(summaryMock.getSuccessCount()).thenReturn(success);
        when(summaryMock.getFailureCount()).thenReturn(fail);
        when(summaryMock.getIgnoredCount()).thenReturn(ignore);

        return summaryMock;
    }

}
