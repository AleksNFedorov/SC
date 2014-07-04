package com.scejtesting.core.concordion.extension;

import com.scejtesting.core.Constants;
import com.scejtesting.core.Utils;
import org.concordion.internal.FileTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class FileTargetWithCustomPrefix extends FileTarget {

    public static final File resultBaseDir = getBaseOutputDir();
    private static final Logger LOG = LoggerFactory.getLogger(FileTargetWithCustomPrefix.class);

    public FileTargetWithCustomPrefix() {
        super(resultBaseDir);
    }

    public FileTargetWithCustomPrefix(File baseDir) {
        super(baseDir);
    }

    public static File getBaseOutputDir() {
        String outputPath = new Utils().resolveResourcePath(
                System.getProperty(Constants.PROPERTY_OUTPUT_DIR));

        String pattern = System.getProperty(Constants.PROPERTY_LAUNCH_RESULT_FOLDER_PATTERN_DIR);
        if (pattern == null)
            pattern = Constants.DATE_TIME_PREFIX_FORMAT;
        SimpleDateFormat prefixFormatter = new SimpleDateFormat(pattern);
        String folderPrefix = prefixFormatter.format(new Date(System.currentTimeMillis()));
        if (outputPath == null) {
            String defaultResultsFolder = System.getProperty("java.io.tmpdir");
            LOG.warn("No top results output folders found, using system default fomr [java.io.tmpdir][" +
                    defaultResultsFolder + "]");

            return new File(new File(defaultResultsFolder, "concordion"), folderPrefix);
        }
        return new File(outputPath, folderPrefix);
    }

}
