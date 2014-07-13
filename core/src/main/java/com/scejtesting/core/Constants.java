package com.scejtesting.core;

/**
 * Created by ofedorov on 6/17/14.
 */
public final class Constants {

    // JVM Property
    // JVM property to specify test configuration file location
    public static final String SUITE_CONFIG_PROPERTY_KEY = "scejtesting.suite";

    //Default test configuration file name, used when no user config specified
    public static final String DEFAULT_CONFIG_NAME = "scejsuite.xml";

    // JVM Property
    // Coma separated list of tests which shold be run
    public static final String TESTS_TO_RUN_PROPERTY_KEY = "scejtesting.run.tests";

    // JVM Property
    //Path to top results folder
    public static final String PROPERTY_OUTPUT_DIR = "concordion.output.dir";

    // JVM Property
    //Test launch results folder pattern, created per each launch
    public static final String PROPERTY_LAUNCH_RESULT_FOLDER_PATTERN_DIR = "scejtesting.launch.result.folder.pattern";

    // Default test launch results folder pattern
    public static final String DATE_TIME_PREFIX_FORMAT = "dd-MM-yy_HH-mm-ss";

    // Valocity tamplate results
    //HTML tag in specification for append aggregated child specification run results
    public static final String CUSTOM_RESULTS_HOST_TAG = "scejresults";

    //Path to Results Velocity template file
    public static final String VELOCITY_RESULTS_TEMPLATE_FILE_PROPERTY = "scejtesting.resultsTemplateFile";

    //Default velocity template file
    public static final String VELOCITY_DEFAULT_TEMPLATE_FILE = "results.vm";

    //System property for global substitution dictionary
    public static final String GLOBAL_DICTIONARY = "scejGlobalDictionary.properties";

}
