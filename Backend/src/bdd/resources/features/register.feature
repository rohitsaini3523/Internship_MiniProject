Feature: Testing register controller

  Scenario: Test user register for new user
    Given User calls register endpoint
    When System processes register request
    Then Validate response from the register endpoint

  Scenario: Test user register for existing user
    Given User calls register endpoint
    When System processes register request
    Then Validate response from the register endpoint for existing user