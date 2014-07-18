package com.scejtesting.core.concordion.command;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.context.SpecificationResultRegistry;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.ResultRecorder;
import org.concordion.internal.SummarizingResultRecorder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by aleks on 7/15/14.
 */
public class SaveResultsCommandTest {

    private TestContext context;

    @Before
    public void initTest() {
        Specification specification = mock(Specification.class);
        when(specification.getLocation()).thenReturn("/rootSpecificaiton.html");

        com.scejtesting.core.config.Test test = mock(com.scejtesting.core.config.Test.class);
        when(test.getSpecification()).thenReturn(specification);
        when(test.getDefaultTestClass()).thenCallRealMethod();

        context = new TestContextService().createNewTestContext(test);

    }


    @Test
    public void positiveFlow() {
        SummarizingResultRecorder resultRecorder = mock(SummarizingResultRecorder.class);
        when(resultRecorder.getSuccessCount()).thenReturn(1l);
        when(resultRecorder.getFailureCount()).thenReturn(2l);
        when(resultRecorder.getIgnoredCount()).thenReturn(3l);
        when(resultRecorder.getExceptionCount()).thenReturn(4l);

        new SaveResultsCommand().execute(null, null, resultRecorder);

        SpecificationResultRegistry resultsRegistry = context.getCurrentSpecificationContext().
                getResultRegistry();

        Assert.assertEquals(0, resultsRegistry.getSuccessCount());
        Assert.assertEquals(0, resultsRegistry.getFailureCount());
        Assert.assertEquals(0, resultsRegistry.getIgnoredCount());
        Assert.assertEquals(0, resultsRegistry.getExceptionCount());

        resultsRegistry.processResults();

        Assert.assertEquals(1, resultsRegistry.getSuccessCount());
        Assert.assertEquals(2, resultsRegistry.getFailureCount());
        Assert.assertEquals(3, resultsRegistry.getIgnoredCount());
        Assert.assertEquals(4, resultsRegistry.getExceptionCount());

        Assert.assertEquals(context, new SaveResultsCommand().getCurrentTestContext());
    }

    @Test
    public void nonResultsSummaryRecorder() {
        ResultRecorder resultRecorder = mock(ResultRecorder.class);

        new SaveResultsCommand().execute(null, null, resultRecorder);

        SpecificationResultRegistry resultsRegistry = context.getCurrentSpecificationContext().
                getResultRegistry();

        Assert.assertEquals(0, resultsRegistry.getSuccessCount());
        Assert.assertEquals(0, resultsRegistry.getFailureCount());
        Assert.assertEquals(0, resultsRegistry.getIgnoredCount());
        Assert.assertEquals(0, resultsRegistry.getExceptionCount());
    }
}
