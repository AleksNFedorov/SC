package com.scejtesting.selenium.concordion.extension.command;

import com.scejtesting.core.concordion.command.ScejCommand;
import com.scejtesting.selenium.WebTestFixture;
import org.concordion.api.AbstractCommand;
import org.concordion.api.CommandCall;
import org.concordion.api.Evaluator;
import org.concordion.api.ResultRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Fedorovaleks
 * Date: 3/31/14
 */
public abstract class AbstractSeleniumDriverCommand extends AbstractCommand implements ScejCommand {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractSeleniumDriverCommand.class);
    protected final WebTestFixture seleniumDriverManagerService = new WebTestFixture();

    protected abstract void processDriverCommand(String driverName);

    @Override
    public final void setUp(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {

        String expression = commandCall.getExpression();
        LOG.info("Driver name evaluated as [{}]", expression);

        processDriverCommand(expression);
    }

}
