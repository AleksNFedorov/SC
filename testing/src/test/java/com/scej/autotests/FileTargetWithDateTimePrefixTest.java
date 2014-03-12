package com.scej.autotests;

import com.scej.autotests.core.integration.extension.FileTargetWithDateTimePrefix;
import org.concordion.api.Resource;
import org.concordion.internal.FileTarget;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 * Date: 01.02.14
 * Time: 7:34
 * To change this template use File | Settings | File Templates.
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
