Feature: Testing contact controller

  Scenario: Test user add contact details - valid username
    Given User calls add contact endpoint for valid username
    When System processes add contact request for valid username
    Then Validate response from the add contact endpoint for valid username

  Scenario: Test user display contact details - valid username
    Given User calls display contact endpoint for valid username
    When System processes display contact request for valid username
    Then Validate response from the display contact endpoint for valid username
