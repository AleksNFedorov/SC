package com.scejtesting.core.concordion.extension.specificationprocessing;

import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.api.listener.SpecificationProcessingListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by aleks on 7/21/14.
 */
public class AsyncLaunchResultsProcessor implements SpecificationProcessingListener {

    protected static final Logger LOG = LoggerFactory.getLogger(AsyncLaunchResultsProcessor.class);

    private final TestContext currentTestContext = new TestContextService().getCurrentTestContext();

    @Override
    public void beforeProcessingSpecification(SpecificationProcessingEvent event) {

    }

    @Override
    public void afterProcessingSpecification(SpecificationProcessingEvent event) {
        LOG.debug("method invoked");

        TestContext.SpecificationContext specificationContext = getTestContext().getCurrentSpecificationContext();

        LOG.info("There are [{}] async calls to wait", specificationContext.getRunningAsyncCallsAmount());

        while (specificationContext.getRunningAsyncCallsAmount() > 0) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                LOG.error("Async calls waiting interrupted [{}]", e.getMessage());
            }
        }

        LOG.info("method finished");
    }

    protected TestContext getTestContext() {
        return currentTestContext;
    }

}
