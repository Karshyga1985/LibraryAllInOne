@smoke
Feature: As a user, I want to delete a book (negative testing)

  @us09
  Scenario Outline: Verify that not admin users can not delete book
    Given I logged Library api as a "<userType>"
    And Accept header is "application/json"
    And Path param "id" is "<book_id>"
    When I send DELETE request to "/delete_book/{id}" endpoint
    Then status code should be 403
    And Response Content type is "application/json; charset=utf-8"
    And the field value for "error" path should be equal to "Unauthorized Access"
    Examples:
      | userType  | book_id |
      | librarian | 23596   |
      | student   | 23598   |
