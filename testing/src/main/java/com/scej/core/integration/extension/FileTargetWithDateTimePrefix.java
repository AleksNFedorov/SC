package com.scejtesting.core.integration.extension;

import org.concordion.internal.FileTarget;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Fedorovaleks
 * Date: 01.02.14
 * Time: 7:21
 * To change this template use File | Settings | File Templates.
 */
public class FileTargetWithDateTimePrefix extends FileTarget {

    private static final String DATE_TIME_PREFIX_FORMAT = "dd-MM-yy_HH-mm-ss";
    public static final String PROPERTY_OUTPUT_DIR = "concordion.output.dir";

    private static final File baseOutDir = getBaseOutputDir();

    public FileTargetWithDateTimePrefix() {
        super(baseOutDir);
    }


    private static File getBaseOutputDir() {
        String outputPath = System.getProperty(PROPERTY_OUTPUT_DIR);
        SimpleDateFormat prefixFormatter = new SimpleDateFormat(DATE_TIME_PREFIX_FORMAT);
        String folderPrefix = prefixFormatter.format(new Date(System.currentTimeMillis()));
        if (outputPath == null) {
            return new File(new File(System.getProperty("java.io.tmpdir"), "concordion"), folderPrefix);
        }
        return new File(outputPath, folderPrefix);
    }
}
