package com.scej.core.concordion.extension.documentparsing;

import com.scej.core.config.Test;
import com.scej.core.context.TestContext;
import com.scej.core.context.TestContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DictionaryLoaderService {

    private static final Logger LOG = LoggerFactory.getLogger(DictionaryLoaderService.class);

    public static final String GLOBAL_DICTIONARY = "scejGlobalDictionary.properties";


    public Properties buildSubstitutionDictionary() {
        LOG.debug("method invoked");


        Properties gloalDictionary = loadGlobalDictionary();
        Properties testDictionary = loadTestDictionary();

        testDictionary.putAll(gloalDictionary);

        gloalDictionary = testDictionary;

        return gloalDictionary;
    }

    private Properties loadGlobalDictionary() {
        LOG.debug("method invoked");


        Properties globalDictionaryProperties = getCurrentTestContext().getAttribute(GLOBAL_DICTIONARY);
        if (globalDictionaryProperties != null) {
            LOG.info("global properties restored from context");
            return globalDictionaryProperties;
        }

        LOG.info("Loading global properties");

        try {
            String globalDictionaryFile = getClass().getClassLoader().getResource(GLOBAL_DICTIONARY).getFile();
            globalDictionaryProperties = loadDictionaryFromFile(globalDictionaryFile);
            LOG.info("global dictionary successfully loaded");
        } catch (Exception ex) {
            LOG.error("Exception when loading global properties ", ex);
            globalDictionaryProperties = new Properties();
        }

        getCurrentTestContext().addAttribute(GLOBAL_DICTIONARY, globalDictionaryProperties);
        LOG.debug("method finished [{}]", globalDictionaryProperties);
        return globalDictionaryProperties;
    }

    private Properties loadTestDictionary() {
        LOG.debug("method invoked");

        Test currentTest = getCurrentTestContext().getTest();


        if (currentTest.getSubstitutionDictionary() == null) {
            LOG.info("No test dictionary");
            return new Properties();
        }

        Properties testDictionaryProperties = getCurrentTestContext().getAttribute(currentTest);


        if (testDictionaryProperties != null) {
            LOG.info("test properties restored from context");
            return testDictionaryProperties;
        }

        try {
            String pathToTestDictionaryFile = currentTest.getSubstitutionDictionary();
            testDictionaryProperties = loadDictionaryFromFile(pathToTestDictionaryFile);
            LOG.info("test dictionary [{}] successfully loaded", pathToTestDictionaryFile);
        } catch (Exception ex) {
            LOG.error("Exception when loading global properties ", ex);
            testDictionaryProperties = new Properties();
        }

        getCurrentTestContext().addAttribute(currentTest, testDictionaryProperties);
        LOG.debug("method finished [{}]", testDictionaryProperties);
        return testDictionaryProperties;
    }

    protected TestContext getCurrentTestContext() {
        return new TestContextService().getCurrentTestContext();
    }

    private Properties loadDictionaryFromFile(String globalDictionary) throws IOException {
        LOG.debug("method invoked");
        Properties globalDictionaryProperties = new Properties();
        File globalDictionaryFile = new File(globalDictionary);
        if (!globalDictionaryFile.exists()) {
            LOG.info("No dictionary found [{}]", globalDictionary);
            return globalDictionaryProperties;
        }
        globalDictionaryProperties.load(new FileInputStream(globalDictionaryFile));

        LOG.debug("method finished [{}]", globalDictionaryProperties);
        return globalDictionaryProperties;
    }
}