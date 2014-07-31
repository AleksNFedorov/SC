package com.scejtesting.core.concordion.async;

import com.scejtesting.core.concordion.SyncResultRecorderWrapper;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import com.scejtesting.core.runner.ContextSynchronizer;
import org.concordion.api.CommandCall;
import org.concordion.api.Evaluator;
import org.concordion.api.ResultRecorder;
import org.concordion.internal.command.RunCommand;
import org.concordion.internal.listener.RunResultRenderer;
import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aleks on 7/22/14.
 */
public class AsyncSpecExecutionCallable implements Runnable {

    private static Logger LOG = LoggerFactory.getLogger(AsyncSpecExecutionCallable.class);

    private final TestContext contextToClone;
    private final CommandCall commandCall;
    private final ResultRecorder resultRecorder;

    class FakeEvaluator implements Evaluator {
        @Override
        public Object getVariable(String variableName) {
            return null;
        }

        @Override
        public void setVariable(String variableName, Object value) {

        }

        @Override
        public Object evaluate(String expression) {
            return null;
        }
    }


    public AsyncSpecExecutionCallable(TestContext contextToClone, CommandCall commandCall, ResultRecorder resultRecorder) {
        Check.notNull(contextToClone, "Context to clone must be specified");
        Check.isFalse(TestContext.isDestroyedContext(contextToClone), "Provided context is destroyed");
        Check.notNull(commandCall, "Command call must be specified");
        Check.notNull(resultRecorder, "Results records must be specified");

        this.contextToClone = contextToClone;
        this.commandCall = commandCall;
        this.resultRecorder = resultRecorder;
        LOG.debug("AsyncSpecExecutionCallable created for [{}]", contextToClone);
    }

    protected RunCommand buildRunCommand() {
        RunCommand command = new RunCommand();
        command.addRunListener(new RunResultRenderer());
        return command;
    }

    @Override
    public void run() {
        LOG.debug("method invoked [{}]", contextToClone.getCurrentSpecificationContext());
        TestContextService testContextService = buildTestContextService();
        TestContext asyncContext = testContextService.cloneContext(contextToClone);
        TestContext.SpecificationContext specificationContext = contextToClone.getCurrentSpecificationContext();
        try {
            ContextSynchronizer runner = new ContextSynchronizer() {
                @Override
                public Object runCallBack(TestContext context) {
                    buildRunCommand().execute(
                            commandCall,
                            new FakeEvaluator(),
                            new SyncResultRecorderWrapper(resultRecorder));
                    return new Object();
                }
            };
            runner.runSync(asyncContext);
            LOG.info("Async call finished successfully");
        } catch (Throwable ex) {
            LOG.error("Async call exception [{}]", ex.getMessage(), ex);
        } finally {
            testContextService.dropContext(asyncContext);
            specificationContext.onAsyncCallFinished();
        }
        LOG.debug("method finished");
    }

    TestContextService buildTestContextService() {
        return new TestContextService();
    }

}
