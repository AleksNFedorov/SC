package com.scejtesting.core.concordion.extension.specificationprocessing;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.SpecificationLocatorService;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.concordion.api.Element;
import org.junit.Assert;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by aleks on 6/28/14.
 */
public class ResultsThumbBuilderTest {


    @org.junit.Test
    public void linkGeneration_sameLevelRootPackage() throws IOException {
        Specification root = new Specification("/com/scej/Head.html");
        Specification ch1 = new Specification("Ch1.html");
        Specification ch21 = new Specification("Ch21.html");

        Test test = mock(Test.class);

        when(test.getSpecification()).thenReturn(root);

        TestContext context = new TestContextService().createNewTestContext(test);

        context.createNewSpecificationContext(null, ch1);
        context.createNewSpecificationContext(null, ch21);

        List<Element> thumbLinks = new ResultsThumbBuilder().buildResultThumbs();

        Assert.assertEquals(2, thumbLinks.size());
        Assert.assertEquals("Head.html", extractHrefFromElement(thumbLinks.get(0)));

        String ch1Link = extractHrefFromElement(thumbLinks.get(1));
        Assert.assertTrue(SpecificationLocatorService.containsGeneratedSuffix(ch1Link));
        Assert.assertEquals("Ch1.html", SpecificationLocatorService.cleanSuffix(ch1Link));

    }

    @org.junit.Test
    public void linkGeneration_sameLevel() throws IOException {
        Specification root = new Specification("/com/scej/Head.html");
        Specification ch1 = new Specification("Ch1.html");
        Specification ch21 = new Specification("Ch21.html");

        Test test = mock(Test.class);

        when(test.getSpecification()).thenReturn(root);

        TestContext context = new TestContextService().createNewTestContext(test);

        context.createNewSpecificationContext(null, ch1);
        context.createNewSpecificationContext(null, ch21);

        List<Element> thumbLinks = new ResultsThumbBuilder().buildResultThumbs();

        Assert.assertEquals(2, thumbLinks.size());
        Assert.assertEquals("Head.html", extractHrefFromElement(thumbLinks.get(0)));

        String ch1Link = extractHrefFromElement(thumbLinks.get(1));
        Assert.assertTrue(SpecificationLocatorService.containsGeneratedSuffix(ch1Link));
        Assert.assertEquals("Ch1.html", SpecificationLocatorService.cleanSuffix(ch1Link));
    }

    @org.junit.Test
    public void linkGeneration_deepHierarchy() throws IOException {

        Specification root = new Specification("/com/scej/Head.html");
        Specification ch1 = new Specification("ch1/Ch1.html");
        Specification ch21 = new Specification("what/the/fuck/Ch21.html");

        Test test = mock(Test.class);

        when(test.getSpecification()).thenReturn(root);

        TestContext context = new TestContextService().createNewTestContext(test);

        context.createNewSpecificationContext(null, ch1);
        context.createNewSpecificationContext(null, ch21);

        List<Element> thumbLinks = new ResultsThumbBuilder().buildResultThumbs();

        Assert.assertEquals(2, thumbLinks.size());
        Assert.assertEquals("../../../../Head.html", extractHrefFromElement(thumbLinks.get(0)));

        String ch1Link = extractHrefFromElement(thumbLinks.get(1));
        Assert.assertTrue(SpecificationLocatorService.containsGeneratedSuffix(ch1Link));
        Assert.assertEquals("../../../../ch1/Ch1.html", SpecificationLocatorService.cleanSuffix(ch1Link));
    }

    private String extractHrefFromElement(Element linkElement) {
        return linkElement.getAttributeValue("href");
    }

}
