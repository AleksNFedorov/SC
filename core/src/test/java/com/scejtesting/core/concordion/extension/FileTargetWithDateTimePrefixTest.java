package com.scejtesting.core.concordion.extension;

import com.scejtesting.core.Constants;
import org.concordion.api.Resource;
import org.concordion.api.Target;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class FileTargetWithDateTimePrefixTest {

    public static final String JAVA_TMP_DIR = System.getProperty("java.io.tmpdir");

    @Test
    public void finishTest() {
        System.clearProperty(Constants.PROPERTY_OUTPUT_DIR);
        System.clearProperty(Constants.PROPERTY_LAUNCH_RESULT_FOLDER_PATTERN_DIR);
    }

    @Test
    public void testTargetPrefixWithExplicitLogFolder() {


        System.setProperty(Constants.PROPERTY_OUTPUT_DIR, "/someTestFolder");
        Target target = new FileTargetWithCustomPrefix(FileTargetWithCustomPrefix.getBaseOutputDir());
        Resource testResource = new Resource("/someResource");
        String resultFile = target.resolvedPathFor(testResource);
        Assert.assertTrue(resultFile.endsWith("someResource"));
        Assert.assertTrue(resultFile.matches(".*[0-9]{2}-[0-9]{2}-[0-9]{2}.*"));
        Assert.assertTrue(resultFile.contains("/someTestFolder"));
    }

    @Test
    public void testTargetPrefixWithDefault() {

        Target target = new FileTargetWithCustomPrefix(FileTargetWithCustomPrefix.getBaseOutputDir());
        Resource testResource = new Resource("/someResource");
        String resultFile = target.resolvedPathFor(testResource);

        Assert.assertTrue(resultFile.endsWith("someResource"));
        Assert.assertTrue(resultFile.matches(".*[0-9]{2}-[0-9]{2}-[0-9]{2}.*"));
    }

    @Test
    public void fileTargetWithCustomPattern() {

        System.setProperty(Constants.PROPERTY_LAUNCH_RESULT_FOLDER_PATTERN_DIR, "12345");

        Target target = new FileTargetWithCustomPrefix();
        Resource testResource = new Resource("/someResource");
        String resultFile = target.resolvedPathFor(testResource);

        Assert.assertTrue(resultFile.endsWith("someResource"));
        Assert.assertTrue(resultFile.matches(".*12345.*"));
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
