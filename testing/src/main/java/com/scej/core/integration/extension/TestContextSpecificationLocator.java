package com.scejtesting.core.integration.extension;

import com.scejtesting.core.integration.GlobalTestContext;
import org.concordion.api.Resource;
import org.concordion.api.SpecificationLocator;
import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 * Date: 01.01.14
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 */
public class TestContextSpecificationLocator implements SpecificationLocator {

    private final Logger LOG = LoggerFactory.getLogger(TestContextSpecificationLocator.class);


    public Resource locateSpecification(Object fixture) {
        LOG.debug("method invoked [{}]", fixture);
        try {
            LOG.debug("Getting global test context");
            GlobalTestContext.SpecificationContext specificationContext = GlobalTestContext.getInstance().getCurrentTestContext();
            LOG.info("Test context aquired");
            LOG.debug("Test context [{}]", specificationContext);
            return buildSpecificationResource(specificationContext);
        } catch (RuntimeException ex) {
            LOG.error("Exception during specification lookup", ex);
            throw ex;
        } finally {
            LOG.debug("Method finished");
        }
    }

    private Resource buildSpecificationResource(GlobalTestContext.SpecificationContext context) {
        LOG.debug("method invoked [{}]", context);
        Check.notNull(context, "Context is null");
        try {
            String specificationHref = context.getSpecification().getRealPath();
            Resource specificationResource;
            if (isRelativePath(specificationHref)) {
                LOG.info("Specification href detected as relative");
                specificationResource = buildRelativePathSpecificationResource(context);
            } else {
                LOG.info("Specification href detected as not relative");
                specificationResource = buildAbsolutePathSpecificationResource(context);
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

    private Resource buildAbsolutePathSpecificationResource(GlobalTestContext.SpecificationContext context) {
        LOG.debug("method invoked [{}]", context);
        String specificationHref = context.getSpecification().getRealPath();
        Resource absolutePathSpecResource = new Resource(specificationHref);
        LOG.info("absolute path resource has been build");
        LOG.debug("method finished [{}]", absolutePathSpecResource);
        return absolutePathSpecResource;
    }

    private Resource buildRelativePathSpecificationResource(GlobalTestContext.SpecificationContext context) {
        LOG.debug("method invoked [{}]", context);
        Resource invokerResource = context.getCurrentTestResource();
        String specificationHref = context.getSpecification().getRealPath();

        LOG.debug("Spec href [{}], and resource [{}]", specificationHref, invokerResource);

        Resource specificationResource = invokerResource.getRelativeResource(specificationHref);
        LOG.info("Relative path spec resource has been build");
        LOG.debug("method finished [{}]", specificationResource);
        return specificationResource;
    }

    public static boolean isRelativePath(String path) {
        return !path.startsWith("/");
    }
}
