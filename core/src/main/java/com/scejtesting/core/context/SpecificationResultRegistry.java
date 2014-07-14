package com.scejtesting.core.context;

import org.concordion.api.ResultSummary;
import org.concordion.api.RunnerResult;
import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;

/**
 * Created by aleks on 4/26/14.
 */
public class SpecificationResultRegistry implements ResultSummary {

    protected static final Logger LOG = LoggerFactory.getLogger(SpecificationResultRegistry.class);

    private long successCount;
    private long failCount;
    private long exceptionCount;
    private long ignoreCount;

    public void addResult(ResultSummary summary, RunnerResult result) {
        this.addResult(summary);
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
    }

    public void addResult(ResultSummary summary) {

        Check.notNull(summary, "Result can't be empty");

        successCount += summary.getSuccessCount();
        failCount += summary.getFailureCount();
        exceptionCount += summary.getExceptionCount();
        ignoreCount += summary.getIgnoredCount();

        LOG.info("New result has been added [{}] ", toString());
    }

    @Override
    public void assertIsSatisfied() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void assertIsSatisfied(Object fixture) {
        throw new UnsupportedOperationException();

    }

    @Override
    public boolean hasExceptions() {
        return false;
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
    public void print(PrintStream out) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void print(PrintStream out, Object fixture) {
        throw new UnsupportedOperationException();
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
