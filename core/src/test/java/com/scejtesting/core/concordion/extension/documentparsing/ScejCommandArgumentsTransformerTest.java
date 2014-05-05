package com.scejtesting.core.concordion.extension.documentparsing;

import com.scejtesting.core.concordion.command.ScejCommand;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.concordion.internal.XMLParser;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by aleks on 5/4/14.
 */
public class ScejCommandArgumentsTransformerTest {

    @Test
    public void test() throws IOException {

        String pathToSpecificaiton = getClass().getClassLoader().getResource("com/scejtesting/core/concordion/extension/documentparsing/ScjeCommandArgumentsTransformer.html").getFile();

        Document parsedDocument = new XMLParser().parse(new FileInputStream(pathToSpecificaiton));

        ScejCommandArgumentsTransformer transformer = new ScejCommandArgumentsTransformer();

        transformer.beforeParsing(parsedDocument);

        Nodes allHrefNodes = parsedDocument.query("//@*[namespace-uri()='" + ScejCommand.SCEJ_TESTING_NAME_SPACE + "']");

        Assert.assertEquals(6, allHrefNodes.size());

        validateAttribute(allHrefNodes.get(0), "asList(#TEXT, #var)");
        validateAttribute(allHrefNodes.get(1), "#TEXT");
        validateAttribute(allHrefNodes.get(2), "someMethod(#TEXT)");
        validateAttribute(allHrefNodes.get(3), "someMethod(#TEXT, #var)");
        validateAttribute(allHrefNodes.get(4), "");
        validateAttribute(allHrefNodes.get(5), "asList(#TEXT, #var)");

    }

    private void validateAttribute(Node attribute, String validValue) {
        String attributeValue = attribute.getValue();
        Assert.assertEquals(validValue, attributeValue);
    }
}
