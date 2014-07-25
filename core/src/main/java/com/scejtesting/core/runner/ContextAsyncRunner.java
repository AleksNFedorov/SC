package com.scejtesting.core.runner;

import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;

/**
 * Created by aleks on 7/22/14.
 */
public abstract class ContextAsyncRunner<T> extends ContextSyncRunner<T> {

    private final TestContextService service = new TestContextService();
    private TestContext originalContext;
    private TestContext newContext;

    @Override
    public T runSync(TestContext context) {
        service.lock();
        this.originalContext = context;
        newContext = service.cloneContext(context);
        return super.runSync(newContext);
    }

    public TestContext getNewContext() {
        return newContext;
    }

    @Override
    protected void onInitialized() {
        service.setContextIdToUse(originalContext.getContextId());
    }
}
