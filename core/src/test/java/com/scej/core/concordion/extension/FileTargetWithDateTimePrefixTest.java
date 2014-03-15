package com.scej.core.concordion.extension;

import org.concordion.api.Resource;
import org.concordion.internal.FileTarget;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class FileTargetWithDateTimePrefixTest {

    @Test
    public void testTargetPrefixWithExplicitLogFolder() {

        System.setProperty("concordion.output.dir", "someTestFolder");
        FileTarget target = new FileTargetWithDateTimePrefix();
        Resource testResource = new Resource("/someResource");
        File resultFile = target.getFile(testResource);

        Assert.assertTrue(resultFile.getPath().endsWith("someResource"));
        Assert.assertTrue(resultFile.getParent().matches(".*[0-9]{2}-[0-9]{2}-[0-9]{2}.*"));
    }

    @Test
    public void testTargetPrefixWithDefault() {

        FileTarget target = new FileTargetWithDateTimePrefix();
        Resource testResource = new Resource("/someResource");
        File resultFile = target.getFile(testResource);

        Assert.assertTrue(resultFile.getPath().endsWith("someResource"));
        Assert.assertTrue(resultFile.getParent().matches(".*[0-9]{2}-[0-9]{2}-[0-9]{2}.*"));
    }

}
