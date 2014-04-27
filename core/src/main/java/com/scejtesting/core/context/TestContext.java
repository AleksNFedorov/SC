package com.scejtesting.core.context;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import org.concordion.api.Resource;
import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class TestContext extends Context {

    protected static final Logger LOG = LoggerFactory.getLogger(TestContext.class);


    private static TestContext inst;
    private final Stack<SpecificationContext> contextStack = new Stack<SpecificationContext>();
    private Test test;

    private TestContext(Test test) {
        LOG.debug("method invoked [{}]", test);

        this.test = test;
        createTopLevelTestContext();
    }

    static void createTestContext(Test test) {
        LOG.debug("method invoked [{}]", test);
        LOG.info("Initializing new context");

        Check.notNull(test, "Test can't be null");
        Check.isTrue(inst == null, "Test context already initialized");

        inst = new TestContext(test);

        LOG.info("New test context instance created");

        LOG.debug("method finished");
    }

    static TestContext getInstance() {
        LOG.debug("method invoked");
        return inst;
    }

    public static void destroyTestContext() {
        LOG.debug("method invoked");
        Check.notNull(inst, "There is not active test context");
        Check.isTrue(inst.contextStack.size() == 1, "Attempt to destroy non root context, use [destroyCurrentSpecificationContext] instead");
        inst.contextStack.pop();
        LOG.info("Top level specification context has been destroyed, destorying global context");
        inst = null;
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

    @Override
    public String toString() {
        return "TestContext{" +
                ", stack size='" + contextStack.size()
                + '}';
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

        public SpecificationResultRegistry getResultRegistry() {
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
