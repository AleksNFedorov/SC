package com.scejtesting.core.concordion.extension;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.Resource;
import org.concordion.api.SpecificationLocator;
import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class HierarchySpecificationLocator implements SpecificationLocator {

    private static final Logger LOG = LoggerFactory.getLogger(HierarchySpecificationLocator.class);

    private final TestContext currentTestContext = new TestContextService().getCurrentTestContext();

    public Resource locateSpecification(Object fixture) {
        LOG.debug("method invoked [{}]", fixture);
        try {
            LOG.debug("Getting global test context");
            TestContext.SpecificationContext specificationContext = getCurrentTestContext().getCurrentSpecificationContext();
            Check.notNull(specificationContext, "Context is null");
            LOG.info("Test context acquired");
            LOG.debug("Test context [{}]", specificationContext);
            return buildSpecificationResource(specificationContext);
        } catch (RuntimeException ex) {
            LOG.error("Exception during specification lookup", ex);
            throw ex;
        } finally {
            LOG.debug("Method finished");
        }
    }

    private Resource buildSpecificationResource(TestContext.SpecificationContext context) {
        LOG.debug("method invoked [{}]", context);
        try {
            Specification specification = context.getSpecification();
            Resource specificationResource;
            if (specification.isTopLevelSpecification()) {
                LOG.info("Specification detected as top level");
                specificationResource = buildAbsolutePathSpecificationResource(context);
            } else {
                LOG.info("Specification detected as child");
                specificationResource = buildRelativePathSpecificationResource(context);
            }

            LOG.debug("method finished, [{}]", specificationResource);
            return specificationResource;
        } catch (RuntimeException ex) {
            LOG.error("Exception during specification lookup", ex);
            throw ex;
        } finally {
            LOG.debug("Method finished");
        }
    }

    private Resource buildAbsolutePathSpecificationResource(TestContext.SpecificationContext context) {
        LOG.debug("method invoked [{}]", context);
        String specificationHref = context.getSpecification().getRealPath();
        Resource absolutePathSpecResource = new Resource(specificationHref);
        LOG.info("absolute path resource has been build");
        LOG.debug("method finished [{}]", absolutePathSpecResource);
        return absolutePathSpecResource;
    }

    private Resource buildRelativePathSpecificationResource(TestContext.SpecificationContext context) {
        LOG.debug("method invoked [{}]", context);
        Resource invokerResource = context.getCurrentTestResource();
        String specificationHref = context.getSpecification().getRealPath();

        LOG.debug("Spec href [{}], and resource [{}]", specificationHref, invokerResource);

        Resource specificationResource = invokerResource.getRelativeResource(specificationHref);
        LOG.info("Relative path spec resource has been build");
        LOG.debug("method finished [{}]", specificationResource);
        return specificationResource;
    }

    protected TestContext getCurrentTestContext() {
        return currentTestContext;
    }

}
