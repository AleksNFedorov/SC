package com.scejtesting.core.context;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import org.concordion.api.Resource;
import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class TestContext extends Context implements Cloneable {

    protected static final Logger LOG = LoggerFactory.getLogger(TestContext.class);
    private static final AtomicInteger contextSequence = new AtomicInteger(0);

    private final Stack<SpecificationContext> contextStack = new Stack<SpecificationContext>();
    private Test test;
    private Integer contextId = contextSequence.getAndIncrement();

    public TestContext(Test test) {
        LOG.debug("method invoked [{}]", test);
        Check.notNull(test, "Test can't be null");

        this.test = test;
        createTopLevelTestContext();
    }

    public void destroyTestContext() {
        LOG.debug("method invoked");
        Check.isTrue(contextStack.size() == 1, "Attempt to destroy non root context, use [destroyCurrentSpecificationContext] instead");
        contextStack.pop();
        LOG.info("Top level specification context has been destroyed, destorying global context");
        LOG.debug("method finished");
    }

    private void createTopLevelTestContext() {
        LOG.debug("method invoked");
        contextStack.push(new SpecificationContext(null, test.getSpecification()));
        LOG.info("top level test context created");
        LOG.debug("method finished");
    }

    public final Test getTest() {
        LOG.debug("method invoked");
        return test;
    }

    public void createNewSpecificationContext(Resource resource, Specification specification) {
        LOG.debug("method invoked [{}], [{}]", resource, specification);
        SpecificationContext context = new SpecificationContext(resource, specification);
        LOG.info("new test context created [{}]", context);
        contextStack.push(context);
        LOG.debug("context stack has been updated, size [{}]", contextStack.size());
        LOG.debug("method finished");
    }

    public SpecificationContext getCurrentSpecificationContext() {
        LOG.debug("method invoked");
        SpecificationContext currentSpecificationContext = contextStack.peek();
        LOG.debug("method finished");
        return currentSpecificationContext;
    }

    public void destroyCurrentSpecificationContext() {
        LOG.debug("method invoked");
        Check.isTrue(contextStack.size() > 1, "Attempt to destroy root test context");
        contextStack.pop();
        LOG.debug("context stack has been updated [{}]", contextStack.size());
        LOG.debug("method finished");
    }

    public List<SpecificationContext> getSpecificationStack() {
        return new ArrayList<SpecificationContext>(contextStack);
    }

    @Override
    public String toString() {
        return "TestContext{" +
                ", stack size='" + contextStack.size()
                + '}';
    }

    public Integer getContextId() {
        return contextId;
    }

    @Override
    protected TestContext clone() {
        TestContext clonedContext = new TestContext(this.test);
        clonedContext.contextStack.addAll(this.contextStack);

        LOG.info("Context [{}] cloned to [{}]", getContextId(), clonedContext.getContextId());

        return clonedContext;
    }

    public class SpecificationContext {
        private final Resource currentTestResource;
        private final Specification specification;
        private final SpecificationResultRegistry resultRegistry;

        private SpecificationContext(Resource currentTestResource, Specification specification) {
            LOG.debug("constructor invoked [{}], [{}]", currentTestResource, specification);
            Check.notNull(specification, "Specification can't be null");

            this.currentTestResource = currentTestResource;
            this.specification = specification;
            resultRegistry = new SpecificationResultRegistry();
        }

        public Resource getCurrentTestResource() {
            return currentTestResource;
        }

        public Specification getSpecification() {
            return specification;
        }

        public synchronized SpecificationResultRegistry getResultRegistry() {
            return resultRegistry;
        }

        @Override
        public String toString() {
            return "SpecificationContext{" +
                    ", currentTestResource=" + currentTestResource +
                    ", specification=" + specification +
                    '}';
        }
    }
}
