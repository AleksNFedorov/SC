package com.scejtesting.core.concordion;

import com.scejtesting.core.config.*;
import com.scejtesting.core.context.SpecificationResultRegistry;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.Resource;
import org.concordion.api.Result;
import org.concordion.api.RunnerResult;
import org.concordion.internal.FailFastException;
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
                result = finishTestWithResult(Result.IGNORED);
            }

            LOG.debug("Specification [{}] execution result [{}]", specification, result.getResult());

            return result;
        } catch (FailFastException ex) {
            LOG.error("Exception during specification executing [{}]", ex.getMessage(), ex);
            result = finishTestWithResult(Result.EXCEPTION);
        } catch (Throwable ex) {
            LOG.error("Exception during specification executing [{}]", ex.getMessage(), ex);
            adjustResultOnException(Result.FAILURE);
            result = finishTestWithResult(Result.FAILURE);
            throw new RuntimeException(ex);
        } finally {
            destroyContextAndPopulateResults(result);
            LOG.debug("method finished");
        }
        return result;
    }

    private void adjustResultOnException(Result result) {
        SpecificationResultRegistry executedSpecificationRegistry = getCurrentTestContext().
                getCurrentSpecificationContext().getResultRegistry();
        executedSpecificationRegistry.addResult(new RunnerResult(result));
    }

    private RunnerResult finishTestWithResult(Result result) {
        new TestContextService().revertContextSwitch();
        return new RunnerResult(result);
    }

    protected RunnerResult executeSpecification(Specification specification, Resource specificationResource, String href) throws Exception {
        LOG.debug("method invoked [{}], [{}]");

        getCurrentTestContext().createNewSpecificationContext(specificationResource, specification);
        LOG.info("Specification context created");
        contextCreated = true;

        RunnerResult result = executeSpecificationParent(specificationResource, href);

        LOG.info("Result is ready [{}]", result);

        return result;
    }

    RunnerResult executeSpecificationParent(Resource specificationResource, String href) throws Exception {
        return super.execute(specificationResource, href);
    }

    private void destroyContextAndPopulateResults(RunnerResult result) {

        TestContext.SpecificationContext specificationContext = getCurrentTestContext().getCurrentSpecificationContext();
        SpecificationResultRegistry executedSpecificationRegistry = specificationContext.getResultRegistry();

        LOG.info("Execution result [{}] stored ", result.getResult());

        if (contextCreated) {

            getCurrentTestContext().destroyCurrentSpecificationContext();

            LOG.info("Specification [{}] context destroyed", specificationContext);

            SpecificationResultRegistry parentSpecificationRegistry = getCurrentTestContext().getCurrentSpecificationContext().getResultRegistry();

            parentSpecificationRegistry.addResult(executedSpecificationRegistry, result);

            LOG.info("Specification [{}] results added to parent registry ", specificationContext);
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
        } finally {
            LOG.debug("method finished");
        }
    }

    protected SpecificationLocatorService getSpecificationLocationService() {
        return new SpecificationLocatorService();
    }

    protected TestContext getCurrentTestContext() {
        return currentTestContext;
    }

}
