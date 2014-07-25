package com.scejtesting.core.concordion.extension.specificationprocessing;

import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.api.listener.SpecificationProcessingListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    }

    protected TestContext getTestContext() {
        return currentTestContext;
    }

}
