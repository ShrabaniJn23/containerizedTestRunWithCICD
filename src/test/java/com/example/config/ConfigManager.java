package com.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";

    static {
        loadDefaultConfig();
        loadEnvProfile();
    }

    private static void loadDefaultConfig() {
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
            } else {
                System.err.println("config.properties not found in classpath");
            }
        } catch (IOException e) {
            System.err.println("Error loading config.properties: " + e.getMessage());
        }
    }

    private static void loadEnvProfile() {
        String env = getSystemOrEnv("environment", "ENVIRONMENT", "default");
        if (env == null || env.trim().isEmpty()) {
            env = "default";
        }

        if (!"default".equalsIgnoreCase(env)) {
            String profileFile = String.format("config.%s.properties", env.trim().toLowerCase());
            try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(profileFile)) {
                if (input != null) {
                    Properties profile = new Properties();
                    profile.load(input);
                    properties.putAll(profile);
                    System.out.println("Loaded environment profile: " + profileFile);
                } else {
                    System.out.println("Environment file not found: " + profileFile + "; using defaults");
                }
            } catch (IOException e) {
                System.err.println("Error loading " + profileFile + ": " + e.getMessage());
            }
        }
    }

    private static String getSystemOrEnv(String propertyKey, String envKey, String defaultValue) {
        String value = System.getProperty(propertyKey);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }
        value = System.getenv(envKey);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }
        value = properties.getProperty(propertyKey);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }
        return defaultValue;
    }

    public static String getProperty(String key) {
        String value = System.getProperty(key);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }

        String envKey = key.toUpperCase().replace('.', '_');
        value = System.getenv(envKey);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }

        value = properties.getProperty(key);
        if (value != null && !value.trim().isEmpty()) {
            return value.trim();
        }

        return null;
    }

    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value.trim());
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
