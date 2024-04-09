Feature: Testing login controller

  Scenario: Test user login - valid username and password
    Given User calls login endpoint for valid user
    When System processes login request for valid user
    Then Validate response from the login endpoint for valid user

  Scenario: Test user login - invalid user
    Given User calls login endpoint for invalid user
    When System processes login request for invalid user
    Then Validate response from the login endpoint for invalid user

  Scenario: Test user display - valid username
    Given User calls display user endpoint for valid username
    When System processes display user request for valid username
    Then Validate response from the display user endpoint for valid username


