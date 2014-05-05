package com.scejtesting.core.concordion.extension.documentparsing;

import nu.xom.Document;
import org.concordion.api.listener.DocumentParsingListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * User: Fedorovaleks
 * Date: 3/18/14
 */
public class DocumentParsingListenerFacade implements DocumentParsingListener {

    protected static final Logger LOG = LoggerFactory.getLogger(DocumentParsingListenerFacade.class);

    private final List<NamedDocumentParsingListener> parsingListeners = new LinkedList<NamedDocumentParsingListener>() {
        {
            add(new ChildSpecificationLinkUpdater());
            add(new DictionarySubstitutionListener());
            add(new RegisterGlobalsCommandDocumentEnricher());
            add(new ScejCommandArgumentsTransformer());
        }
    };


    @Override
    public void beforeParsing(Document document) {

        LOG.debug("method invoked");

        for (NamedDocumentParsingListener parsingListener : parsingListeners) {
            parsingListener.beforeParsing(document);
            LOG.info("Listener [{}] successfully processed document ", parsingListener.getParserName());
        }

        LOG.info("All listeners finished");

        LOG.debug("method finished");
    }


}
