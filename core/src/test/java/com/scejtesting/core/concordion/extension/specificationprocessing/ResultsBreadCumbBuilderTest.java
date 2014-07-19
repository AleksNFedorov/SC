package com.scejtesting.core.concordion.extension.specificationprocessing;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.SpecificationLocatorService;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import nu.xom.Attribute;
import org.concordion.api.Element;
import org.concordion.api.Resource;
import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by aleks on 6/28/14.
 */
public class ResultsBreadCumbBuilderTest {

    private Test mockTest;
    private TestContext currentTestContext;

    @Before
    public void initTest() {
        Specification root = spy(new Specification("/com/scej/Head.html"));

        Test test = mock(Test.class);
        when(test.getSpecification()).thenReturn(root);
        when(test.getName()).thenReturn("testName");

        currentTestContext = new TestContextService().createNewTestContext(test);

        mockTest = test;
    }

    @org.junit.Test
    public void linkGeneration_sameLevelRootPackage() throws IOException {

        buildProcessedSpecification(mockTest.getSpecification());
        Specification ch1 = buildProcessedSpecification("Ch1.html");
        Specification ch21 = buildProcessedSpecification("Ch21.html");

        currentTestContext.createNewSpecificationContext(new Resource("/com/scej/Head.html"), ch1);
        currentTestContext.createNewSpecificationContext(new Resource("/com/scej/Ch1.html"), ch21);

        List<Element> thumbLinks = new ResultsBreadcumbBuilder().buildResultThumbs();

        Assert.assertEquals(2, thumbLinks.size());
        Assert.assertEquals("Head.html", extractHrefFromElement(thumbLinks.get(0)));

        String ch1Link = extractHrefFromElement(thumbLinks.get(1));
        Assert.assertEquals("Ch1.html", SpecificationLocatorService.cleanSuffix(ch1Link));

    }

    @org.junit.Test
    public void linkGeneration_deepHierarchy() throws IOException {

        buildProcessedSpecification(mockTest.getSpecification());
        Specification ch1 = buildProcessedSpecification("ch1/Ch1.html");
        Specification ch21 = buildProcessedSpecification("what/the/fuck/Ch21.html");

        currentTestContext.createNewSpecificationContext(new Resource("/com/scej/Head.html"), ch1);
        currentTestContext.createNewSpecificationContext(new Resource("/com/scej/ch1/Ch1.html"), ch21);

        List<Element> thumbLinks = new ResultsBreadcumbBuilder().buildResultThumbs();

        Assert.assertEquals(2, thumbLinks.size());
        Assert.assertEquals("../../../../Head.html", extractHrefFromElement(thumbLinks.get(0)));

        String ch1Link = extractHrefFromElement(thumbLinks.get(1));
        Assert.assertEquals("../../../Ch1.html", SpecificationLocatorService.cleanSuffix(ch1Link));
    }

    private String extractHrefFromElement(Element linkElement) {
        return linkElement.getAttributeValue("href");
    }

    private Specification buildProcessedSpecification(String newSpecification) {
        return buildProcessedSpecification(spy(new Specification(newSpecification)));
    }

    private Specification buildProcessedSpecification(Specification newSpecification) {
        String realPath = new SpecificationLocatorService().
                buildUniqueSpecificationHREF(newSpecification, newSpecification.getLocation());
        doReturn(realPath).when(newSpecification).getRealPath();
        nu.xom.Element newLink = new nu.xom.Element("a");
        newLink.addAttribute(new Attribute("href", realPath));
        new TestContextService().getCurrentTestContext().saveChildSpecificationElement(new Element(newLink));
        return newSpecification;
    }

}
