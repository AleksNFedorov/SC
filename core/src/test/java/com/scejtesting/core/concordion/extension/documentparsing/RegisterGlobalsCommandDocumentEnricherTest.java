package com.scejtesting.core.concordion.extension.documentparsing;

import com.scejtesting.core.concordion.command.RegisterGlobalVariablesCommand;
import com.scejtesting.core.runner.ScejSuiteRunner;
import nu.xom.Document;
import nu.xom.Element;
import org.concordion.internal.XMLParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * User: Fedorovaleks
 * Date: 21.03.14
 */
public class RegisterGlobalsCommandDocumentEnricherTest {

    @Test
    public void positiveFlow() throws IOException {
        String pathToSpecification = getClass().getClassLoader().
                getResource("com/scej/core/concordion/extension/documentparsing/RegisterGlobalsTest.html").getFile();

        Document parsedDocument = new XMLParser().parse(new FileInputStream(pathToSpecification));

        RegisterGlobalsCommandDocumentEnricher enricher = new RegisterGlobalsCommandDocumentEnricher();

        enricher.beforeParsing(parsedDocument);

        Element registerGlobalsElement = parsedDocument.getRootElement().
                getFirstChildElement("body").
                getFirstChildElement("div");

        Assert.assertNotNull(registerGlobalsElement.getAttribute(
                new RegisterGlobalVariablesCommand().getCommandType(),
                ScejSuiteRunner.SCEJ_TESTING_NAME_SPACE));

    }

    @Test
    public void registerGlobalsNoBody() throws IOException {
        String pathToSpecification = getClass().getClassLoader().
                getResource("com/scej/core/concordion/extension/documentparsing/RegisterGlobalsNoBodyTest.html").getFile();

        Document parsedDocument = new XMLParser().parse(new FileInputStream(pathToSpecification));

        RegisterGlobalsCommandDocumentEnricher enricher = new RegisterGlobalsCommandDocumentEnricher();

        enricher.beforeParsing(parsedDocument);

        Element registerGlobalsElement = parsedDocument.getRootElement().
                getFirstChildElement("body").
                getFirstChildElement("div");

        Assert.assertNotNull(registerGlobalsElement.getAttribute(
                new RegisterGlobalVariablesCommand().getCommandType(),
                ScejSuiteRunner.SCEJ_TESTING_NAME_SPACE));
    }
}
