package com.scejtesting.core.concordion.extension.documentparsing;

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
    private final DocumentParsingListener registerGlobalsEnricher;

    public DocumentParsingListenerFacade() {

        childLinkUpdater = new ChildSpecificationLinkUpdater();
        substituteListener = new DictionarySubstitutionListener();
        registerGlobalsEnricher = new RegisterGlobalsCommandDocumentEnricher();

        LOG.info("instance created");

    }

    @Override
    public void beforeParsing(Document document) {

        LOG.debug("method invoked");

        substituteListener.beforeParsing(document);

        LOG.info("substitution listener finished");

        childLinkUpdater.beforeParsing(document);

        LOG.info("chile link updater listener finished");

        registerGlobalsEnricher.beforeParsing(document);

        LOG.info("register globals enricher listener finished");

        LOG.debug("method finished");
    }
}
