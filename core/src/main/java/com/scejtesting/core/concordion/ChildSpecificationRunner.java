package com.scejtesting.core.concordion;

import com.scejtesting.core.config.*;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
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

    private final TestContextService testContextService;


    public ChildSpecificationRunner() {
        testContextService = buildTestContextService();
    }

    @Override
    public RunnerResult execute(Resource resource, String href) throws Exception {
        LOG.debug("method invoked [{}], [{}]", href, resource);
        Check.notNull(href, "Link to specification cna`t be null");

        RunnerResult result = null;

        try {
            Specification specification = resolveSpecification(href);


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
            getCurrentTestContext().getCurrentSpecificationContext().getResultRegistry().addResult(result);
            LOG.debug("method finished");
        }
    }

    protected RunnerResult executeSpecification(Specification specification, Resource specificationResource, String href) throws Exception {
        LOG.debug("method invoked [{}], [{}]");
        getCurrentTestContext().createNewSpecificationContext(specificationResource, specification);
        LOG.info("Test context created");
        RunnerResult result = super.execute(specificationResource, href);
        LOG.info("Result is ready [{}]", result);

        getCurrentTestContext().destroyCurrentSpecificationContext();
        return result;
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
        Specification specByHref = SpecificationLocatorService.getService().getChildSpecificationByRealLocation(currentSpecification, href);
        LOG.info("Child specification has been resolved as [{}]", specByHref);
        return specByHref;
    }


    @Override
    protected Class<?> findTestClass(Resource resource, String href) throws ClassNotFoundException {
        LOG.debug("method invoked [{}], [{}]", resource, href);
        try {
            return getSpecificationLocationService().resolveSpecificationClassByContext();
        } catch (RuntimeException ex) {
            LOG.error("Exception during test class specification lookup [{}]", ex.getMessage());
            throw ex;
        } finally {
            LOG.debug("method finished");
        }
    }


    protected SpecificationLocatorService getSpecificationLocationService() {
        return SpecificationLocatorService.getService();
    }


    protected TestContext getCurrentTestContext() {
        return testContextService.getCurrentTestContext();
    }

    protected TestContextService buildTestContextService() {
        return new TestContextService();
    }

}
