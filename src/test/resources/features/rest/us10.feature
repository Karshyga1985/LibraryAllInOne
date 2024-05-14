Feature: As a librarian, I want to get list of books borrowed by user

  @us10 @db
  Scenario Outline: Get borrowed books list by user id
    Given I logged Library api as a "librarian"
    And Accept header is "application/json"
    And Path param "user_id" is "<user_id>"
    When I send GET request to "/get_borrowed_books_by_user/{user_id}" endpoint
    Then status code should be 200
    And Response Content type is "application/json; charset=utf-8"
    And API response should match with Database
    Examples:
      | user_id |
      | 13454   |
      | 13456   |
      | 13474   |