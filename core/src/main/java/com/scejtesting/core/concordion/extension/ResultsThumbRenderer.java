package com.scejtesting.core.concordion.extension;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.Element;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.api.listener.SpecificationProcessingListener;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by aleks on 5/12/14.
 */
public class ResultsThumbRenderer implements SpecificationProcessingListener {

    public static final Pattern htmlSuffixReplacer = Pattern.compile("\\.[H|h][T|t][M|m][L|l]{0,1}");
    public static final Pattern pathReplacer = Pattern.compile(".*/");
    public static final Pattern firstSlashReplacer = Pattern.compile("^/");

    @Override
    public void beforeProcessingSpecification(SpecificationProcessingEvent event) {

    }

    @Override
    public void afterProcessingSpecification(SpecificationProcessingEvent event) {

        Element documentRootElement = event.getRootElement().getRootElement();

        List<TestContext.SpecificationContext> currentSpecificationFullStack =
                getTestContextService().getCurrentTestContext().getSpecificationStack();

        Element thumbHolderElement = new Element("div");
        documentRootElement.prependChild(thumbHolderElement);

        for (TestContext.SpecificationContext specificationContext : currentSpecificationFullStack) {
            thumbHolderElement.appendChild(createLinkBySpecification(specificationContext));

            nu.xom.Element element = new nu.xom.Element("span");
            element.appendChild(new nu.xom.Text(" > "));
            thumbHolderElement.appendChild(new Element(element));

        }

        thumbHolderElement.appendChild(new Element("br"));

    }

    private Element createLinkBySpecification(TestContext.SpecificationContext specContext) {

        Specification specification = specContext.getSpecification();

        String name = specification.getLocation().
                replaceAll(htmlSuffixReplacer.pattern(), "").
                replaceAll(pathReplacer.pattern(), "").
                replaceAll(firstSlashReplacer.pattern(), "");

        nu.xom.Element hrefElement = new nu.xom.Element("a");
        hrefElement.appendChild(new nu.xom.Text(name));


        String path = getAbsolutePathBySpecification(specification);

        hrefElement.addAttribute(new nu.xom.Attribute("href", path));

        return new Element(hrefElement);
    }

    private String getAbsolutePathBySpecification(Specification specification) {

        String absolutePath = null;

        if (specification.isTopLevelSpecification()) {
            absolutePath = new File(FileTargetWithCustomPrefix.resultBaseDir,
                    specification.getRealPath()).getAbsolutePath();
        } else {
            absolutePath = specification.getRealPath();
        }

        return absolutePath;
    }

    protected TestContextService getTestContextService() {
        return new TestContextService();
    }
}
