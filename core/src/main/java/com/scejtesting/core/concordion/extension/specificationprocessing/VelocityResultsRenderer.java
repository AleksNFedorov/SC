package com.scejtesting.core.concordion.extension.specificationprocessing;

import com.scejtesting.core.Constants;
import com.scejtesting.core.context.SpecificationResultRegistry;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import nu.xom.Document;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.concordion.api.Element;
import org.concordion.api.Result;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.api.listener.SpecificationProcessingListener;
import org.concordion.internal.XMLParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aleks on 4/26/14.
 */
public class VelocityResultsRenderer implements SpecificationProcessingListener {

    protected static final Logger LOG = LoggerFactory.getLogger(VelocityResultsRenderer.class);

    private final TestContext currentTestContext = new TestContextService().getCurrentTestContext();

    private String templateFileName;
    private Template resultsTemplate;

    public VelocityResultsRenderer() {
        init();
    }

    private void init() {

        try {
            templateFileName = resolveTemplateFile();

            VelocityEngine templateRenderEngine = new VelocityEngine();

            java.util.Properties p = new java.util.Properties();
            p.setProperty("resource.loader", "class");
            p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

            templateRenderEngine.init(p);
            resultsTemplate = templateRenderEngine.getTemplate(templateFileName);
        } catch (Exception ex) {
            LOG.error("Velocity initialization exception", ex);
            throw new RuntimeException(ex);
        }

        LOG.info("Results renderer initialized successfully");

    }

    private String resolveTemplateFile() {
        String velocityResultsFile = System.getProperty(Constants.VELOCITY_RESULTS_TEMPLATE_FILE_PROPERTY);
        if (velocityResultsFile != null) {
            LOG.info("Velocity results template file resolved as [" + velocityResultsFile + "]");
            return velocityResultsFile;
        } else {
            LOG.info("No custom results template specified");
            return Constants.VELOCITY_DEFAULT_TEMPLATE_FILE;
        }
    }

    @Override
    public void beforeProcessingSpecification(SpecificationProcessingEvent event) {
    }

    @Override
    public void afterProcessingSpecification(SpecificationProcessingEvent event) {

        LOG.debug("method invoked");

        VelocityContext resultsVelocityContext = buildFromCurrentSpecificationContext();

        String resultsContent = generateResultsContent(resultsVelocityContext);

        appendResultsContentToDocument(event.getRootElement().getRootElement(), resultsContent);

        LOG.debug("method finished");

    }

    private String generateResultsContent(VelocityContext resultsVelocityContext) {
        StringWriter resultsContent = new StringWriter(1000);

        try {
            resultsTemplate.merge(resultsVelocityContext, resultsContent);
        } catch (IOException e) {
            LOG.error("Velocity template merging error", e);
            throw new RuntimeException(e);
        }

        LOG.debug("Results content string generated");

        return resultsContent.toString();

    }

    private void appendResultsContentToDocument(Element eventElement, String resultsContent) {

        Element resultHostElement = getResultsHostElement(eventElement);

        try {
            Document resultsDocument = XMLParser.parse(resultsContent);
            nu.xom.Element parsedResultsRootElement = resultsDocument.getRootElement();
            resultsDocument.setRootElement(new nu.xom.Element("div"));
            parsedResultsRootElement.detach();

            Element resultElement = new Element(parsedResultsRootElement);
            resultHostElement.prependChild(resultElement);
        } catch (IOException e) {
            LOG.error("Result parse exception ", e);
            throw new RuntimeException(e);
        }

        LOG.info("Results inserted info reporting page");

    }

    private Element getResultsHostElement(Element eventElement) {

        Element rootDocumentElement = eventElement.getRootElement();

        Element resultHostElement = rootDocumentElement.getElementById(Constants.CUSTOM_RESULTS_HOST_TAG);

        if (resultHostElement != null) {
            LOG.info("Result host element found");
            return resultHostElement;
        }

        resultHostElement = rootDocumentElement.getFirstChildElement("body");

        LOG.info("Using default root host element [body]");

        return resultHostElement;

    }

    private VelocityContext buildFromCurrentSpecificationContext() {

        SpecificationResultRegistry resultsRegistry = getCurrentSpecificationResults();
        resultsRegistry.processResults();

        VelocityContext velocityResultsContext = new VelocityContext();
        Map<String, Long> resultMap = new HashMap<String, Long>();
        velocityResultsContext.put("results", resultMap);

        resultMap.put(Result.SUCCESS.name(), resultsRegistry.getSuccessCount());
        resultMap.put(Result.FAILURE.name(), resultsRegistry.getFailureCount());
        resultMap.put(Result.EXCEPTION.name(), resultsRegistry.getExceptionCount());
        resultMap.put(Result.IGNORED.name(), resultsRegistry.getIgnoredCount());

        LOG.info("Velocity context has been built");

        return velocityResultsContext;

    }

    public String getTemplateFileName() {
        return templateFileName;
    }

    protected SpecificationResultRegistry getCurrentSpecificationResults() {
        return currentTestContext.getCurrentSpecificationContext().getResultRegistry();

    }
}
