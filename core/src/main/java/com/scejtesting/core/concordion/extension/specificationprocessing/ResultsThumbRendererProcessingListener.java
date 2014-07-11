package com.scejtesting.core.concordion.extension.specificationprocessing;

import org.concordion.api.Element;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.api.listener.SpecificationProcessingListener;
import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

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

        Check.notNull(event, "Specification processing event can't be null");

        Element thumbHolderElement = buildThumbContainer(event);

        try {
            for (Element link : buildThumbLinks()) {
                appendLinkToThumbContainer(thumbHolderElement, link);
            }

            thumbHolderElement.appendChild(new Element("br"));
            LOG.info("Thumb links generaged");
        } catch (IOException e) {
            LOG.error("Thumb generation Exception ", e);
        } finally {
            LOG.debug("method finished");
        }
    }

    protected Element buildThumbContainer(SpecificationProcessingEvent event) {
        Element documentRootElement = getSpecificationRootElement(event);
        Check.notNull(documentRootElement, "Specification must have body element");
        Element thumbHolderElement = new Element("div");
        documentRootElement.prependChild(thumbHolderElement);

        return thumbHolderElement;
    }

    protected Element getSpecificationRootElement(SpecificationProcessingEvent event) {
        return event.getRootElement().
                getRootElement().getChildElements("body")[0];
    }

    protected void appendLinkToThumbContainer(Element thumbHolderElement, Element link) {
        thumbHolderElement.appendChild(link);
        nu.xom.Element element = new nu.xom.Element("span");
        element.appendChild(new nu.xom.Text(" > "));
        thumbHolderElement.appendChild(new Element(element));
    }

    protected List<Element> buildThumbLinks() throws IOException {
        return new ResultsThumbBuilder().buildResultThumbs();
    }

}
