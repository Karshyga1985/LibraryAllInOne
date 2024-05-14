@smoke
Feature: As a librarian, I want to retrieve user groups information

  @us15 @db
  Scenario: Retrieve user groups information from the API endpoint
    Given I logged Library api as a "librarian"
    And Accept header is "application/json"
    When I send GET request to "/get_user_groups" endpoint
    Then status code should be 200
    And Response Content type is "application/json; charset=utf-8"
    And Each "id" field should not be null
    And Each "name" field should not be null
    And user groups API response should match with Database

    #this us contains bug where API response doesn't contain admin info, and DB has a admin info