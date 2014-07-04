package com.scejtesting.core.concordion.extension.specificationprocessing;

import org.concordion.api.Element;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.api.listener.SpecificationProcessingListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by aleks on 5/12/14.
 */
public class ResultsThumbRendererProcessingListener implements SpecificationProcessingListener {

    protected static final Logger LOG = LoggerFactory.getLogger(ResultsThumbRendererProcessingListener.class);

    @Override
    public void beforeProcessingSpecification(SpecificationProcessingEvent event) {

    }

    @Override
    public void afterProcessingSpecification(SpecificationProcessingEvent event) {

        LOG.debug("method invoked");

        Element documentRootElement = event.getRootElement().getRootElement().getChildElements("body")[0];

        Element thumbHolderElement = new Element("div");
        documentRootElement.prependChild(thumbHolderElement);

        try {
            for (Element link : new ResultsThumbBuilder().buildResultThumbs()) {
                thumbHolderElement.appendChild(link);

                nu.xom.Element element = new nu.xom.Element("span");
                element.appendChild(new nu.xom.Text(" > "));
                thumbHolderElement.appendChild(new Element(element));

            }

            thumbHolderElement.appendChild(new Element("br"));
            LOG.info("Thumb links generaged");
        } catch (IOException e) {
            LOG.error("Thumb generation Exception ", e);
        } finally {
            LOG.debug("method finished");
        }

    }

}
