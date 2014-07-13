package com.scejtesting.core.runner;

import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aleks on 7/12/14.
 */
public abstract class ContextSyncRunner<T> {

    private static final Logger LOG = LoggerFactory.getLogger(ScejStandAloneRunner.class);

    public abstract T runCallBack(TestContext context);

    public final T synchronizeContext(Integer testContextIndex) {
        LOG.debug("method invoked [{}]", testContextIndex);

        Check.notNull(testContextIndex, "Test context index must be specified");
        Check.isTrue(!testContextIndex.equals(TestContext.DESTROYED_CONTEXT), "Cant use destroyed context");

        TestContextService service = getTestContextService();
        TestContext testContext = service.getTestContext(testContextIndex);

        LOG.info("Test context aquired ");

        service.lock();
        service.setContextIdToUse(testContext.getContextId());

        T result = runCallBack(testContext);
        LOG.info("Callback successfully finished");

        service.waitForInitialization();
        service.unLock();

        LOG.debug("method finished");

        return result;
    }

    TestContextService getTestContextService() {
        return new TestContextService();
    }

}
