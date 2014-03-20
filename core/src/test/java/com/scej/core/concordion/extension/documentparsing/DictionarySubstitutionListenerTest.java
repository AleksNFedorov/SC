package com.scej.core.concordion.extension.documentparsing;

import com.scej.core.config.Specification;
import com.scej.core.config.Test;
import com.scej.core.context.TestContextService;
import nu.xom.Document;
import org.concordion.internal.XMLParser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.mockito.Mockito.*;

/**
 * User: Fedorovaleks
 * Date: 3/18/14
 */
public class DictionarySubstitutionListenerTest extends TestContextService {


    @org.junit.Test
    public void specificationFileUpdateTest() throws IOException {

        String specificationLocation = "com/scej/core/concordion/extension/documentparsing/SubstitutionTest.html";

        Specification specification = new Specification(specificationLocation);

        Test test = mock(Test.class);

        when(test.getSpecification()).thenReturn(specification);

        DictionaryLoaderService dictionaryLoaderService = mock(DictionaryLoaderService.class);

        Properties dictionary = new Properties();
        dictionary.put("parameter1", "parameter1Value");
        dictionary.put("parameter2", "parameter2Value");

        when(dictionaryLoaderService.buildSubstitutionDictionary()).thenReturn(dictionary);

        createNewTestContext(test);


        String pathToSpecificationFile = getClass().getClassLoader().
                getResource("com/scej/core/concordion/extension/documentparsing/SubstitutionTest.html").getFile();
        Document parsedDocument = new XMLParser().parse(new FileInputStream(pathToSpecificationFile));


        DictionarySubstitutionListener dictionarySubstitutionListener = spy(new DictionarySubstitutionListener());

        when(dictionarySubstitutionListener.getDictionaryLoaderService()).thenReturn(dictionaryLoaderService);

        dictionarySubstitutionListener.beforeParsing(parsedDocument);

/*
        Nodes allHrefNodes = parsedDocument.query("//a[@href]");

        for (int i = 0; i < allHrefNodes.size(); ++i) {
            Element currentNode = (Element) allHrefNodes.get(i);
            Attribute hrefAttribute = currentNode.getAttribute("href");
            Assert.assertTrue(SpecificationLocatorService.containsGeneratedSuffix(hrefAttribute.getValue()));
        }
*/

        getCurrentTestContext().destroyCurrentSpecificationContext();

    }
}
