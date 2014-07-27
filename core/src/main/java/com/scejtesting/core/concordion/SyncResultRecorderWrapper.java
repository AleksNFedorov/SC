package com.scejtesting.core.concordion;

import org.concordion.api.Result;
import org.concordion.api.ResultRecorder;
import org.concordion.internal.FailFastException;

/**
 * Created by aleks on 7/26/14.
 */
public class SyncResultRecorderWrapper implements ResultRecorder {

    private final ResultRecorder orig;

    public SyncResultRecorderWrapper(ResultRecorder orig) {
        this.orig = orig;
    }

    @Override
    public synchronized void record(Result result) {
        orig.record(result);
    }

    @Override
    public synchronized void recordFailFastException(FailFastException exception) {
        orig.recordFailFastException(exception);
    }
}
