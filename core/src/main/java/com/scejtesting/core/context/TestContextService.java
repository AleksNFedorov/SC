package com.scejtesting.core.context;

import com.scejtesting.core.config.Test;
import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: Fedorovaleks
 * Date: 3/20/14
 */
public class TestContextService {

    protected static final Logger LOG = LoggerFactory.getLogger(TestContextService.class);

    static final Map<Integer, TestContext> contexts = new ConcurrentHashMap<Integer, TestContext>();
    private static final AtomicInteger contextIdToUse = new AtomicInteger(TestContext.DESTROYED_CONTEXT);
    private static Integer lastReplacedContextId = TestContext.DESTROYED_CONTEXT;

    public void dropContext(TestContext context) {
        LOG.debug("droping context [{}]", context);

        checkContext(context);
        TestContext sourceContext = contexts.remove(context.getContextId());

        if (sourceContext.getContextId().equals(contextIdToUse.get())) {
            LOG.warn("Current context set to null");
            contextIdToUse.set(TestContext.DESTROYED_CONTEXT);
        }

        sourceContext.destroyTestContext();
    }

    public TestContext cloneContext(TestContext testContext) {
        LOG.debug("Cloning context [{}]", testContext);
        checkContext(testContext);
        TestContext clonedContext = testContext.clone();
        contexts.put(clonedContext.getContextId(), clonedContext);

        return clonedContext;
    }

    public TestContext getCurrentTestContext() {
        LOG.debug("getting current context [{}]", contextIdToUse.get());

        TestContext requestedContext = contexts.get(contextIdToUse.get());

        Check.notNull(requestedContext, "No context for id [" + contextIdToUse.get() + "]");

        return requestedContext;
    }

    public TestContext getTestContext(Integer contextIndex) {
        LOG.debug("getting context [{}]", contextIndex);
        checkContextId(contextIndex);

        TestContext requestedContext = contexts.get(contextIndex);

        if (requestedContext == null) {
            LOG.error("No context for id [{}]", contextIndex);
        }

        return requestedContext;
    }

    private void checkContextId(Integer contextId) {
        Check.notNull(contextId, "Context index must be specified");
        Check.isTrue(contextId > 0, "Context index must be above zero");
    }

    private void checkContext(TestContext context) {
        Check.notNull(context, "Context must be specified");
        Check.isFalse(TestContext.isDestroyedContext(context), "Destroyed context not allowed to use");
    }

    public TestContext createNewTestContext(Test test) {
        LOG.debug("Creating new test context for test [{}]", test);
        Check.notNull(test, "Test instance must be specified");

        TestContext newContext = new TestContext(test);
        LOG.info("New test context created [{}]", newContext.getContextId());

        contexts.clear();
        contexts.put(newContext.getContextId(), newContext);
        contextIdToUse.set(newContext.getContextId());
        return newContext;

    }

    public synchronized void revertContextSwitch() {
        if (lastReplacedContextId.equals(TestContext.DESTROYED_CONTEXT)) {
            LOG.warn("No need revert context switch");
            return;
        }
        contextIdToUse.set(lastReplacedContextId);
        lastReplacedContextId = TestContext.DESTROYED_CONTEXT;
        this.notifyAll();
        LOG.info("Context initialized to [{}]", contextIdToUse.get());
    }

    public synchronized void switchContext(TestContext context) {
        Check.notNull(context, "Context must be specified");
        while (!lastReplacedContextId.equals(TestContext.DESTROYED_CONTEXT)) {
            try {
                this.wait(1000);
            } catch (InterruptedException e) {
                LOG.error("Exception during wait [{}]", e.getMessage(), e);
            }
        }
        lastReplacedContextId = contextIdToUse.get();
        contextIdToUse.set(context.getContextId());
        LOG.info("Context [{}] switched to [{}]", lastReplacedContextId, contextIdToUse.get());
    }
}
