package com.scej.core.config;

import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class SuiteConfiguration {

    private static Logger LOG = LoggerFactory.getLogger(SuiteConfiguration.class);

    private Suite suite;

    private static SuiteConfiguration configuration;

    private SuiteConfiguration(String pathToConfiguration) {
        LOG.debug("method invoked [{}]", pathToConfiguration);
        try {
            suite = loadSuiteConfiguration(pathToConfiguration);
            LOG.info("Suite configuration has been loaded [{}]", suite);
            initTests();
        } catch (Exception ex) {
            LOG.error("Exception during configuration creation [{}]", ex.getMessage(), ex);
            throw new RuntimeException(ex);
        } finally {
            LOG.debug("method finished");
        }
    }

    public static void initConfiguration(String pathToConfiguration) {
        LOG.debug("Method invoked [{}]", pathToConfiguration);
        Check.notNull(pathToConfiguration, "Path to configuration is mandatory");
        configuration = new SuiteConfiguration(pathToConfiguration);
        LOG.info("Configuration created");
        LOG.debug("method finished");
    }

    public static SuiteConfiguration getInstance() {
        return configuration;
    }

    private Suite loadSuiteConfiguration(String pathToConfigurationFile) throws JAXBException, SAXException {

        LOG.debug("method invoked [{}]", pathToConfigurationFile);

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(this.getClass().getClassLoader().getResource("scejsuite.xsd"));

        LOG.info("validation schema loaded [{}]", schema);

        JAXBContext suiteContext = JAXBContext.newInstance(Suite.class);

        Unmarshaller unmarshaller = suiteContext.createUnmarshaller();
        unmarshaller.setSchema(schema);
        Suite restoredTestSuite = (Suite) unmarshaller.unmarshal(new File(pathToConfigurationFile));

        LOG.info("Test suite created [{}]", restoredTestSuite);

        LOG.debug("method finished");

        return restoredTestSuite;
    }

    private void initTests() {
        LOG.debug("method invoked");
        for (Test test : suite.getTests()) {
            LOG.info("new mapping created [{}], [{}]", test.getDefaultTestClass(), test);
            test.init();
            initTestRootSpecification(test);

        }
        LOG.debug("method finished");
    }

    private void initTestRootSpecification(Test test) {
        LOG.debug("method invoked");
        Specification testRootSpecification = test.getSpecification();

        testRootSpecification.setTopLevelSpecification();

        String rootSpecificationLocation = testRootSpecification.getLocation();

        Check.isTrue(rootSpecificationLocation.startsWith("/"), "Root specification [" + rootSpecificationLocation + "] location must starts with '/'");

        testRootSpecification.setRealPath(rootSpecificationLocation);


        LOG.debug("method finished");
    }

    public Suite getSuite() {
        return suite;
    }
}
