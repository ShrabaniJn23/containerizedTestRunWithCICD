Feature: Test herokuapp scenarios

  Scenario: Open herokuapp.com and verify title
    Given I open the herokuapp site
    Then the page title contains "The Internet"
