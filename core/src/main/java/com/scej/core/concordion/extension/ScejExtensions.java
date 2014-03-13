package com.scej.core.concordion.extension;

import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;

/**
 * User: Fedorovaleks
 * Date: 13.03.14
 */
public class ScejExtensions implements ConcordionExtension {
    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withSpecificationLocator(new TestContextSpecificationLocator());
        concordionExtender.withTarget(new FileTargetWithDateTimePrefix());
        concordionExtender.withDocumentParsingListener(new IncludeSpecificationDocumentUpdater());
        concordionExtender.withSource(new ClassPathSpecificationSource());
    }
}
