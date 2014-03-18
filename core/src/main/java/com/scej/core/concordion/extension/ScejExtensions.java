package com.scej.core.concordion.extension;

import com.scej.core.concordion.extension.documentparsing.DocumentParsingListenerFacade;
import com.scej.core.concordion.extension.exception.SuiteFailFastExceptionListener;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;

/**
 * User: Fedorovaleks
 * Date: 13.03.14
 */
public class ScejExtensions implements ConcordionExtension {
    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withSpecificationLocator(new HierarchySpecificationLocator());
        concordionExtender.withTarget(new FileTargetWithCustomPrefix());
        concordionExtender.withDocumentParsingListener(new DocumentParsingListenerFacade());
        concordionExtender.withSource(new ClassPathSpecificationSource());
        concordionExtender.withThrowableListener(new SuiteFailFastExceptionListener());
    }
}
