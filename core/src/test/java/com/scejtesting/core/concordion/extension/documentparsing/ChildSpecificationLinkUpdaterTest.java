package com.scejtesting.core.concordion.extension.documentparsing;


import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.SpecificationLocatorService;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContextService;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import org.junit.Assert;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ofedorov on 7/10/14.
 */
public class ChildSpecificationLinkUpdaterTest {

    @org.junit.Test
    public void positiveFlowTest() {

        Specification rootSpec = new Specification("/rootSpec.html");

        Test mockedTest = mock(Test.class);
        when(mockedTest.getSpecification()).thenReturn(rootSpec);

        new TestContextService().createNewTestContext(mockedTest);

        Element concordionRunElement = new Element("a");
        Attribute runAttribute = new Attribute("concordion:run", "http://www.concordion.org/2007/concordion", "");
        Attribute href = new Attribute("href", "", "someSpecification.html");
        concordionRunElement.addAttribute(href);
        concordionRunElement.addAttribute(runAttribute);

        Element nonRunElement = new Element("a");
        Attribute hrefNonRun = new Attribute("href", "", "scejtesting.com.html");
        nonRunElement.addAttribute(hrefNonRun);

        Element root = new Element("html");
        root.appendChild(concordionRunElement);
        root.appendChild(nonRunElement);

        Document specificationDocument = new Document(root);

        ChildSpecificationLinkUpdater linkUpdater = new ChildSpecificationLinkUpdater();

        linkUpdater.beforeParsing(specificationDocument);

        Assert.assertSame("scejtesting.com.html", hrefNonRun.getValue());
        Assert.assertTrue(SpecificationLocatorService.containsGeneratedSuffix(href.getValue()));
        Assert.assertEquals("Child specification linker", linkUpdater.getParserName());

    }
}
