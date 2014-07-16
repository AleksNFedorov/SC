package com.scejtesting.core.concordion.extension.documentparsing;

import com.scejtesting.core.concordion.command.SaveResultsCommand;
import com.scejtesting.core.concordion.command.ScejCommand;
import com.scejtesting.core.config.Specification;
import com.scejtesting.core.context.TestContextService;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by aleks on 7/15/14.
 */
public class SaveResultsCommandDocumentEnricherTest {


    @Before
    public void initTest() {
        Specification specification = mock(Specification.class);
        when(specification.getLocation()).thenReturn("/rootSpecificaiton.html");

        com.scejtesting.core.config.Test test = mock(com.scejtesting.core.config.Test.class);
        when(test.getSpecification()).thenReturn(specification);
        when(test.getDefaultTestClass()).thenCallRealMethod();

        new TestContextService().createNewTestContext(test);

    }


    @Test(expected = RuntimeException.class)
    public void noDocumentTest() {
        new SaveResultsCommandDocumentEnricher().beforeParsing(null);
    }

    @Test
    public void noBodyElementTest() {
        Element root = new Element("html");
        Document document = new Document(root);

        new SaveResultsCommandDocumentEnricher().beforeParsing(document);

        Element body = root.getFirstChildElement("body");
        Element saveResultsCommandElement = body.getFirstChildElement("div");
        Attribute saveCommandAttribute = saveResultsCommandElement.getAttribute(0);

        Assert.assertEquals(1, body.getChildElements().size());
        Assert.assertEquals(new SaveResultsCommand().getCommandName(), saveCommandAttribute.getLocalName());
        Assert.assertEquals(ScejCommand.SCEJ_TESTING_NAME_SPACE, saveCommandAttribute.getNamespaceURI());
    }

    @Test
    public void withBodyElementTest() {
        Element root = new Element("html");
        Element body = new Element("body");
        Document document = new Document(root);
        root.appendChild(body);

        new SaveResultsCommandDocumentEnricher().beforeParsing(document);

        Element saveResultsCommandElement = body.getFirstChildElement("div");
        Attribute saveCommandAttribute = saveResultsCommandElement.getAttribute(0);

        Assert.assertEquals(1, body.getChildElements().size());
        Assert.assertEquals(new SaveResultsCommand().getCommandName(), saveCommandAttribute.getLocalName());
        Assert.assertEquals(ScejCommand.SCEJ_TESTING_NAME_SPACE, saveCommandAttribute.getNamespaceURI());
    }


}
