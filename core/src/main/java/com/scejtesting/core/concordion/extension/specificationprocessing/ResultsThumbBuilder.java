package com.scejtesting.core.concordion.extension.specificationprocessing;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.SpecificationLocatorService;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.Element;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by aleks on 6/28/14.
 */
public class ResultsThumbBuilder {

    public static final Pattern htmlSuffixReplacer = Pattern.compile("\\.[H|h][T|t][M|m][L|l]{0,1}");
    public static final Pattern pathReplacer = Pattern.compile(".*/");
    public static final Pattern firstSlashReplacer = Pattern.compile("^/");

    public List<Element> buildResultThumbs() throws IOException {

        List<TestContext.SpecificationContext> currentSpecificationFullStack =
                getTestContextService().getCurrentTestContext().getSpecificationStack();

        List<Element> generatedThumbLinks = new ArrayList<Element>();

        if (currentSpecificationFullStack.size() > 1) {

            List<String> specFullPathList = buildCurrentSpecRootRelativePath(currentSpecificationFullStack);
            String currentSpecPath = specFullPathList.get(specFullPathList.size() - 1);

            for (int i = 0; i < specFullPathList.size() - 1; ++i) {
                generatedThumbLinks.add(buildLink(specFullPathList.get(i), currentSpecPath));
            }
        }

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
        return specFile.substring(0, extensionIndex);
    }

    private String getRelativePath(String targetFile, String baseFile) {
        String baseFileFolder = new File(baseFile).getParent();

        //Calculate path deep
        int foldersCount = 0;
        for (int i = 0; i < baseFileFolder.length(); ++i) {
            if (baseFileFolder.charAt(i) == '/')
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
        return SpecificationLocatorService.getService().buildUniqueSpecificationHREF(parentSpecification, specification.getLocation());
    }

    /*

    private org.concordion.api.Element createLinkBySpecification(TestContext.SpecificationContext specContext) {

        String linkName = buildSpecLinkName(specContext);

        nu.xom.Element hrefElement = new nu.xom.Element("a");
        hrefElement.appendChild(new nu.xom.Text(linkName));


        String path = buildRelativePathToSpec(specContext);

        hrefElement.addAttribute(new nu.xom.Attribute("href", path));

        return new org.concordion.api.Element(hrefElement);
    }

    private String buildSpecLinkName(TestContext.SpecificationContext specContext) {
        Specification specification = specContext.getSpecification();

        return specification.getLocation().
                replaceAll(htmlSuffixReplacer.pattern(), "").
                replaceAll(pathReplacer.pattern(), "").
                replaceAll(firstSlashReplacer.pattern(), "");
    }

    private String buildRelativePathToSpec(TestContext.SpecificationContext targetSpecContext) {
        String targetLocation = targetSpecContext.getSpecification().getLocation();
        String currentLocation =
                getTestContextService().getCurrentTestContext().getCurrentSpecificationContext().
                        getSpecification().getLocation();

        return new File(currentLocation).toURI().relativize(new File(targetLocation).toURI()).getPath();

    }

*/
    protected TestContextService getTestContextService() {
        return new TestContextService();
    }

}
