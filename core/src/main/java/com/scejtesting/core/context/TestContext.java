package com.scejtesting.core.context;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import org.concordion.api.Element;
import org.concordion.api.Resource;
import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class TestContext extends Context implements Cloneable {

    public static final Integer DESTROYED_CONTEXT = -1;

    protected static final Logger LOG = LoggerFactory.getLogger(TestContext.class);
    private static final AtomicInteger contextSequence = new AtomicInteger(0);
    private final Stack<SpecificationContext> contextStack = new Stack<SpecificationContext>();
    private Test test;
    private Integer contextId = contextSequence.incrementAndGet();
    private final Map<String, Element> childSpecificationsElements;


    public TestContext(Test test) {
        LOG.debug("method invoked [{}]", test);
        Check.notNull(test, "Test can't be null");

        this.test = test;
        childSpecificationsElements = new ConcurrentHashMap<String, Element>();
        createTopLevelTestContext();
    }

    public void destroyTestContext() {
        LOG.debug("method invoked");
        Check.isTrue(contextStack.size() == 1, "Attempt to destroy non root context, use [destroyCurrentSpecificationContext] instead");
        contextStack.pop();
        contextId = DESTROYED_CONTEXT;
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
        //Need to avoid root spec duplication, already added on creation
        clonedContext.contextStack.clear();
        clonedContext.contextStack.addAll(this.contextStack);
        clonedContext.childSpecificationsElements.putAll(this.childSpecificationsElements);
        copyTo(clonedContext);

        LOG.info("Context [{}] cloned to [{}]", getContextId(), clonedContext.getContextId());

        return clonedContext;
    }

    public void saveChildSpecificationElement(Element childSpecElement) {
        LOG.debug("Saving child specification element [{}]", childSpecElement);
        Check.notNull(childSpecElement, "Child spec element must be specified");
        String href = childSpecElement.getAttributeValue("href");
        Check.notEmpty(href, "Child spec element href must be specified");
        childSpecificationsElements.put(href, childSpecElement);
        LOG.info("Child specification element saved [{}]", href);
    }

    public Element getChildSpecificationElement(String href) {
        LOG.debug("Saving child specification element [{}]", href);
        Check.notEmpty(href, "Child spec element href must be specified");
        Element element = childSpecificationsElements.get(href);
        if (element == null) {
            LOG.warn("Unknown element for link [{}]", href);
        }

        LOG.debug("method finished");
        return element;

    }


    public static boolean isDestroyedContext(TestContext context) {
        Check.notNull(context, "Test context must be specified");
        return DESTROYED_CONTEXT.equals(context.getContextId());
    }



    public class SpecificationContext {
        private final Resource currentTestResource;
        private final Specification specification;
        private final SpecificationResultRegistry resultRegistry;
        private final List<Future> asyncCalls = new ArrayList<Future>();

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

        public void saveAsyncCall(Future call) {
            asyncCalls.add(call);
        }

        public List<Future> getAsyncCalls() {
            return new ArrayList<Future>(asyncCalls);
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
