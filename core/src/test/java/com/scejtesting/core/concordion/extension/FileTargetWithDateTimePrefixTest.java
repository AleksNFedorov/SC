package com.scejtesting.core.concordion.extension;

import com.scejtesting.core.Constants;
import org.concordion.api.Resource;
import org.concordion.api.Target;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class FileTargetWithDateTimePrefixTest {

    @After
    public void finishTest() {
        System.clearProperty(Constants.PROPERTY_OUTPUT_DIR);
        System.clearProperty(Constants.PROPERTY_LAUNCH_RESULT_FOLDER_PATTERN_DIR);
    }

    @Test
    public void testTargetPrefixWithExplicitLogFolder() {

        String tmpFolderAbsolutePath = createTmpFolder("testTargetPrefixWithExplicitLogFolder");

        System.setProperty(Constants.PROPERTY_OUTPUT_DIR, tmpFolderAbsolutePath);
        Target target = new FileTargetWithCustomPrefix(FileTargetWithCustomPrefix.getBaseOutputDir());
        Resource testResource = new Resource("/someResource");
        String resultFile = target.resolvedPathFor(testResource);
        Assert.assertTrue(resultFile.endsWith("someResource"));
        Assert.assertTrue(resultFile.matches(".*[0-9]{2}-[0-9]{2}-[0-9]{2}.*"));
        Assert.assertTrue(resultFile.startsWith(tmpFolderAbsolutePath));
    }

    private String createTmpFolder(String prefix) {
        File tmpFolder = new File(System.getProperty("java.io.tmpdir"),
                prefix + System.currentTimeMillis());
        tmpFolder.deleteOnExit();

        if (!tmpFolder.exists()) {
            tmpFolder.mkdir();
        }
        return tmpFolder.getAbsolutePath();
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
