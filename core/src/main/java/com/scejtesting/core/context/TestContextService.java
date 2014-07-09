package com.scejtesting.core.context;

import com.scejtesting.core.config.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: Fedorovaleks
 * Date: 3/20/14
 */
public class TestContextService {

    private static final ConcurrentHashMap<Integer, TestContext> contexts = new ConcurrentHashMap<Integer, TestContext>();
    private static Lock contextLock = new ReentrantLock(false);

    public TestContext getCurrentTestContext() {
        return TestContext.getInstance();
    }

    public void createNewTestContext(Test test) {
        TestContext.createTestContext(test);
    }

    public void destroyTestContext() {
        TestContext.destroyTestContext();
    }
}
