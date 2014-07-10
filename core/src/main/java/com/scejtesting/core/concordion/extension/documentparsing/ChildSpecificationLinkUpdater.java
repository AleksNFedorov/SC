package com.scejtesting.core.concordion.extension.documentparsing;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.SpecificationLocatorService;
import com.scejtesting.core.context.TestContext;
import com.scejtesting.core.context.TestContextService;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class ChildSpecificationLinkUpdater implements NamedDocumentParsingListener {

    private static final Logger LOG = LoggerFactory.getLogger(ChildSpecificationLinkUpdater.class);

    private final TestContext currentTestContext = new TestContextService().getCurrentTestContext();

    @Override
    public void beforeParsing(Document document) {
        LOG.debug("method invoked");
        Specification specification = getCurrentTestContext().getCurrentSpecificationContext().getSpecification();
        Nodes allHrefNodes = document.query("//a[@href]");
        LOG.info("Found [{}]", allHrefNodes.size());
        for (int i = 0; i < allHrefNodes.size(); ++i) {
            Element currentHrefNode = (Element) allHrefNodes.get(i);
            LOG.info("Processing [{}] node", currentHrefNode);
            if (isConcordionRunnerHrefNode(currentHrefNode)) {
                LOG.info("Node is a concordion node");
                updateSpecificationPath(currentHrefNode, specification);
            }
        }
        LOG.debug("method finished");
    }

    private boolean isConcordionRunnerHrefNode(Element hrefNode) {
        return hrefNode.getAttribute("run", "http://www.concordion.org/2007/concordion") != null;
    }

    private void updateSpecificationPath(Element hrefNode, Specification specification) {
        LOG.debug("method invoked");
        Attribute hrefAttribute = hrefNode.getAttribute("href");
        LOG.info("HREF attribute is [{}]", hrefAttribute);
        String link = hrefAttribute.getValue();
        link = new SpecificationLocatorService().buildUniqueSpecificationHREF(specification, link);
        hrefAttribute.setValue(link);
        LOG.debug("method finished");
    }

    @Override
    public String getParserName() {
        return "Child specification linker";
    }

    TestContext getCurrentTestContext() {
        return currentTestContext;
    }

}
