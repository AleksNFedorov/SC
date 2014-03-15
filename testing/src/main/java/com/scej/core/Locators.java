package com.scej.core;

import java.io.IOException;
import java.util.Properties;

public class Locators {

    private static final Properties applicationProperties = new Properties();


    public enum BrowserWindowState {
        Closed,
        Open
    }

    public enum Browser {
        Chrome,
        InternetExplorer,
        FireFox,
        Opera
    }

    static {
        try {
            applicationProperties.load(Locators.class.getResourceAsStream("/app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public final static String pageXpath = "//html";

    public static String getApplicationProperty(String propertyKey) {
        return applicationProperties.getProperty(propertyKey);
    }

}
