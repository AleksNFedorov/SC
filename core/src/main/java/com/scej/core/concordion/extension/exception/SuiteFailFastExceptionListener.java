package com.scej.core.concordion.extension.exception;

import com.scej.core.TestContext;
import com.scej.core.config.*;
import org.concordion.api.listener.ThrowableCaughtEvent;
import org.concordion.api.listener.ThrowableCaughtListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Fedorovaleks
 * Date: 16.03.14
 */
public class SuiteFailFastExceptionListener implements ThrowableCaughtListener {

    private static Logger LOG = LoggerFactory.getLogger(SuiteFailFastExceptionListener.class);


    @Override
    public void throwableCaught(ThrowableCaughtEvent event) {
        LOG.debug("method invoked [{}]", event);

        checkSuiteExceptions(event.getThrowable());
        checkTestExceptions(event.getThrowable());
        checkSpecificationExceptions(event.getThrowable());

        LOG.info("Thrown exception is not registered as fail fast exception");
        LOG.debug("method finished");
    }

    private void checkSuiteExceptions(Throwable exception) {
        LOG.debug("method invoked [{}]", exception);
        Suite runningSuite = SuiteConfiguration.getInstance().getSuite();
        LOG.info("Checking exceptions for running suite");
        if (isExceptionRegisteredInHolder(runningSuite, exception)) {
            ScejException runningSuiteExceptions = new ScejSuiteException(exception);
            runningSuite.setThrownException(runningSuiteExceptions);
            LOG.warn("Throwing suite fail exception, reason [{}]", exception.getMessage());
            throw runningSuiteExceptions;
        }
        LOG.debug("method finished");
    }

    private void checkTestExceptions(Throwable exception) {
        LOG.debug("method invoked [{}]", exception);
        Test currentSuiteTest = TestContext.getInstance().getTest();
        LOG.info("Checking exceptions for current test");
        if (isExceptionRegisteredInHolder(currentSuiteTest, exception)) {
            ScejException testRunningException = new ScejTestException(exception);
            currentSuiteTest.setThrownException(testRunningException);
            LOG.warn("Throwing test fail exception, reason [{}]", exception.getMessage());
            throw testRunningException;
        }
        LOG.debug("method finished");

    }

    private void checkSpecificationExceptions(Throwable exception) {
        LOG.debug("method invoked [{}]", exception);
        TestContext.SpecificationContext currentTextContext = TestContext.getInstance().getCurrentSpecificationContext();
        Specification currentSpecification = currentTextContext.getSpecification();

        LOG.info("Checking exceptions for current test");
        if (isExceptionRegisteredInHolder(currentSpecification, exception)) {
            ScejException testRunningException = new ScejSpecificationException(exception);
            currentSpecification.setThrownException(testRunningException);
            LOG.warn("Throwing specification fail exception, reason [{}]", exception.getMessage());
            throw testRunningException;
        }
        LOG.debug("method finished");
    }

    private boolean isExceptionRegisteredInHolder(ExceptionsHolder exHolder, Throwable exception) {
        LOG.debug("method invoked [{}]", exception);

        Exceptions specificationExceptions = exHolder.getExceptions();

        if (specificationExceptions != null) {
            LOG.info("checking test exceptions for match");
            if (specificationExceptions.isRegistered(exception)) {
                LOG.info("Exception [{}] is registered in current holder", exception);
                return true;
            }
        }
        LOG.info("No fail exceptions registered in holder");
        LOG.debug("method finished");
        return false;
    }
}
