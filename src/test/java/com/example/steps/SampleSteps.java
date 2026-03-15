package com.example.steps;

import com.example.utils.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class SampleSteps {
    private WebDriver driver = DriverManager.getDriver();

    @Given("I open the herokuapp site")
    public void i_open_the_herokuapp_site() {
        WebDriver driver = DriverManager.getDriver();
        driver.get("https://the-internet.herokuapp.com");
    }

    @Then("the page title contains {string}")
    public void the_page_title_contains(String expected) {
        WebDriver driver = DriverManager.getDriver();
        String title = driver.getTitle();
        Assert.assertTrue(title.contains(expected), "Title did not contain expected text. Actual: " + title);
    }
}
