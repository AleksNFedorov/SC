package com.scejtesting.core.context;

import com.scejtesting.core.config.Test;

/**
 * User: Fedorovaleks
 * Date: 3/20/14
 */
public class TestContextService {

    public TestContext getCurrentTestContext() {
        return TestContext.getInstance();
    }

    public void createNewTestContext(Test test) {
        TestContext.createTestContext(test);
    }
}
