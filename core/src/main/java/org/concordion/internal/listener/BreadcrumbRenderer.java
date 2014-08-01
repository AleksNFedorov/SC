package org.concordion.internal.listener;

import org.concordion.api.Source;
import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.api.listener.SpecificationProcessingListener;
import org.concordion.internal.XMLParser;

public class BreadcrumbRenderer implements SpecificationProcessingListener {

    public BreadcrumbRenderer(Source source, XMLParser xmlParser) {
    }

    public void beforeProcessingSpecification(SpecificationProcessingEvent event) {
        // No action needed beforehand
    }

    public void afterProcessingSpecification(SpecificationProcessingEvent event) {
    }

}