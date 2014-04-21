package com.scejtesting.core.concordion.extension;

import com.scejtesting.core.concordion.extension.documentparsing.ChildSpecificationLinkUpdater;
import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.SpecificationLocatorService;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContextService;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import org.concordion.internal.XMLParser;
import org.junit.Assert;

import java.io.FileInputStream;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: Fedorovaleks
 * Date: 15.03.14
 */
public class ChildSpecificationLinkUpdaterTest extends TestContextService {

    @org.junit.Test
    public void specificationFileUpdateTest() throws IOException {

        String specificationLocation = "/HeadSpecification.html";

        Specification specification = new Specification(specificationLocation);

        Test test = mock(Test.class);

        when(test.getSpecification()).thenReturn(specification);

        createNewTestContext(test);


        String pathToSpecificationFile = getClass().getClassLoader().
                getResource("com/scejtesting/core/concordion/extension/HeadSpecification.html").getFile();
        Document parsedDocument = new XMLParser().parse(new FileInputStream(pathToSpecificationFile));

        new ChildSpecificationLinkUpdater().beforeParsing(parsedDocument);

        Nodes allHrefNodes = parsedDocument.query("//a[@href]");

        for (int i = 0; i < allHrefNodes.size(); ++i) {
            Element currentNode = (Element) allHrefNodes.get(i);
            Attribute hrefAttribute = currentNode.getAttribute("href");
            Assert.assertTrue(SpecificationLocatorService.containsGeneratedSuffix(hrefAttribute.getValue()));
        }

        getCurrentTestContext().destroyCurrentSpecificationContext();

    }
}
