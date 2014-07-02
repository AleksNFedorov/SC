package com.scejtesting.core.concordion.extension.specificationprocessing;

import org.concordion.api.listener.SpecificationProcessingEvent;
import org.concordion.api.listener.SpecificationProcessingListener;

/**
 * Created by aleks on 5/12/14.
 */
public class ResultsThumbRendererProcessingListener implements SpecificationProcessingListener {

    @Override
    public void beforeProcessingSpecification(SpecificationProcessingEvent event) {

    }

    @Override
    public void afterProcessingSpecification(SpecificationProcessingEvent event) {

//        Element documentRootElement = event.getRootElement().getRootElement();
//
//        List<TestContext.SpecificationContext> currentSpecificationFullStack =
//                getTestContextService().getCurrentTestContext().getSpecificationStack();
//
//        Element thumbHolderElement = new Element("div");
//        documentRootElement.prependChild(thumbHolderElement);
//
//        for (TestContext.SpecificationContext specificationContext : currentSpecificationFullStack) {
//            thumbHolderElement.appendChild(createLinkBySpecification(specificationContext));
//
//            nu.xom.Element element = new nu.xom.Element("span");
//            element.appendChild(new nu.xom.Text(" > "));
//            thumbHolderElement.appendChild(new Element(element));
//
//        }
//
//        thumbHolderElement.appendChild(new Element("br"));

    }

}
