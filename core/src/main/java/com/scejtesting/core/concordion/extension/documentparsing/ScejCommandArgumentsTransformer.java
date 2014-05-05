package com.scejtesting.core.concordion.extension.documentparsing;

import com.scejtesting.core.concordion.command.ScejCommand;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Nodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by aleks on 5/4/14.
 */
public class ScejCommandArgumentsTransformer implements NamedDocumentParsingListener {

    private static final Logger LOG = LoggerFactory.getLogger(ScejCommandArgumentsTransformer.class);

    @Override
    public void beforeParsing(Document document) {

        LOG.debug("method invoked");

        Nodes allHrefNodes = document.query("//@*[namespace-uri()='" + ScejCommand.SCEJ_TESTING_NAME_SPACE + "']");

        LOG.info("Found [{}] attributes for check", allHrefNodes.size());


        for (int i = 0; i < allHrefNodes.size(); ++i) {
            Attribute currentAttribute = (Attribute) allHrefNodes.get(i);
            if (needModifyArgumentsString(currentAttribute)) {
                LOG.info("Modifiing attribute [{}]", currentAttribute.getType());
                String newAttributeValue = buildNewAttributeValue(currentAttribute.getValue());
                currentAttribute.setValue(newAttributeValue);
                LOG.info("Attribute [{}] value updated to [{}]", currentAttribute.getType(), newAttributeValue);
            }
        }

        LOG.debug("method finished");
    }

    private boolean needModifyArgumentsString(Attribute attribute) {
        String attributeValue = attribute.getValue().trim();
        return attributeValue.startsWith("#") && attributeValue.contains(",");
    }

    private String buildNewAttributeValue(String currentValue) {
        return "asList(" + currentValue + ")";
    }

    @Override
    public String getParserName() {
        return "Scej command arguments transformer";
    }
}
