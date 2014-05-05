package com.scejtesting.core.concordion.extension.documentparsing;

import org.concordion.api.listener.DocumentParsingListener;

/**
 * Created by aleks on 5/4/14.
 */
public interface NamedDocumentParsingListener extends DocumentParsingListener {

    String getParserName();

}
