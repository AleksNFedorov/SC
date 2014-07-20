package com.scejtesting.core.context;

import org.concordion.api.ResultSummary;
import org.concordion.api.RunnerResult;
import org.concordion.internal.SummarizingResultRecorder;
import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aleks on 4/26/14.
 */
public class SpecificationResultRegistry extends SummarizingResultRecorder {

    protected static final Logger LOG = LoggerFactory.getLogger(SpecificationResultRegistry.class);

    private ResultSummary specificationResultSummary;

    private long successCount;
    private long failCount;
    private long exceptionCount;
    private long ignoreCount;

    public void addResult(ResultSummary summary, RunnerResult result) {
        LOG.debug("method invoked [{}][{}]", summary, result);
        Check.notNull(result, "Result must be specified");
        this.applyStoreResultSummary(summary);
        switch (result.getResult()) {
            case SUCCESS:
                successCount--;
                break;
            case IGNORED:
                ignoreCount--;
                break;
            case EXCEPTION:
                exceptionCount--;
                break;
            case FAILURE:
                failCount--;
                break;
        }
        LOG.debug("method finished");
    }


    public void addResult(ResultSummary summary) {
        Check.notNull(summary, "Result summary can't be empty");
        specificationResultSummary = summary;
        LOG.info("New result has been added [{}] ", toString());
    }

    public void processResults() {
        if (specificationResultSummary != null) {
            applyStoreResultSummary(specificationResultSummary);
            specificationResultSummary = null;
        }
    }

    private void applyStoreResultSummary(ResultSummary summary) {
        successCount += summary.getSuccessCount();
        failCount += summary.getFailureCount();
        exceptionCount += summary.getExceptionCount();
        ignoreCount += summary.getIgnoredCount();
    }

    @Override
    public long getSuccessCount() {
        return successCount;
    }

    @Override
    public long getFailureCount() {
        return failCount;
    }

    @Override
    public long getExceptionCount() {
        return exceptionCount;
    }

    @Override
    public long getIgnoredCount() {
        return ignoreCount;
    }

    @Override
    public String toString() {
        return "SpecificationResultRegistry{" +
                "successCount=" + successCount +
                ", failCount=" + failCount +
                ", exceptionCount=" + exceptionCount +
                ", ignoreCount=" + ignoreCount +
                '}';
    }
}
