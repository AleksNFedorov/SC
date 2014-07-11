package com.scejtesting.core.concordion.extension.documentparsing;

import nu.xom.Document;
import org.concordion.api.listener.DocumentParsingListener;
import org.concordion.internal.util.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Fedorovaleks
 * Date: 3/18/14
 */
public class DocumentParsingListenerFacade implements DocumentParsingListener {

    protected static final Logger LOG = LoggerFactory.getLogger(DocumentParsingListenerFacade.class);

    private final List<NamedDocumentParsingListener> parsingListeners = Collections.unmodifiableList(new LinkedList<NamedDocumentParsingListener>() {
        {
            add(new ChildSpecificationLinkUpdater());
            add(new DictionarySubstitutionListener());
            add(new RegisterGlobalsCommandDocumentEnricher());
            add(new ScejCommandArgumentsTransformer());
        }
    });

    @Override
    public void beforeParsing(Document document) {

        LOG.debug("method invoked");

        Check.notNull(document, "Document must be specified");

        try {
            for (NamedDocumentParsingListener parsingListener : getParsingListeners()) {
                parsingListener.beforeParsing(document);
                LOG.info("Listener [{}] successfully processed document ", parsingListener.getParserName());
            }
        } catch (Throwable ex) {
            LOG.error("Exception during document processing", ex);
            throw new RuntimeException(ex);
        }

        LOG.info("All listeners finished");

        LOG.debug("method finished");
    }

    protected List<NamedDocumentParsingListener> getParsingListeners() {
        return parsingListeners;
    }


}
