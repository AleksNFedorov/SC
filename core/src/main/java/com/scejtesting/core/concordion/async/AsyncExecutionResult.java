package com.scejtesting.core.concordion.async;

import org.concordion.api.CommandCall;
import org.concordion.api.ResultRecorder;
import org.concordion.api.ResultSummary;
import org.concordion.internal.SummarizingResultRecorder;
import org.concordion.internal.util.Check;

/**
 * Created by aleks on 7/21/14.
 */
public class AsyncExecutionResult {

    private final CommandCall call;
    private final ResultRecorder parentResultsRecorder;
    private final SummarizingResultRecorder runnerResult = new SummarizingResultRecorder();
    private Throwable exception;

    public AsyncExecutionResult(CommandCall call, ResultRecorder resultRecorder) {
        Check.notNull(call, "Command call must be specified");
        Check.notNull(resultRecorder, "Results recorder must be specified");
        this.call = call;
        this.parentResultsRecorder = resultRecorder;
    }

    public CommandCall getCommandCall() {
        return call;
    }

    public ResultRecorder getParentResultRecorder() {
        return parentResultsRecorder;
    }

    public ResultSummary getRunnerResult() {
        return runnerResult;
    }

    public ResultRecorder getResultRecorder() {
        return runnerResult;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        Check.isTrue(this.exception == null, "Exception already set");
        this.exception = exception;
    }
}
