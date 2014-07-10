package com.scejtesting.core.concordion.extension;

import com.scejtesting.core.context.TestContextService;

/**
 * User: Fedorovaleks
 * Date: 15.03.14
 */
public class ChildSpecificationLinkUpdaterTest extends TestContextService {

/*
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

        destroyTestContextService();

    }
*/
}
