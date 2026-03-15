package com.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";

    static {
        try {
            InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE);
            if (input != null) {
                properties.load(input);
                input.close();
            } else {
                System.err.println("config.properties not found in classpath");
            }
        } catch (IOException e) {
            System.err.println("Error loading config.properties: " + e.getMessage());
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    public static String getDriverMode() {
        return getProperty("driver.mode", "local");
    }

    public static String getDockerHost() {
        return getProperty("docker.host", "http://localhost:4444");
    }

    public static String getDockerBrowser() {
        return getProperty("docker.browser", "chrome");
    }

    public static String getBrowser() {
        return getProperty("browser", "chrome");
    }

    public static boolean isHeadless() {
        return getBooleanProperty("headless", false);
    }
}
