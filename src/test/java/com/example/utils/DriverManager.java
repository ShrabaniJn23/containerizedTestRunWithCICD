package com.example.utils;

import org.openqa.selenium.WebDriver;

public class DriverManager {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<String> browserName = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver d) {
        driver.set(d);
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            try {
                driver.get().quit();
            } catch (Exception ignored) {
            }
            driver.remove();
        }
        browserName.remove();
    }
    
    public static String getBrowserName() {
        return browserName.get();
    }
    
    public static void setBrowserName(String name) {
        browserName.set(name);
    }
}
