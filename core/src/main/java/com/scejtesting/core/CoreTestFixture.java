package com.scejtesting.core;

import com.scejtesting.core.concordion.extension.ScejCoreExtensions;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.extension.Extensions;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
@RunWith(ConcordionRunner.class)
@Extensions(value = ScejCoreExtensions.class)
public final class CoreTestFixture {

    public Integer getTestContextIndex() {
        return new TestContextService().getCurrentTestContext().getContextId();
    }

}
