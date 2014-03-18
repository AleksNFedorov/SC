package com.scej.core.concordion.extension;

import org.concordion.api.Resource;
import org.concordion.api.Target;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class FileTargetWithDateTimePrefixTest {

    @Test
    public void testTargetPrefixWithExplicitLogFolder() {

        System.setProperty("concordion.output.dir", "someTestFolder");
        Target target = new FileTargetWithCustomPrefix();
        Resource testResource = new Resource("/someResource");
        String resultFile = target.resolvedPathFor(testResource);

        Assert.assertTrue(resultFile.endsWith("someResource"));
        Assert.assertTrue(resultFile.matches(".*[0-9]{2}-[0-9]{2}-[0-9]{2}.*"));
    }

    @Test
    public void testTargetPrefixWithDefault() {


        Target target = new FileTargetWithCustomPrefix();
        Resource testResource = new Resource("/someResource");
        String resultFile = target.resolvedPathFor(testResource);

        Assert.assertTrue(resultFile.endsWith("someResource"));
        Assert.assertTrue(resultFile.matches(".*[0-9]{2}-[0-9]{2}-[0-9]{2}.*"));
    }

    @Test
    public void fileTargetWithCustomPattern() {

        System.setProperty(FileTargetWithCustomPrefix.PROPERTY_LAUNCH_RESULT_FOLDER_PATTERN_DIR, "12345");

        Target target = new FileTargetWithCustomPrefix();
        Resource testResource = new Resource("/someResource");
        String resultFile = target.resolvedPathFor(testResource);

        Assert.assertTrue(resultFile.endsWith("someResource"));
        Assert.assertTrue(resultFile.matches(".*12345.*"));

        System.clearProperty(FileTargetWithCustomPrefix.PROPERTY_LAUNCH_RESULT_FOLDER_PATTERN_DIR);

    }

    @Test
    public void multipleTestInSameLocation() {

        Target target = new FileTargetWithCustomPrefix();
        Resource testResource = new Resource("/someResource");
        String resultFile = target.resolvedPathFor(testResource);

        Target target2 = new FileTargetWithCustomPrefix();
        String resultFile2 = target.resolvedPathFor(testResource);

        Assert.assertEquals(resultFile, resultFile2);

    }

}
