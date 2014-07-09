package com.scejtesting.core.context;

import com.scejtesting.core.config.Test;
import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: Fedorovaleks
 * Date: 3/20/14
 */
public class TestContextService {

    protected static final Logger LOG = LoggerFactory.getLogger(TestContextService.class);

    private static final ConcurrentHashMap<Integer, TestContext> contexts = new ConcurrentHashMap<Integer, TestContext>();

    private static Lock contextLock = new ReentrantLock(false);

    private static final AtomicBoolean extensionInitialized = new AtomicBoolean(true);

    private static final AtomicInteger contextIdToUse = new AtomicInteger(0);

    public TestContext cloneContext(Integer contextIndex) {
        LOG.debug("Cloning context [{}]", contextIndex);
        checkContextId(contextIndex);

        TestContext sourceContext = contexts.get(contextIndex);
        LOG.info("Context with id [{}] resolved [{}]", contextIndex, sourceContext);

        Check.notNull(sourceContext, "Context not found [" + contextIndex + "]");
        TestContext clonedContext = sourceContext.clone();
        contexts.put(clonedContext.getContextId(), clonedContext);

        return clonedContext;
    }

    public TestContext getCurrentTestContext() {
        LOG.debug("getting current context [{}]", contextIdToUse.get());

        TestContext requestedContext = contexts.get(contextIdToUse.get());

        if (requestedContext == null) {
            LOG.error("No context for id [{}]", contextIdToUse.get());
        }

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

    public void setExtensionInitialized() {
        extensionInitialized.set(true);
    }

    public void setContextIdToUse(Integer contextId) {
        contextIdToUse.set(contextId);
        extensionInitialized.set(false);
    }

    public void waitForInitialization() {
        while (!extensionInitialized.get()) {
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
        }
    }
}
