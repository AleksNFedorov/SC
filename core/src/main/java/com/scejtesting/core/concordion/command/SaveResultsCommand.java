package com.scejtesting.core.concordion.command;

import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.CommandCall;
import org.concordion.api.Evaluator;
import org.concordion.api.ResultRecorder;
import org.concordion.api.ResultSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aleks on 7/13/14.
 */
public class SaveResultsCommand implements ScejCommand {

    private static Logger LOG = LoggerFactory.getLogger(RegisterGlobalVariablesCommand.class);

    private TestContext currentTestContext = new TestContextService().getCurrentTestContext();

    @Override
    public String getCommandName() {
        return "saveResults";
    }

    @Override
    public void setUp(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {

    }

    @Override
    public void execute(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
        LOG.debug("method invoked");
        if (resultRecorder instanceof ResultSummary) {
            ResultSummary resultSummary = (ResultSummary) resultRecorder;
            getCurrentTestContext().getCurrentSpecificationContext().
                    getResultRegistry().storeSpecificationResultSummary(resultSummary);
        } else {
            LOG.warn("Results recorder is not Results summary instance");
        }
        LOG.debug("method finished");
    }

    @Override
    public void verify(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {

    }

    TestContext getCurrentTestContext() {
        return currentTestContext;
    }
}
