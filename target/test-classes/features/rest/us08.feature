
Feature: As a librarian, I want to update a book information

  @us08
  Scenario Outline: Update a book API
    Given I logged Library api as a "librarian"
    And Accept header is "application/json"
    And Request Content Type header is "application/json"
    And I provide "<book_id>" and update "<book_name>" as a request body
    When I send PATCH request to "/update_book" endpoint
    Then status code should be 200
    And Response Content type is "application/json; charset=utf-8"
    And the field value for "message" path should be equal to "The book has been updated."
    Examples:
      | book_id | book_name             |
      | 23590   | Kara Book UPDATE      |
      | 23591   | Head First Java       |
      | 23592   | The Scrum Field Guide |