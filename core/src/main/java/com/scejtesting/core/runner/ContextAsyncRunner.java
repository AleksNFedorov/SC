package com.scejtesting.core.runner;

import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;

/**
 * Created by aleks on 7/22/14.
 */
public abstract class ContextAsyncRunner<T> extends ContextSyncRunner<T> {

    private final TestContextService service = new TestContextService();
    private TestContext newContext;

    @Override
    public T runSync(TestContext context) {
        newContext = service.cloneContext(context);
        return super.runSync(newContext);
    }

}
