package com.scejtesting.core.concordion.extension.documentparsing;

import nu.xom.Document;
import org.concordion.internal.XMLParser;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by aleks on 5/4/14.
 */
public class ScejCommandArgumentsTransformerTest {

    @Test
    public void test() throws IOException {

        String pathToSpecificaiton = getClass().getClassLoader().getResource("com/scejtesting/core/concordion/extension/HeadSpecification.html").getFile();

        Document parsedDocument = new XMLParser().parse(new FileInputStream(pathToSpecificaiton));

        ScejCommandArgumentsTransformer transformer = new ScejCommandArgumentsTransformer();

        transformer.beforeParsing(parsedDocument);


    }
}
