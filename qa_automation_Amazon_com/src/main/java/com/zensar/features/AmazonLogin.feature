Feature: AmazonLogin

  Background: 
    Given User navigates to Amazon Page and Login

  Scenario Outline: Verify user able to Login on Amazon India #<DataSetName>
    When user logs in to Amazon by Entering Username & Password
    Then user search for Samsung mobile phone
    Then Select the Mobile phone and move to kart
		Then Click on proceed to checkout
		Then Check for Payment Details
    
    #And User Navigates to confirm Project setup page & Clicks on Confirm Project Setup
    Examples: 
      | DataSetName |
      | ValidLogin  |