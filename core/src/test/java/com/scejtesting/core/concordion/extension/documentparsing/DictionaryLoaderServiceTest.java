package com.scejtesting.core.concordion.extension.documentparsing;

import com.scejtesting.core.Constants;
import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.util.Properties;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ofedorov on 7/10/14.
 */
public class DictionaryLoaderServiceTest {

    @Before
    public void init() {
        Test mockTest = mock(Test.class);
        Specification testSpec = new Specification("/root.html");
        when(mockTest.getSpecification()).thenReturn(testSpec);
        new TestContextService().createNewTestContext(mockTest);
    }

    @After
    public void finishTest() {
        System.clearProperty(Constants.GLOBAL_DICTIONARY);
    }

    @org.junit.Test
    public void defaultDictionaryTest() {
        Properties dictionary = new DictionaryLoaderService().buildSubstitutionDictionary();
        Assert.assertNotNull(dictionary);
        Assert.assertEquals(2, dictionary.size());
        Assert.assertEquals("default", dictionary.getProperty("commonValue"));
        Assert.assertEquals("default", dictionary.getProperty("testValue"));
    }

    @org.junit.Test
    public void twoDictionaryTest() {

    }

    @org.junit.Test
    public void globalDictionaryPropertyFromContext() {

        Properties props = new Properties();
        props.setProperty("commonValue", "global");
        props.setProperty("testValue", "global");

        new TestContextService().getCurrentTestContext().addAttribute(Constants.GLOBAL_DICTIONARY, props);

        Properties dictionary = new DictionaryLoaderService().buildSubstitutionDictionary();
        Assert.assertNotNull(dictionary);
        Assert.assertEquals(2, dictionary.size());
        Assert.assertEquals("global", dictionary.getProperty("commonValue"));
        Assert.assertEquals("global", dictionary.getProperty("testValue"));
    }

    @org.junit.Test
    public void testDictionaryFromFile() {
        TestContext currentTestContext = new TestContextService().getCurrentTestContext();
        when(currentTestContext.getTest().getSubstitutionDictionary()).thenReturn("test.dictionary.properties");

        Properties dictionary = new DictionaryLoaderService().buildSubstitutionDictionary();
        Assert.assertNotNull(dictionary);
        Assert.assertEquals(2, dictionary.size());
        Assert.assertEquals("test", dictionary.getProperty("commonValue"));
        Assert.assertEquals("test", dictionary.getProperty("testValue"));

    }

    @org.junit.Test
    public void testDictionaryFromContext() {
        Properties props = new Properties();
        props.setProperty("commonValue", "test");

        TestContext currentTestContext = new TestContextService().getCurrentTestContext();
        when(currentTestContext.getTest().getSubstitutionDictionary()).thenReturn("");

        currentTestContext.addAttribute(currentTestContext.getTest(), props);

        Properties dictionary = new DictionaryLoaderService().buildSubstitutionDictionary();
        Assert.assertNotNull(dictionary);
        Assert.assertEquals(2, dictionary.size());
        Assert.assertEquals("test", dictionary.getProperty("commonValue"));
        Assert.assertEquals("default", dictionary.getProperty("testValue"));

    }
}
