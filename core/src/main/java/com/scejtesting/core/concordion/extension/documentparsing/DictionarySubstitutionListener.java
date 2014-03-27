package com.scejtesting.core.concordion.extension.documentparsing;

import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import org.concordion.api.listener.DocumentParsingListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.regex.Pattern;

/**
 * User: Fedorovaleks
 * Date: 3/18/14
 */
public class DictionarySubstitutionListener implements DocumentParsingListener {

    public static final String SUBSTITUTION_NODES_XPATH = "//*[contains(text(), '${')]";
    private static final Logger LOG = LoggerFactory.getLogger(DictionarySubstitutionListener.class);
    public static Pattern SUBSTITUTE_PATTERN = Pattern.compile(".*\\$\\{.{1,}\\}.*");


    @Override
    public void beforeParsing(Document document) {
        LOG.debug("method invoked");


        Nodes allHrefNodes = document.query(SUBSTITUTION_NODES_XPATH);

        if (allHrefNodes.size() == 0) {
            LOG.info("Nothing to substitute in specification [{}]", getCurrentTestContext().getCurrentSpecificationContext().getSpecification());
            return;
        }
        LOG.info("Found [{}]", allHrefNodes.size());

        Properties substitutionDictionary = getDictionaryLoaderService().buildSubstitutionDictionary();


        for (int i = 0; i < allHrefNodes.size(); ++i) {
            Element nodeWithSubstitutionTemplate = (Element) allHrefNodes.get(i);
            LOG.info("Processing [{}] node", nodeWithSubstitutionTemplate);
            String elementValue = nodeWithSubstitutionTemplate.getValue();
            if (needSubstituteContent(elementValue)) {
                String stringWithSubstitutions = resolveValueReplacement(nodeWithSubstitutionTemplate.getValue(), substitutionDictionary);
                setValueToElement(nodeWithSubstitutionTemplate, stringWithSubstitutions);
            }
        }
        LOG.debug("method finished");

    }

    protected TestContext getCurrentTestContext() {
        return new TestContextService().getCurrentTestContext();
    }

    private void setValueToElement(Element element, String content) {
        element.removeChildren();
        element.appendChild(new nu.xom.Text(content));
        LOG.info("Element value replaced to [{}]", content);
    }

    private boolean needSubstituteContent(String elementContent) {
        return SUBSTITUTE_PATTERN.matcher(elementContent).matches();
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
        int substitutionBeginIndex = value.lastIndexOf("${");
        int substitutionEndIndex = value.lastIndexOf('}');

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


}
