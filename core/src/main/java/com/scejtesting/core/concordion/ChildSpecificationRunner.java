package com.scejtesting.core.concordion;

import com.scejtesting.core.config.*;
import com.scejtesting.core.context.SpecificationResultRegistry;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import com.scejtesting.core.runner.ContextSyncRunner;
import org.concordion.api.Resource;
import org.concordion.api.Result;
import org.concordion.api.RunnerResult;
import org.concordion.internal.runner.DefaultConcordionRunner;
import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class ChildSpecificationRunner extends DefaultConcordionRunner {

    private final static Logger LOG = LoggerFactory.getLogger(ChildSpecificationRunner.class);
    Specification specification;

    private TestContext currentTestContext;
    private boolean contextCreated;

    public ChildSpecificationRunner() {
        TestContextService service = new TestContextService();
        currentTestContext = service.getCurrentTestContext();
        service.setTestContextInitialized();
    }

    @Override
    public RunnerResult execute(Resource resource, String href) throws Exception {
        LOG.debug("method invoked [{}], [{}]", href, resource);
        Check.notNull(href, "Link to specification cna`t be null");

        RunnerResult result = null;
        contextCreated = false;

        try {
            specification = resolveSpecification(href);

            if (canRunSpecification(specification)) {
                result = executeSpecification(specification,
                        resource,
                        href);
            } else {
                result = new RunnerResult(Result.IGNORED);
            }

            LOG.debug("Specification [{}] execution result [{}]", specification, result.getResult());

            return result;
        } catch (RuntimeException ex) {
            LOG.error("Exception during specification executing [{}]", ex.getMessage(), ex);
            result = new RunnerResult(Result.EXCEPTION);
            throw ex;
        } finally {
            destroyContextAndPopulateResults(result);
            LOG.debug("method finished");
        }
    }

    protected RunnerResult executeSpecification(Specification specification, Resource specificationResource, String href) throws Exception {
        LOG.debug("method invoked [{}], [{}]");

        getCurrentTestContext().createNewSpecificationContext(specificationResource, specification);
        LOG.info("Specification context created");
        contextCreated = true;

        RunnerResult result = super.execute(specificationResource, href);
        LOG.info("Result is ready [{}]", result);

        return result;
    }

    private void destroyContextAndPopulateResults(RunnerResult result) {

        SpecificationResultRegistry executedSpecificationRegistry = getCurrentTestContext().getCurrentSpecificationContext().getResultRegistry();

        executedSpecificationRegistry.addResult(result);

        LOG.info("Execution result [{}] stored ", result.getResult());

        if (contextCreated) {

            getCurrentTestContext().destroyCurrentSpecificationContext();

            LOG.info("Specification context destroyed");

            SpecificationResultRegistry parentSpecificationRegistry = getCurrentTestContext().getCurrentSpecificationContext().getResultRegistry();

            parentSpecificationRegistry.addAll(executedSpecificationRegistry);

            LOG.info("Specification results added to parent registry");
        }

    }

    private boolean canRunSpecification(Specification specification) {
        LOG.debug("method invoked [{}]");
        Suite currentSuite = getSuite();
        Test currentTest = getCurrentTestContext().getTest();
        return (specification != null &&
                currentSuite.getThrownException() == null &&
                currentTest.getThrownException() == null);
    }

    protected Suite getSuite() {
        return SuiteConfiguration.getInstance().getSuite();
    }

    protected Specification resolveSpecification(String href) {
        LOG.debug("method invoked [{}]", href);
        TestContext.SpecificationContext specificationContext = getCurrentTestContext().getCurrentSpecificationContext();
        Specification currentSpecification = specificationContext.getSpecification();
        Specification specByHref = getSpecificationLocationService().getChildSpecificationByRealLocation(currentSpecification, href);
        LOG.info("Child specification has been resolved as [{}]", specByHref);
        return specByHref;
    }

    @Override
    protected Class<?> findTestClass(Resource resource, String href) throws ClassNotFoundException {
        LOG.debug("method invoked [{}], [{}]", resource, href);
        try {
            Class resolvedClass = getSpecificationLocationService().
                    resolveSpecificationClassByContext(specification, getCurrentTestContext().getTest());
            LOG.debug("Specification [{}] test class [{}]", href, resolvedClass);
            return resolvedClass;
        } catch (RuntimeException ex) {
            LOG.error("Exception during test class specification lookup [{}]", ex.getMessage());
            throw ex;
        } finally {
            LOG.debug("method finished");
        }
    }

    @Override
    protected org.junit.runner.Result runJUnitClass(final Class<?> concordionClass) {
        Integer contextId = getCurrentTestContext().getContextId();

        ContextSyncRunner<org.junit.runner.Result> runner = new ContextSyncRunner<org.junit.runner.Result>() {
            @Override
            public org.junit.runner.Result runCallBack(TestContext context) {
                return runParentJUnit(concordionClass);
            }
        };

        return runner.synchronizeContext(contextId);
    }

    private org.junit.runner.Result runParentJUnit(Class<?> concordionClass) {
        return super.runJUnitClass(concordionClass);
    }

    protected SpecificationLocatorService getSpecificationLocationService() {
        return new SpecificationLocatorService();
    }

    protected TestContext getCurrentTestContext() {
        return currentTestContext;
    }

}
