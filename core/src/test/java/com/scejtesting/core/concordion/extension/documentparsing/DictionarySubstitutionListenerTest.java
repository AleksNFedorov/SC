package com.scejtesting.core.concordion.extension.documentparsing;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContextService;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import org.concordion.internal.XMLParser;
import org.junit.After;
import org.junit.Assert;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.mockito.Mockito.*;

/**
 * User: Fedorovaleks
 * Date: 3/18/14
 */
public class DictionarySubstitutionListenerTest extends TestContextService {

    @After
    public void onFinish() {

        destroyTestContext();
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void unknownSubstitutionTest() throws IOException {

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
                getResource("com/scejtesting/core/concordion/extension/documentparsing/dictionary/UnknownSubstitutionTest.html").getFile();
        Document parsedDocument = new XMLParser().parse(new FileInputStream(pathToSpecificationFile));


        DictionarySubstitutionListener dictionarySubstitutionListener = spy(new DictionarySubstitutionListener());

        when(dictionarySubstitutionListener.getDictionaryLoaderService()).thenReturn(dictionaryLoaderService);

        dictionarySubstitutionListener.beforeParsing(parsedDocument);


    }


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
                getResource("com/scejtesting/core/concordion/extension/documentparsing/dictionary/SubstitutionTest.html").getFile();
        Document parsedDocument = new XMLParser().parse(new FileInputStream(pathToSpecificationFile));


        DictionarySubstitutionListener dictionarySubstitutionListener = spy(new DictionarySubstitutionListener());

        when(dictionarySubstitutionListener.getDictionaryLoaderService()).thenReturn(dictionaryLoaderService);

        dictionarySubstitutionListener.beforeParsing(parsedDocument);

        Nodes allChangesNodes = parsedDocument.query("//b");

        Element node1 = (Element) allChangesNodes.get(0);
        Element node2 = (Element) allChangesNodes.get(1);
        Element node3 = (Element) allChangesNodes.get(2);

        String node1ValueString = dictionary.getProperty("parameter1");
        String node2ValueString = dictionary.getProperty("parameter1") + "-"
                + dictionary.getProperty("parameter1") + "-"
                + dictionary.getProperty("parameter2");

        Assert.assertEquals(node1ValueString, node1.getValue());
        Assert.assertEquals(node2ValueString, node2.getValue());
        Assert.assertEquals(node2ValueString, node3.getValue());
        Assert.assertEquals(node1ValueString, node3.getAttribute(0).getValue());

    }


}
