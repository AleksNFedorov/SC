package com.scejtesting.selenium.concordion.command;

import com.scejtesting.core.concordion.command.ScejCommand;
import com.scejtesting.selenium.SeleniumDriverManagerService;
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

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    protected final SeleniumDriverManagerService seleniumDriverManagerService = getDriverManerService();

    protected abstract void processDriverCommand(String driverName);

    @Override
    public final void setUp(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {

        String driverName = commandCall.getExpression();
        LOG.info("Driver name evaluated as [{}]", driverName);

        processDriverCommand(driverName);
    }

    protected SeleniumDriverManagerService getDriverManerService() {
        return new SeleniumDriverManagerService();
    }
}
