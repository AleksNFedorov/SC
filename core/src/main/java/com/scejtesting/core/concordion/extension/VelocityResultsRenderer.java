package com.scejtesting.core.concordion.extension;

import com.scejtesting.core.context.SpecificationResultRegistry;
import com.scejtesting.core.context.TestContextService;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.concordion.api.Element;
import org.concordion.api.Result;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.api.listener.SpecificationProcessingListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by aleks on 4/26/14.
 */
public class VelocityResultsRenderer implements SpecificationProcessingListener {


    public static final String DEFAULT_RESULTS_TEMPLATE = "results.vm";
    public static final String CUSTOM_RESULTS_HOST_TAG = "scejresults";
    protected static final Logger LOG = LoggerFactory.getLogger(VelocityResultsRenderer.class);
    private final VelocityEngine templateRenderEngine;
    private Template resultsTemplate;


    public VelocityResultsRenderer() {

        templateRenderEngine = new VelocityEngine();
        init();

    }

    private void init() {

        try {

            java.util.Properties p = new java.util.Properties();
            p.setProperty("resource.loader", "class");
            p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

            templateRenderEngine.init(p);
            resultsTemplate = templateRenderEngine.getTemplate(DEFAULT_RESULTS_TEMPLATE);
        } catch (Exception ex) {
            LOG.error("Velocity initialization exception", ex);
            throw new RuntimeException(ex);
        }


        LOG.info("Results renderer initialized successfully");

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

        resultHostElement.prependText(resultsContent);

        LOG.info("Results inserted info reporting page");


    }

    private Element getResultsHostElement(Element eventElement) {

        Element rootDocumentElement = eventElement.getRootElement();

        Element resultHostElement = rootDocumentElement.getElementById(CUSTOM_RESULTS_HOST_TAG);

        if (resultHostElement != null) {
            LOG.info("Result host element found");
            return resultHostElement;
        }

        resultHostElement = rootDocumentElement.getFirstChildElement("body");

        LOG.info("Using default root host element [body]");

        return resultHostElement;

    }

    private VelocityContext buildFromCurrentSpecificationContext() {
        SpecificationResultRegistry resultsRegistry = getTestContextService().getCurrentTestContext().getCurrentSpecificationContext().getResultRegistry();

        VelocityContext velocityResultsContext = new VelocityContext();

        for (Result result : Result.values()) {
            int resultsAmount = resultsRegistry.getResultsAmount(result);
            velocityResultsContext.put(result.name(), resultsAmount);
            LOG.debug("Result [{}] added to context, amount [{}]", result, resultsAmount);
        }

        LOG.info("Velocity context has been built");

        return velocityResultsContext;

    }

    protected TestContextService getTestContextService() {
        return new TestContextService();
    }
}
