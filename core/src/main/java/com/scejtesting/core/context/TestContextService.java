package com.scejtesting.core.context;

import com.scejtesting.core.config.Test;
import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: Fedorovaleks
 * Date: 3/20/14
 */
public class TestContextService {

    protected static final Logger LOG = LoggerFactory.getLogger(TestContextService.class);

    private static final ConcurrentHashMap<Integer, TestContext> contexts = new ConcurrentHashMap<Integer, TestContext>();

    private static Lock contextLock = new ReentrantLock(false);

    public TestContext getTestContext(Integer contextIndex) {
        LOG.debug("getting context [{}]", contextIndex);
        Check.notNull(contextIndex, "Context index must be specified");
        Check.isTrue(contextIndex > 0, "Context index must be above zero");

        TestContext requestedContext = contexts.get(contextIndex);

        if (requestedContext == null) {
            LOG.error("No context for id [{}]", contextIndex);
        }

        return requestedContext;
    }

    public TestContext createNewTestContext(Test test) {
        LOG.debug("Creating new test context for test [{}]", test);
        Check.notNull(test, "Test instance must be specified");

        TestContext newContext = new TestContext(test);
        LOG.info("New test context created [{}]", newContext.getContextId());

        contexts.clear();
        contexts.put(newContext.getContextId(), newContext);
        return newContext;

    }

    public void lock() {
        contextLock.lock();
    }

    public void unLock() {
        contextLock.unlock();
    }
}
