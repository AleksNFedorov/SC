package com.scej.core.runner;

import com.scej.core.concordion.TestContext;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

/**
 * User: Fedorovaleks
 * Date: 13.03.14
 */
public class ScejRunner extends ConcordionRunner {

    public ScejRunner(Class<?> fixtureClass) throws InitializationError {
        super(fixtureClass);
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        try {
            super.runChild(method, notifier);
        } finally {
            TestContext.getInstance().destroyCurrentSpecificationContext();
        }
    }
}
