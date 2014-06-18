package com.scejtesting.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by ofedorov on 6/18/14.
 */
public class UtilsTest {

    @Test
    public void nullResultTest() {
        Assert.assertNull(new Utils().resolveResourcePath(null));
        Assert.assertNull(new Utils().resolveResourcePath("comeUnexisedFile" + System.currentTimeMillis()));
    }

    @Test
    public void absolutePathTest() {

        String pathToFile = "scejsuite.xml";

        String absolutePathToFile = getClass().getClassLoader().getResource(pathToFile).getFile();

        String utilsResolvedPath = new Utils().resolveResourcePath(absolutePathToFile);

        Assert.assertEquals(absolutePathToFile, utilsResolvedPath);
    }

    @Test
    public void relativePathTest() {

        String pathToFile = "scejsuite.xml";

        String absolutePathToFile = getClass().getClassLoader().getResource(pathToFile).getFile();

        String utilsResolvedPath = new Utils().resolveResourcePath(pathToFile);

        Assert.assertEquals(absolutePathToFile, utilsResolvedPath);
    }
}