package com.scej.core.concordion.extension.documentparsing;

import com.scej.core.TestContext;
import com.scej.core.config.Specification;
import com.scej.core.config.SpecificationLocatorService;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import org.concordion.api.listener.DocumentParsingListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class ChildSpecificationLinkUpdater implements DocumentParsingListener {

    private static final Logger LOG = LoggerFactory.getLogger(ChildSpecificationLinkUpdater.class);


    @Override
    public void beforeParsing(Document document) {
        LOG.debug("method invoked");
        Specification specification = TestContext.getInstance().getCurrentSpecificationContext().getSpecification();
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
        if (hrefAttribute != null) {
            String link = hrefAttribute.getValue();
            link = SpecificationLocatorService.getService().buildUniqueSpecificationHREF(specification, link);
            hrefAttribute.setValue(link);
        }
        LOG.debug("method finished");
    }


}