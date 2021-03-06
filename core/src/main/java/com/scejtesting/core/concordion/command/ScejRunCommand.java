package com.scejtesting.core.concordion.command;

import com.scejtesting.core.concordion.SyncResultRecorderWrapper;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import com.scejtesting.core.runner.ContextSynchronizer;
import org.concordion.api.CommandCall;
import org.concordion.api.Element;
import org.concordion.api.Evaluator;
import org.concordion.api.ResultRecorder;
import org.concordion.internal.command.RunCommand;
import org.concordion.internal.listener.RunResultRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aleks on 7/12/14.
 */
public class ScejRunCommand extends RunCommand implements ScejCommand {

    private static Logger LOG = LoggerFactory.getLogger(RegisterGlobalVariablesCommand.class);

    private final TestContext currentTestContext = new TestContextService().getCurrentTestContext();

    public ScejRunCommand() {
        addRunListener(new RunResultRenderer());
    }

    @Override
    public void execute(final CommandCall commandCall, final Evaluator evaluator, final ResultRecorder resultRecorder) {
        LOG.debug("method invoked");
        ContextSynchronizer runner = new ContextSynchronizer() {
            @Override
            public Object runCallBack(TestContext context) {
                Element element = commandCall.getElement();
                saveLinkElement(element);
                executeConcordionRun(
                        commandCall,
                        evaluator,
                        new SyncResultRecorderWrapper(resultRecorder));
                return new Object();
            }
        };
        LOG.debug("Method finished");

        runner.runSync(getTestContext());
    }

    void executeConcordionRun(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
        super.execute(commandCall, evaluator, resultRecorder);
    }

    protected void saveLinkElement(Element element) {
        LOG.debug("method invoked");
        getTestContext().saveChildSpecificationElement(element);
        LOG.debug("Method finished");
    }

    protected TestContext getTestContext() {
        return currentTestContext;
    }

    @Override
    public String getCommandName() {
        return "run";
    }
}
