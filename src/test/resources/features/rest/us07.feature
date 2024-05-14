@smoke
Feature: As a user, I want to search for a specific book I created by its id
  so that I can quickly find the information I need.

  @us07
  Scenario Outline: Retrieve created single book
    Given I logged Library api as a "librarian"
    And Accept header is "application/json"
    And Path param "id" is "<book_id>"
    When I send GET request to "/get_book_by_id/{id}" endpoint
    Then status code should be 200
    And Response Content type is "application/json; charset=utf-8"
    And "<book_id>" field should be same with path param values
    And following fields should not be null
      | name             |
      | isbn             |
      | year             |
      | author           |
      | book_category_id |
    Examples:
      | book_id |
      | 23583   |
      | 23584   |
      | 23585   |

  @us07
  Scenario Outline: Verify that the user gets error message if search a book with invalid id
    Given I logged Library api as a "librarian"
    And Accept header is "application/json"
    And Path param "id" is "<book_id>"
    When I send GET request to "/get_book_by_id/{id}" endpoint
    Then status code should be 200
    And Response Content type is "application/json; charset=utf-8"
    And "<book_id>" field should be same with path param values
    And following fields should not be null
      | name             |
      | isbn             |
      | year             |
      | author           |
      | book_category_id |
    Examples:
      | book_id |
      | 23583   |
      | 23584   |
      | 23585   |
