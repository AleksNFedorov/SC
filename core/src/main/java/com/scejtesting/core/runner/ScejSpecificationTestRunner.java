package com.scejtesting.core.runner;

import com.scejtesting.core.context.SpecificationResultRegistry;
import com.scejtesting.core.context.TestContextService;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

/**
 * User: Fedorovaleks
 * Date: 13.03.14
 */
public class ScejSpecificationTestRunner extends ConcordionRunner {

    public ScejSpecificationTestRunner(Class<?> fixtureClass) throws InitializationError {
        super(fixtureClass);
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        try {
            super.runChild(method, notifier);
        } finally {
            completeSpecExecution();
        }
    }

    private void completeSpecExecution() {
        TestContextService service = buildTestContextService();

        SpecificationResultRegistry finishedSpecificationRegistry = service.getCurrentTestContext().getCurrentSpecificationContext().getResultRegistry();

        buildTestContextService().getCurrentTestContext().destroyCurrentSpecificationContext();

        SpecificationResultRegistry parentSpecificationRegistry = service.getCurrentTestContext().getCurrentSpecificationContext().getResultRegistry();

        parentSpecificationRegistry.addAll(finishedSpecificationRegistry);


    }

    protected TestContextService buildTestContextService() {
        return new TestContextService();
    }
}
