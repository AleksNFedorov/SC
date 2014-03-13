package com.scej.core;

import com.scej.core.concordion.TestContext;
import com.scej.core.concordion.extension.*;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.api.extension.Extension;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.After;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */

@RunWith(ConcordionRunner.class)
//@FailFast(onExceptionType = {UnreachableBrowserException.class, NoSuchWindowException.class})
public class CoreTest {

    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Extension
    public final ConcordionExtension genericExtension = new ConcordionExtension() {
        @Override
        public void addTo(ConcordionExtender concordionExtender) {
            concordionExtender.withSpecificationLocator(new TestContextSpecificationLocator());
            concordionExtender.withTarget(new FileTargetWithDateTimePrefix());
            concordionExtender.withDocumentParsingListener(new IncludeSpecificationDocumentUpdater());
            concordionExtender.withSource(new ClassPathSpecificationSource());
        }
    };


    @After
    public void onTestEnd() {
        LOG.debug("method invoked");
        TestContext.getInstance().destroyCurrentSpecificationContext();
        LOG.debug("method finished");
    }

}
