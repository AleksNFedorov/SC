package com.scejtesting.core.concordion.extension.documentparsing;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContextService;
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
                getResource("com/scejtesting/core/concordion/extension/documentparsing/SubstitutionTest.html").getFile();
        Document parsedDocument = new XMLParser().parse(new FileInputStream(pathToSpecificationFile));


        DictionarySubstitutionListener dictionarySubstitutionListener = spy(new DictionarySubstitutionListener());

        when(dictionarySubstitutionListener.getDictionaryLoaderService()).thenReturn(dictionaryLoaderService);

        dictionarySubstitutionListener.beforeParsing(parsedDocument);

        destroyTestContext();

    }


}
