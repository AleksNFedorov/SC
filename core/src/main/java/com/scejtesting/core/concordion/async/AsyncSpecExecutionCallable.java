package com.scejtesting.core.concordion.async;

import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.runner.ContextSynchronizerWithClonedContext;
import org.concordion.api.CommandCall;
import org.concordion.api.ResultRecorder;
import org.concordion.internal.command.RunCommand;
import org.concordion.internal.listener.RunResultRenderer;
import org.concordion.internal.util.Check;

import java.util.concurrent.Callable;

/**
 * Created by aleks on 7/22/14.
 */
public class AsyncSpecExecutionCallable implements Callable<AsyncExecutionResult> {

    private final TestContext contextToClone;
    private final AsyncExecutionResult asyncExecutionResult;

    private final RunCommand runCommand;

    public AsyncSpecExecutionCallable(TestContext contextToClone, CommandCall commandCall, ResultRecorder resultRecorder) {
        Check.notNull(contextToClone, "Context to clone must be specified");
        Check.isFalse(TestContext.isDestroyedContext(contextToClone), "Provided context is destroyed");
        this.contextToClone = contextToClone;
        asyncExecutionResult = new AsyncExecutionResult(commandCall, resultRecorder);
        runCommand = buildRunCommand();
    }

    private RunCommand buildRunCommand() {
        RunCommand command = new RunCommand();
        command.addRunListener(new RunResultRenderer());
        return command;
    }

    @Override
    public AsyncExecutionResult call() throws Exception {
        try {
            ContextSynchronizerWithClonedContext runner = new ContextSynchronizerWithClonedContext() {
                @Override
                public Object runCallBack(TestContext context) {
                    runCommand.execute(
                            asyncExecutionResult.getCommandCall(),
                            new FakeEvaluator(),
                            asyncExecutionResult.getResultRecorder());
                    return new Object();
                }
            };
            runner.runSync(contextToClone);
        } catch (Throwable ex) {
            asyncExecutionResult.setException(ex);
        }
        return asyncExecutionResult;
    }
}
