package com.scejtesting.core.concordion.extension;

import com.scejtesting.core.config.Specification;
import com.scejtesting.core.config.SpecificationLocatorService;
import org.concordion.api.Resource;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by aleks on 7/9/14.
 */
public class ClassPathSpecificationSourceTest {

    @Test
    public void canFindTest() {

        String pathToRealFile = "/com/scejtesting/core/concordion/extension/HeadSpecification.html";

        Specification parentSpecification = new Specification("/parent.html");
        String pathToSpecification = new SpecificationLocatorService().
                buildUniqueSpecificationHREF(parentSpecification, pathToRealFile);

        Resource specResource = new Resource(pathToSpecification);

        boolean result = new ClassPathSpecificationSource().canFind(specResource);

        Assert.assertTrue(result);
    }

    @Test
    public void canFindTestUnknownFile() {

        String pathToRealFile = "/someUnknkownHtml" + System.currentTimeMillis() + ".html";

        Specification parentSpecification = new Specification("/parent.html");
        String pathToSpecification = new SpecificationLocatorService().
                buildUniqueSpecificationHREF(parentSpecification, pathToRealFile);

        Resource specResource = new Resource(pathToSpecification);

        boolean result = new ClassPathSpecificationSource().canFind(specResource);

        Assert.assertFalse(result);
    }


    @Test
    public void createInputStreamTest() throws IOException {
        String pathToRealFile = "/com/scejtesting/core/concordion/extension/HeadSpecification.html";

        Specification parentSpecification = new Specification("/parent.html");
        String pathToSpecification = new SpecificationLocatorService().
                buildUniqueSpecificationHREF(parentSpecification, pathToRealFile);

        Resource specResource = new Resource(pathToSpecification);

        InputStream result = new ClassPathSpecificationSource().createInputStream(specResource);

        Assert.assertNotNull(result);

    }

    @Test(expected = IOException.class)
    public void createInputStreamUnknownFileTest() throws IOException {

        String pathToRealFile = "/someUnknkownHtml" + System.currentTimeMillis() + ".html";

        Specification parentSpecification = new Specification("/parent.html");
        String pathToSpecification = new SpecificationLocatorService().
                buildUniqueSpecificationHREF(parentSpecification, pathToRealFile);

        Resource specResource = new Resource(pathToSpecification);

        new ClassPathSpecificationSource().createInputStream(specResource);
    }


}
