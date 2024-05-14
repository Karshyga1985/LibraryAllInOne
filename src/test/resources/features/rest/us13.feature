@smoke
Feature: As a student, I want to borrow a book

  @us13 @db
  Scenario Outline: Verify that the student can borrow a book
    Given I logged Library api as a "student"
    And Accept header is "application/x-www-form-urlencoded"
    And I provide "<book_id>" as "book_id" and "<user_id>" as "user_id" as a request body
    When I send POST request to "/book_borrow" endpoint
    Then status code should be 200
    And Response Content type is "application/json; charset=utf-8"
    And the field value for "message" path should be equal to "The book has been borrowed..."
    And "<book_id>" should match with DB record and is_returned should be "0"
    Examples:
      | book_id | user_id |
      | 23599   | 5737    |
      | 26780   | 5735    |
      | 13828   | 13457   |