package com.scejtesting.core.concordion.extension.specificationprocessing;

import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.Element;
import org.concordion.api.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aleks on 6/28/14.
 */
public class ResultsBreadcumbBuilder {

    protected static final Logger LOG = LoggerFactory.getLogger(ResultsBreadcumbRendererProcessingListener.class);

    private final TestContext currentTestContext = new TestContextService().getCurrentTestContext();

    public List<Element> buildResultThumbs() throws IOException {

        LOG.debug("Method invoked");

        List<TestContext.SpecificationContext> currentSpecificationFullStack =
                getTestContext().getSpecificationStack();

        List<Element> generatedThumbLinks = new ArrayList<Element>();

        if (currentSpecificationFullStack.size() > 1) {

            List<SpecificationBreadcumb> specFullPathList = buildCurrentSpecRootRelativePath(currentSpecificationFullStack);
            LOG.info("Root related links generated");

            for (SpecificationBreadcumb specificationThump : specFullPathList) {
                Element newLink = buildLink(specificationThump);
                generatedThumbLinks.add(newLink);
                LOG.debug("New link generated [{}]", newLink);
            }
        }

        LOG.debug("method finished");

        return generatedThumbLinks;
    }

    private List<SpecificationBreadcumb> buildCurrentSpecRootRelativePath(List<TestContext.SpecificationContext> specStack) {

        List<SpecificationBreadcumb> specsFullPathList = new ArrayList<SpecificationBreadcumb>();

        Resource parentSpecResource = getTestContext().getCurrentSpecificationContext().getCurrentTestResource();
        String currentSpecRelativePath = getTestContext().getCurrentSpecificationContext().getSpecification().getRealPath();
        Resource currentSpecificationResource = parentSpecResource.getRelativeResource(currentSpecRelativePath);

        for (int i = 1; i < specStack.size(); ++i) {
            String processingSpecLocation = specStack.get(i - 1).getSpecification().getRealPath();
            Resource specResource = specStack.get(i).getCurrentTestResource();
            String relativePath = currentSpecificationResource.getRelativePath(specResource);

            SpecificationBreadcumb thumb;

            if (i == 1) {
                thumb = new SpecificationBreadcumb(getTestContext().getTest().getName(), relativePath);
            } else {
                thumb = new SpecificationBreadcumb(buildSpecificationName(processingSpecLocation), relativePath);
            }
            specsFullPathList.add(thumb);
        }
        return specsFullPathList;
    }

    private Element buildLink(SpecificationBreadcumb targetSpecThumb) throws IOException {
        nu.xom.Element hrefElement = new nu.xom.Element("a");
        hrefElement.appendChild(new nu.xom.Text(targetSpecThumb.getLinkName()));
        hrefElement.addAttribute(new nu.xom.Attribute("href", targetSpecThumb.getPath()));
        return new org.concordion.api.Element(hrefElement);
    }

    private String buildSpecificationName(String specPath) {
        Element element = getTestContext().getChildSpecificationElement(specPath);
        return element.getText();
    }

    protected TestContext getTestContext() {
        return currentTestContext;
    }

    private class SpecificationBreadcumb {
        private final String linkName;
        private final String path;

        private SpecificationBreadcumb(String linkName, String path) {
            this.linkName = linkName;
            this.path = path;
        }

        public String getLinkName() {
            return linkName;
        }

        public String getPath() {
            return path;
        }
    }

}
