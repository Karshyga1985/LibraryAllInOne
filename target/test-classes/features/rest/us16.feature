Feature: As a user I want to get JWT Token using API request

  Scenario Outline: Verify that the user can get JWT token
    Given I start an API request
    And Accept header is "application/json"
    And Request Content Type header is "application/x-www-form-urlencoded"
    And I provide "<email>" as "email" and "<password>" as "password" as a request body
    When I send POST request to "/login" endpoint
    Then status code should be 200
    And Response Content type is "application/json"
    And "token" field should not be null
    Examples:
      | email               | password    |
      | librarian10@library | libraryUser |
      | student5@library    | libraryUser |

  Scenario Outline: Verify that the user can not get JWT token if provided incorrect email
    Given I start an API request
    And Accept header is "application/json"
    And Request Content Type header is "application/x-www-form-urlencoded"
    And I provide "<incorrectEmail>" as "email" and "<password>" as "password" as a request body
    When I send POST request to "/login" endpoint
    Then status code should be 404
    And Response Content type is "application/json"
    And the field value for "error" path should be equal to "Sorry, Wrong Email or Password"
    Examples:
      | incorrectEmail       | password    |
      | librar10@library     | libraryUser |
      | student50000@library | libraryUser |

  Scenario Outline: Verify that the user can not get JWT token if provided incorrect password
    Given I start an API request
    And Accept header is "application/json"
    And Request Content Type header is "application/x-www-form-urlencoded"
    And I provide "<email>" as "email" and "<incorrectPassword>" as "password" as a request body
    When I send POST request to "/login" endpoint
    Then status code should be 404
    And Response Content type is "application/json"
    And the field value for "error" path should be equal to "Sorry, Wrong Email or Password"
    Examples:
      | email                | incorrectPassword |
      | librar10@library     | libraryUser       |
      | student50000@library | libraryUser       |
