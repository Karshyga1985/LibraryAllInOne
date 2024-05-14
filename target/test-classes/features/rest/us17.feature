
Feature: As a librarian, I want to update a user information

  @us17
  Scenario Outline: Update user API
    Given I logged Library api as a "librarian"
    And Accept header is "application/json"
    And Request Content Type header is "application/x-www-form-urlencoded"
    And I update one of the following fields "<full_name>", "<email>", "<password>", "<user_group_id>", "<status>", "<start_date>", "<end_date>", "<address>", "<id>",  the user information as request body
    When I send PATCH request to "/update_user" endpoint
    Then status code should be 200
    And Response Content type is "application/json; charset=utf-8"
    And the field value for "message" path should be equal to "The user has been updated."
    Examples:
      | full_name       | email                   | password    | user_group_id | status   | start_date | end_date   | address                                           | id    |
      | Leo Dickens     | nestor.walker@gmail.com | libraryUser | 2             | INACTIVE | 2013-07-14 | 2019-01-14 | 4685 Gutmann Island, Colebury, WA 89254-8889      | 13755 |
      | Mr. John Ernser | krysta.hauck@yahoo.com  | libraryUser | 3             | ACTIVE   | 2020-05-11 | 2025-08-10 | 75350 Norbert Turnpike, Olsonhaven, VT 53754-6483 | 13759 |
      | Valentine Koss  | vanda.orn@gmail.com     | libraryUser | 2             | ACTIVE   | 2021-02-16 | 2026-06-17 | Apt. 253 98246 Alvin Ports, Rempelburgh, MI 32156 | 13760 |

    #API request for this function doesn't work as expected. This test scenario was failed in POSTMAN, SWAGGER and REST-ASSURED automation
  #worked on it just for practice