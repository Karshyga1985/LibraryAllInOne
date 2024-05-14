@smoke
Feature: As a student, I want to return borrowed book

  @us12
  Scenario: Verify that the student can return a book
    Given I logged Library api as a "student"
    And Accept header is "application/x-www-form-urlencoded"
    And I provide borrow_id "15" as a request body
    When I send POST request to "/return_book" endpoint
    Then status code should be 200
    And Response Content type is "application/json; charset=utf-8"
    And the field value for "message" path should be equal to "The book has been returned.."