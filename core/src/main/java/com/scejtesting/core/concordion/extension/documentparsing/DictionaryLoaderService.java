package com.scejtesting.core.concordion.extension.documentparsing;

import com.scejtesting.core.Constants;
import com.scejtesting.core.config.Test;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

public class DictionaryLoaderService {

    private static final Logger LOG = LoggerFactory.getLogger(DictionaryLoaderService.class);

    private final TestContext currentTestContext = new TestContextService().getCurrentTestContext();

    public Properties buildSubstitutionDictionary() {
        LOG.debug("method invoked");

        Properties gloalDictionary = loadGlobalDictionary();
        Properties testDictionary = loadTestDictionary();

        Properties allProperties = new Properties();
        allProperties.putAll(gloalDictionary);
        allProperties.putAll(testDictionary);
        return allProperties;
    }

    private Properties loadGlobalDictionary() {
        LOG.debug("method invoked");

        Properties globalDictionaryProperties = getCurrentTestContext().getAttribute(Constants.GLOBAL_DICTIONARY);
        if (globalDictionaryProperties != null) {
            LOG.info("global properties restored from context");
            return globalDictionaryProperties;
        }

        LOG.info("Loading global properties");

        globalDictionaryProperties = new Properties();

        try {
            URL resourceURL = getClass().getClassLoader().getResource(Constants.GLOBAL_DICTIONARY);
            if (resourceURL == null) {
                LOG.info("No global dictionary found [{}]", Constants.GLOBAL_DICTIONARY);
            } else {
                globalDictionaryProperties = loadDictionaryFromFile(resourceURL.getFile());
                LOG.info("global dictionary successfully loaded");
            }
        } catch (Exception ex) {
            LOG.error("Exception when loading global properties ", ex);
        }

        getCurrentTestContext().addAttribute(Constants.GLOBAL_DICTIONARY, globalDictionaryProperties);
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

    private Properties loadDictionaryFromFile(String globalDictionary) throws IOException {
        LOG.debug("method invoked");
        Properties globalDictionaryProperties = new Properties();
        File globalDictionaryFile = new File(globalDictionary);

        if (!globalDictionaryFile.isAbsolute()) {
            URL dictionaryURL = getClass().getClassLoader().getResource(globalDictionary);
            if (dictionaryURL == null)
                throw new IllegalArgumentException("Cant find dictionary file [" + globalDictionary + "]");
            globalDictionaryFile = new File(dictionaryURL.getFile());
        }

        if (!globalDictionaryFile.exists()) {
            LOG.info("No dictionary found [{}]", globalDictionary);
            return globalDictionaryProperties;
        }
        globalDictionaryProperties.load(new InputStreamReader(new FileInputStream(globalDictionaryFile), "UTF-8"));

        LOG.debug("method finished [{}]", globalDictionaryProperties);
        return globalDictionaryProperties;
    }

    protected TestContext getCurrentTestContext() {
        return currentTestContext;
    }

}