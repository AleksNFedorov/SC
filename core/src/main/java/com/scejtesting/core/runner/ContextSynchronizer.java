package com.scejtesting.core.runner;

import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aleks on 7/12/14.
 */
public abstract class ContextSynchronizer<T> {

    private static final Logger LOG = LoggerFactory.getLogger(ScejStandAloneRunner.class);

    public abstract T runCallBack(TestContext context);

    public T runSync(TestContext context) {
        LOG.debug("method invoked [{}]", context);

        Check.notNull(context, "Test context index must be specified");
        Check.isFalse(TestContext.isDestroyedContext(context), "Cant use destroyed context");

        TestContextService service = getTestContextService();

        LOG.info("Test context acquired ");

        T result = null;

        try {
            service.switchContext(context);

            result = runCallBack(context);
            LOG.info("Callback successfully finished");

        } catch (Exception ex) {
            LOG.error("Sync call execution exception [{}]", ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
        LOG.debug("method finished");

        return result;
    }

    TestContextService getTestContextService() {
        return new TestContextService();
    }

}
