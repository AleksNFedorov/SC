package com.areon.autotests.core.integration.extension;

import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.api.listener.ThrowableCaughtEvent;
import org.concordion.api.listener.ThrowableCaughtListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 * Date: 1/23/14
 * Time: 6:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExceptionLogger implements ThrowableCaughtListener, ConcordionExtension {

    private static Logger LOG = LoggerFactory.getLogger(ExceptionLogger.class);

    {
        LOG.info("Initialized");
    }


    @Override
    public void throwableCaught(ThrowableCaughtEvent event) {
        LOG.debug("method invoked");
        LOG.error("Execution exception", event.getThrowable());
        LOG.debug("method finished");
    }

    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withThrowableListener(this);
    }
}
