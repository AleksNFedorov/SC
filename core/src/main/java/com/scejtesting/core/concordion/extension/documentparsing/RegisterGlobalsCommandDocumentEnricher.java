package com.scejtesting.core.concordion.extension.documentparsing;

import com.scejtesting.core.concordion.command.ScejCommand;
import com.scejtesting.core.runner.ScejSuiteRunner;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import org.concordion.api.listener.DocumentParsingListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: Fedorovaleks
 * Date: 21.03.14
 */
public class RegisterGlobalsCommandDocumentEnricher implements DocumentParsingListener {

    public static final String REGISTER_GLOBALS_DEFAULT_NAMESPACE = "scejregisterglobalsinline";
    private static final Logger LOG = LoggerFactory.getLogger(ChildSpecificationLinkUpdater.class);

    @Override
    public void beforeParsing(Document document) {
        LOG.debug("method invoked");
        Element documentBody = getDocumentBody(document);
        LOG.debug("Document body acquired");
        Element registerGlobalsCommandElement = buildGlobalIntiCommandElement();
        LOG.debug("register globals command has been built [{}]", registerGlobalsCommandElement.toXML());
        documentBody.insertChild(registerGlobalsCommandElement, 0);
        LOG.info("Register globals command has been appended");
        LOG.debug("method finished");
    }

    private Element buildGlobalIntiCommandElement() {
        Element registerGlobalsCommandElement = new Element("div");
        Attribute registerGlobalsAttribute = new Attribute(
                ScejCommand.CommandType.RegisterGlobalVariables.getSpecificationCommand(), "");

        registerGlobalsAttribute.setNamespace(REGISTER_GLOBALS_DEFAULT_NAMESPACE, ScejSuiteRunner.SCEJ_TESTING_NAME_SPACE);
        registerGlobalsCommandElement.addAttribute(registerGlobalsAttribute);
        return registerGlobalsCommandElement;
    }


    private Element getDocumentBody(Document document) {
        Element body = document.getRootElement().getFirstChildElement("body");
        if (body == null) {
            LOG.warn("No [body] node in specification, [body] node has been added");
            body = new Element("body");
            document.getRootElement().insertChild(body, 0);
        }
        return body;
    }

    private void addScejNameSpaceToDocument(Document document) {
        document.getRootElement().addNamespaceDeclaration(REGISTER_GLOBALS_DEFAULT_NAMESPACE,
                ScejSuiteRunner.SCEJ_TESTING_NAME_SPACE);
    }


}
