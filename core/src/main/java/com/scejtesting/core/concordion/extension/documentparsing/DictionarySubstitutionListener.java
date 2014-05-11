package com.scejtesting.core.concordion.extension.documentparsing;

import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import nu.xom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * User: Fedorovaleks
 * Date: 3/18/14
 */
public class DictionarySubstitutionListener implements NamedDocumentParsingListener {

    public static final String SUBSTITUTION_NODES_TEXT_XPATH = "//*[contains(text(), '${')]";
    public static final String SUBSTITUTION_NODES_ATTRIBUTES_XPATH = "//@*";

    public static final String SUBSTITUTION_NODES_XPATH = "//*[contains(text(), '${')]";
    public static final String REPLACEMENT_START = "${";
    public static final char REPLACEMENT_END = '}';
    private static final Logger LOG = LoggerFactory.getLogger(DictionarySubstitutionListener.class);

    @Override
    public void beforeParsing(Document document) {

        Properties substitutionDictionary = getDictionaryLoaderService().buildSubstitutionDictionary();

        updateNodes(document.query(SUBSTITUTION_NODES_TEXT_XPATH), substitutionDictionary);
        updateNodes(document.query(SUBSTITUTION_NODES_ATTRIBUTES_XPATH), substitutionDictionary);

    }

    private void updateNodes(Nodes nodesToUpdate, Properties substitutionDictionary) {
        LOG.debug("method invoked");


        if (nodesToUpdate.size() == 0) {
            LOG.info("Nothing to substitute in specification [{}]", getCurrentTestContext().getCurrentSpecificationContext().getSpecification());
            return;
        }

        LOG.info("Found [{}]", nodesToUpdate.size());


        for (int i = 0; i < nodesToUpdate.size(); ++i) {
            Node nodeWithSubstitutionTemplate = nodesToUpdate.get(i);
            LOG.info("Processing [{}] node", nodeWithSubstitutionTemplate);
            String elementValue = nodeWithSubstitutionTemplate.getValue();
            if (needSubstituteContent(elementValue)) {
                String stringWithSubstitutions = resolveValueReplacement(nodeWithSubstitutionTemplate.getValue(), substitutionDictionary);
                setValueToNode(nodeWithSubstitutionTemplate, stringWithSubstitutions);
            }
        }
        LOG.debug("method finished");

    }

    private void setValueToNode(Node node, String newContent) {
        if (node instanceof Element) {
            LOG.debug("Node detected as Element");
            setValueToNode((Element) node, newContent);
        } else if (node instanceof Attribute) {
            LOG.debug("Node detected as Attribute");
            setValueToNode((Attribute) node, newContent);
        } else {
            throw new IllegalStateException("Illegal node type [" + node.getClass().getSimpleName() + "]");
        }

    }

    private void setValueToNode(Element element, String content) {
        element.removeChildren();
        element.appendChild(new nu.xom.Text(content));
        LOG.info("Element value replaced to [{}]", content);
    }

    private void setValueToNode(Attribute attribute, String content) {
        attribute.setValue(content);
        LOG.info("Attribute value replaced to [{}]", content);
    }


    private boolean needSubstituteContent(String elementContent) {
        int substitutionBeginIndex = elementContent.lastIndexOf(REPLACEMENT_START);
        int substitutionEndIndex = elementContent.lastIndexOf(REPLACEMENT_END);
        return substitutionBeginIndex > -1 && substitutionEndIndex > substitutionBeginIndex;
    }


    private String resolveValueReplacement(String elementValue, Properties dictionary) {
        LOG.debug("method invoked");

        String stringWithSubstitution = elementValue;

        while (needSubstituteContent(stringWithSubstitution)) {
            stringWithSubstitution = replaceOneValue(stringWithSubstitution, dictionary);
        }

        LOG.debug("Replacement finished for value [{}], replacement [{}]", elementValue, stringWithSubstitution);
        LOG.debug("method finished");
        return stringWithSubstitution;
    }

    private String replaceOneValue(String value, Properties dictionary) {
        int substitutionBeginIndex = value.lastIndexOf(REPLACEMENT_START);
        int substitutionEndIndex = value.lastIndexOf(REPLACEMENT_END);

        String replacementKey = value.substring(substitutionBeginIndex + 2, substitutionEndIndex);
        String replacementValue = dictionary.getProperty(replacementKey);
        if (replacementValue == null) {
            LOG.error("Replacement for [{}] does not exist", replacementKey);
            throw new IllegalArgumentException("Replacement does not exist");
        }

        String stringToReplace = value.substring(substitutionBeginIndex, substitutionEndIndex + 1);

        LOG.info("Replacement found for template [{}], replacement [{}]", stringToReplace, replacementValue);
        return value.replace(stringToReplace, replacementValue);
    }


    protected DictionaryLoaderService getDictionaryLoaderService() {
        return new DictionaryLoaderService();
    }

    @Override
    public String getParserName() {
        return "Dictionary substitution";
    }

    protected TestContext getCurrentTestContext() {
        return new TestContextService().getCurrentTestContext();
    }
}
