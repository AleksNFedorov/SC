package com.scej.core.concordion.extension.filetarget;

import org.concordion.internal.FileTarget;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 */
public class FileTargetWithCustomPrefix extends FileTarget {

    private static final String DATE_TIME_PREFIX_FORMAT = "dd-MM-yy_HH-mm-ss";
    public static final String PROPERTY_OUTPUT_DIR = "concordion.output.dir";
    public static final String PROPERTY_LAUNCH_RESULT_FOLDER_PATTERN_DIR = "scej.launch.result.folder.pattern";

    public static final File baseOutDir = getBaseOutputDir();


    public FileTargetWithCustomPrefix() {
        super(baseOutDir);
    }

    private static File getBaseOutputDir() {
        String outputPath = System.getProperty(PROPERTY_OUTPUT_DIR);
        String pattern = System.getProperty(PROPERTY_LAUNCH_RESULT_FOLDER_PATTERN_DIR);
        if (pattern == null)
            pattern = DATE_TIME_PREFIX_FORMAT;
        SimpleDateFormat prefixFormatter = new SimpleDateFormat(pattern);
        String folderPrefix = prefixFormatter.format(new Date(System.currentTimeMillis()));
        if (outputPath == null) {
            return new File(new File(System.getProperty("java.io.tmpdir"), "concordion"), folderPrefix);
        }
        return new File(outputPath, folderPrefix);
    }

}
