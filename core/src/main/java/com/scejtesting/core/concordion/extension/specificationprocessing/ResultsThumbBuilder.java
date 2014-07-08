package com.scejtesting.core.concordion.extension.specificationprocessing;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.SpecificationLocatorService;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aleks on 6/28/14.
 */
public class ResultsThumbBuilder {

    protected static final Logger LOG = LoggerFactory.getLogger(ResultsThumbRendererProcessingListener.class);

    public List<Element> buildResultThumbs() throws IOException {

        LOG.debug("Method invoked");

        List<TestContext.SpecificationContext> currentSpecificationFullStack =
                getTestContextService().getCurrentTestContext().getSpecificationStack();

        List<Element> generatedThumbLinks = new ArrayList<Element>();

        if (currentSpecificationFullStack.size() > 1) {

            List<String> specFullPathList = buildCurrentSpecRootRelativePath(currentSpecificationFullStack);
            LOG.info("Root related links generated");

            String currentSpecPath = specFullPathList.get(specFullPathList.size() - 1);

            for (int i = 0; i < specFullPathList.size() - 1; ++i) {
                Element newLink = buildLink(specFullPathList.get(i), currentSpecPath);
                generatedThumbLinks.add(newLink);
                LOG.debug("New link generated [{}]", newLink);
            }
        }

        LOG.debug("method finished");

        return generatedThumbLinks;
    }

    private Element buildLink(String pathToTarget, String currentSpecPath) throws IOException {
        String linkName = extractSpecNameFromPath(pathToTarget);
        String relativePathToTarget = getRelativePath(pathToTarget, currentSpecPath);

        nu.xom.Element hrefElement = new nu.xom.Element("a");
        hrefElement.appendChild(new nu.xom.Text(linkName));
        hrefElement.addAttribute(new nu.xom.Attribute("href", relativePathToTarget));

        return new org.concordion.api.Element(hrefElement);
    }

    private String extractSpecFileFromPath(String fullSpecPath) {
        return new File(fullSpecPath).getName();
    }

    private String extractSpecNameFromPath(String fullSpecPath) {
        String specFile = extractSpecFileFromPath(fullSpecPath);
        int extensionIndex = specFile.lastIndexOf(".");
        return SpecificationLocatorService.cleanSuffix(specFile.substring(0, extensionIndex));
    }

    private String getRelativePath(String targetFile, String baseFile) {

        //Calculate path deep
        int foldersCount = 0;
        for (int i = 0; i < baseFile.length(); ++i) {
            if (baseFile.charAt(i) == '/')
                foldersCount++;
        }

        //Build relative path to top
        StringBuilder pathToTop = new StringBuilder();
        for (int i = 0; i < foldersCount - 1; ++i) {
            pathToTop.append("../");
        }

        // if same level
        if (pathToTop.length() == 0) {
            pathToTop.append(targetFile);
            //delete leading /
            pathToTop.deleteCharAt(0);
        } else {
            pathToTop.setLength(pathToTop.length() - 1);
            pathToTop.append(targetFile);
        }

        return pathToTop.toString();

    }

    private List<String> buildCurrentSpecRootRelativePath(List<TestContext.SpecificationContext> specStack) {

        List<String> specsFullPathList = new ArrayList<String>();

        Specification currentSpecification = specStack.get(0).getSpecification();
        Specification parentSpecification = currentSpecification;

        String specAbsolutePath = "/" + extractSpecFileFromPath(parentSpecification.getLocation());
        specsFullPathList.add(specAbsolutePath);

        for (int i = 1; i < specStack.size(); ++i) {
            currentSpecification = specStack.get(i).getSpecification();
            String processingSpecLocation = getRealSpecLocation(parentSpecification, currentSpecification);
            String parentFolder = new File(specAbsolutePath).getParent();
            specAbsolutePath = new File(parentFolder, processingSpecLocation).getPath();
            specsFullPathList.add(specAbsolutePath);
            parentSpecification = currentSpecification;
        }

        return specsFullPathList;
    }

    private String getRealSpecLocation(Specification parentSpecification, Specification specification) {
        return new SpecificationLocatorService().buildUniqueSpecificationHREF(parentSpecification, specification.getLocation());
    }

    protected TestContextService getTestContextService() {
        return new TestContextService();
    }

}
