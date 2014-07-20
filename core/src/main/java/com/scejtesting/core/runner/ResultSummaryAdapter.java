package com.scejtesting.core.runner;

import org.concordion.api.ResultSummary;

import java.io.PrintStream;

/**
 * Created by aleks on 7/19/14.
 */
public class ResultSummaryAdapter implements ResultSummary {

    private long successCount;
    private long failCount;
    private long exceptionCount;
    private long ignoreCount;

    public ResultSummaryAdapter(long successCount, long failCount, long exceptionCount, long ignoreCount) {
        this.successCount = successCount;
        this.failCount = failCount;
        this.exceptionCount = exceptionCount;
        this.ignoreCount = ignoreCount;
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
        return getExceptionCount() > 0;
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
}
