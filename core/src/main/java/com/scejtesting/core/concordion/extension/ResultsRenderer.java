package com.scejtesting.core.concordion.extension;

import com.scejtesting.core.context.SpecificationResultRegistry;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.Result;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.api.listener.SpecificationProcessingListener;

/**
 * Created by aleks on 4/26/14.
 */
public class ResultsRenderer implements SpecificationProcessingListener {

    @Override
    public void beforeProcessingSpecification(SpecificationProcessingEvent event) {
        System.out.println("Before processing invoked !!!");
    }

    @Override
    public void afterProcessingSpecification(SpecificationProcessingEvent event) {

        SpecificationResultRegistry registiry = getTestContextService().getCurrentTestContext().getCurrentSpecificationContext().getResultRegistry();

        System.out.println("Total success is [" + registiry.getResultsList(Result.SUCCESS).size() + "]");
        System.out.println("Total ignored is [" + registiry.getResultsList(Result.IGNORED).size() + "]");

        System.out.println("after processing invoked !!!");

    }

    protected TestContextService getTestContextService() {
        return new TestContextService();
    }
}
