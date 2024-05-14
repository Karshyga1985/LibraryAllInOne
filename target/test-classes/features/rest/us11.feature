@smoke
Feature: As a librarian, I want to retrieve all books available for borrowing

  @us11
  Scenario: Retrieve all books from the API endpoint
    Given I logged Library api as a "librarian"
    And Accept header is "application/json"
    When I send GET request to "/get_book_list_for_borrowing" endpoint
    Then status code should be 200
    And Response Content type is "application/json; charset=utf-8"
    And Each "id" field should not be null
