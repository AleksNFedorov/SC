package com.scej.core.integration;

import com.scej.core.config.Specification;
import com.scej.core.config.SpecificationLocatorService;
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
public class ChildTestRunner extends DefaultConcordionRunner {

    private static Logger LOG = LoggerFactory.getLogger(ChildTestRunner.class);

    @Override
    public RunnerResult execute(Resource resource, String href) throws Exception {
        LOG.debug("method invoked [{}], [{}]", href, resource);
        try {
            Specification specification = resolveSpecification(href);


            if (specification == null) {
                return new RunnerResult(Result.IGNORED);
            } else {
                GlobalTestContext.getInstance().createNewTestContext(resource, specification);
                LOG.info("Test context created");
                RunnerResult result = super.execute(resource, href);
                LOG.info("Result is ready [{}]", result);
                return result;
            }

        } catch (RuntimeException ex) {
            LOG.error("Exception during specification executing [{}]", ex.getMessage(), ex);
            throw ex;
        } finally {
            LOG.debug("method finished");
        }
    }

    public Specification resolveSpecification(String href) {
        LOG.debug("method invoked [{}]", href);
        Check.notNull(href, "Link to specification cna`t be null");
        GlobalTestContext.SpecificationContext specificationContext = GlobalTestContext.getInstance().getCurrentTestContext();
        Specification currentSpecification = specificationContext.getSpecification();
        Specification specByHref = SpecificationLocatorService.getService().getChildSpecificationByRealLocation(currentSpecification, href);
        LOG.info("Child specification has been resolved as [{}]", specByHref);
        return specByHref;
    }


    @Override
    protected Class<?> findTestClass(Resource resource, String href) throws ClassNotFoundException {
        LOG.debug("method invoked [{}], [{}]", resource, href);
        try {
            return GlobalTestContext.getInstance().getTest().getClazz();
        } catch (RuntimeException ex) {
            LOG.error("Exception during test class specification lookup [{}]", ex.getMessage());
            throw ex;
        } finally {
            LOG.debug("method finished");
        }
    }
}
