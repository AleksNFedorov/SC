package com.scej.autotests.core.integration;

import com.scej.autotests.core.config.Specification;
import com.scej.autotests.core.config.Test;
import org.apache.http.annotation.NotThreadSafe;
import org.concordion.api.Resource;
import org.concordion.internal.util.Check;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 * Date: 01.01.14
 * Time: 13:16
 * To change this template use File | Settings | File Templates.
 */
@NotThreadSafe
public class GlobalTestContext {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalTestContext.class);

    private static GlobalTestContext inst;

    private RemoteWebDriver driver;
    private Test test;

    private final Stack<SpecificationContext> contextStack = new Stack<SpecificationContext>();

    public class DriverInitSpecificationContext extends SpecificationContext {

        private DriverInitSpecificationContext(SpecificationContext parent) {
            super(parent.getCurrentTestResource(), parent.getSpecification());
            LOG.info("DriverInitSpecificationContext created");
        }
    }

    public class SpecificationContext {
        private final Resource currentTestResource;
        private final Specification specification;

        private SpecificationContext(Resource currentTestResource, Specification specification) {
            LOG.debug("constructor invoked [{}], [{}]", currentTestResource, currentTestResource);
            Check.notNull(specification, "Specification can't be null");

            this.currentTestResource = currentTestResource;
            this.specification = specification;
        }

        public Resource getCurrentTestResource() {
            return currentTestResource;
        }

        public Specification getSpecification() {
            return specification;
        }

        public boolean isTopLevelTest() {
            return currentTestResource == null;
        }

        @Override
        public String toString() {
            return "SpecificationContext{" +
                    ", currentTestResource=" + currentTestResource +
                    ", specification=" + specification +
                    '}';
        }
    }

    public static void createGlobalContext(Test test) {
        LOG.debug("method invoked [{}]", test);

        Check.notNull(test, "Test can't be null");

        LOG.info("Initializing new context");
        inst = new GlobalTestContext(test);
        LOG.info("test context instance created");
        LOG.debug("method finished");
    }

    public static GlobalTestContext getInstance() {
        LOG.debug("method invoked");
        return inst;
    }

    private GlobalTestContext(Test test) {
        LOG.debug("method invoked [{}]", test);
        this.test = test;
        createTopLevelTestContext();
    }

    public final void initWithSeleniumDriver(RemoteWebDriver driver) {
        LOG.debug("method invoked [{}]", driver);
        Check.notNull(driver, "Driver can't be null");
        if(this.driver != null)
            throw new RuntimeException("Driver already set");
        contextStack.push(new DriverInitSpecificationContext(contextStack.pop()));
        this.driver = driver;
    }

    private void createTopLevelTestContext() {
        LOG.debug("method invoked");
        contextStack.push(new SpecificationContext(null, test.getSpecification()));
        LOG.info("top level test context created");
        LOG.debug("method finished");
    }

    public final RemoteWebDriver getDriver() {
        LOG.debug("method invoked");
        return driver;
    }

    public final Test getTest() {
        LOG.debug("method invoked");
        return test;
    }

    public void createNewTestContext(Resource resource, Specification specification) {
        LOG.debug("method invoked [{}], [{}]", resource, specification);
        SpecificationContext context = new SpecificationContext(resource, specification);
        LOG.info("new test context created [{}]", context);
        contextStack.push(context);
        LOG.debug("context stack has been updated, size [{}]", contextStack.size());
        LOG.debug("method finished");
    }

    public SpecificationContext getCurrentTestContext() {
        LOG.debug("method invoked");
        SpecificationContext currentSpecificationContext = contextStack.peek();
        LOG.debug("method finished");
        return currentSpecificationContext;
    }


    public void destroyCurrentTestContext() {
        LOG.debug("method invoked");
        if(contextStack.pop() instanceof DriverInitSpecificationContext) {
            LOG.info("Driver specification processed, Web driver has been destoryed");
            driver = null;
        }

        if(contextStack.size() == 0) {
            LOG.info("Top level specification context has been destroyed, destorying global context");
            inst = null;
        }

        LOG.debug("context stack has been updated [{}]", contextStack.size());
        LOG.debug("method finished");
    }

    @Override
    public String toString() {
        return "GlobalTestContext{" +
                "driver=" + driver +
                ", stack size='" + contextStack.size()
                + '}';
    }
}
