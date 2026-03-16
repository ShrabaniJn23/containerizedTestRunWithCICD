package com.example.hooks;

import com.example.config.ConfigManager;
import com.example.utils.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class TestHooks {

    @Before
    public void setUp() {
        String driverMode = ConfigManager.getDriverMode();
        System.out.println("Driver mode: " + driverMode);

        // Get browser from ThreadLocal (set by BaseTest)
        String browser = DriverManager.getBrowserName();
        if (browser == null) {
            browser = ConfigManager.getDockerBrowser();
            DriverManager.setBrowserName(browser);
        }
        System.out.println("Browser: " + browser);

        WebDriver driver;
        
        if ("remote".equalsIgnoreCase(driverMode)) {
            driver = setupRemoteDriver(browser);
        } else {
            driver = setupLocalDriver(browser);
        }
        
        DriverManager.setDriver(driver);
    }

    private WebDriver setupLocalDriver(String browser) {
        try {
            System.out.println("Setting up LOCAL WebDriver for: " + browser);
            WebDriverManager.chromedriver()
                .clearDriverCache()
                .clearResolutionCache()
                .forceDownload()
                .setup();
            System.out.println("ChromeDriver setup completed successfully");
        } catch (Exception e) {
            System.out.println("WebDriverManager setup error: " + e.getMessage());
            e.printStackTrace();
        }
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        
        if (ConfigManager.isHeadless()) {
            System.out.println("Running in HEADLESS mode");
            options.addArguments("--headless=new");
        }
        
        return new ChromeDriver(options);
    }

    private WebDriver setupRemoteDriver(String browserName) {
        try {
            System.out.println("Setting up REMOTE WebDriver (Docker/Selenium Grid)...");
            String dockerHost = ConfigManager.getDockerHost();
            
            System.out.println("Docker Host: " + dockerHost);
            System.out.println("Browser: " + browserName);
            
            URL gridUrl = new URL(dockerHost);
            RemoteWebDriver driver;
            
            if ("firefox".equalsIgnoreCase(browserName)) {
                FirefoxOptions options = new FirefoxOptions();
                driver = new RemoteWebDriver(gridUrl, options);
                System.out.println("Firefox Remote WebDriver connected successfully");
            } else if ("edge".equalsIgnoreCase(browserName)) {
                EdgeOptions options = new EdgeOptions();
                driver = new RemoteWebDriver(gridUrl, options);
                System.out.println("Edge Remote WebDriver connected successfully");
            } else {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*");
                driver = new RemoteWebDriver(gridUrl, options);
                System.out.println("Chrome Remote WebDriver connected successfully");
            }
            
            return driver;
        } catch (Exception e) {
            System.out.println("Remote WebDriver setup error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to setup Remote WebDriver", e);
        }
    }

    @After
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
