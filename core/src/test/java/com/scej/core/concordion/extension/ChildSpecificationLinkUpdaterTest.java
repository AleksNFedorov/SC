package com.scej.core.concordion.extension;

import com.scej.core.TestContext;
import com.scej.core.config.Specification;
import com.scej.core.config.SpecificationLocatorService;
import com.scej.core.config.Test;
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
public class ChildSpecificationLinkUpdaterTest {

    @org.junit.Test
    public void specificationFileUpdateTest() throws IOException {

        String specificationLocation = "/HeadSpecification.html";

        Specification specification = new Specification(specificationLocation);

        Test test = mock(Test.class);

        when(test.getSpecification()).thenReturn(specification);

        TestContext.createTestContext(test);


        String pathToSpecificationFile = getClass().getClassLoader().
                getResource("com/scej/core/concordion/extension/HeadSpecification.html").getFile();
        Document parsedDocument = new XMLParser().parse(new FileInputStream(pathToSpecificationFile));

        new ChildSpecificationLinkUpdater().beforeParsing(parsedDocument);

        Nodes allHrefNodes = parsedDocument.query("//a[@href]");

        for (int i = 0; i < allHrefNodes.size(); ++i) {
            Element currentNode = (Element) allHrefNodes.get(i);
            Attribute hrefAttribute = currentNode.getAttribute("href");
            Assert.assertTrue(SpecificationLocatorService.containsGeneratedSuffix(hrefAttribute.getValue()));
        }

        TestContext.getInstance().destroyCurrentSpecificationContext();

    }
}