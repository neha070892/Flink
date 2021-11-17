Feature: Flink Feature File

  @TC12345 @Regression
  Scenario: 01 -TC12345- Verification of file upload and download under

    Given User navigated to URL
    And add least expensive "Moisturizers" to cart if weather is "below 19"
    And add least expensive "Sunscreens" to cart if weather is "above 34"
    And click on "Cart" button
    And click on "Pay with Card" button
    And verify execution completed successfully

