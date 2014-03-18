package com.scej.core.concordion.extension.documentparsing;

import nu.xom.Document;
import org.concordion.api.listener.DocumentParsingListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Fedorovaleks
 * Date: 3/18/14
 */
public class DocumentParsingListenerFacade implements DocumentParsingListener {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentParsingListenerFacade.class);


    private final DocumentParsingListener childLinkUpdater;
    private final DocumentParsingListener substituteListener;

    public DocumentParsingListenerFacade() {

        childLinkUpdater = new ChildSpecificationLinkUpdater();
        substituteListener = new DictionarySubstitutionListener();

        LOG.info("instance created");

    }

    @Override
    public void beforeParsing(Document document) {

        LOG.debug("method invoked");

        substituteListener.beforeParsing(document);

        LOG.info("substitution listener finished");

        childLinkUpdater.beforeParsing(document);

        LOG.info("chile link updater listener finished");

        LOG.debug("method finished");
    }
}
