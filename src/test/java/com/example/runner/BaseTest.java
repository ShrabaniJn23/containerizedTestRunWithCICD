package com.example.runner;

import com.example.utils.DriverManager;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTest extends AbstractTestNGCucumberTests {
    
    @BeforeTest
    @Parameters({"browser"})
    public void setUpBrowser(@Optional("chrome") String browser) {
        System.out.println("Setting browser for thread: " + browser);
        DriverManager.setBrowserName(browser);
    }
}
