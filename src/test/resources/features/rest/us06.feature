@smoke
Feature: As a librarian, I want to retrieve all book categories

  @us06 @db @ui
  Scenario: Retrieve all book categories from the API endpoint
    Given I logged Library api as a "librarian"
    And Accept header is "application/json"
    And I logged in Library UI as "librarian"
    And I navigate to "Books" page
    When I send GET request to "/get_book_categories" endpoint
    Then status code should be 200
    And Response Content type is "application/json; charset=utf-8"
    And Each "id" field should not be null
    And Each "name" field should not be null
    And UI, Database and API book categories information must match

