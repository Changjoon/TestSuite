Feature: Member API

  Scenario Outline: Create a new member with parameterized data
    Given I have a member with name "<name>", city "<city>", street "<street>", and zipcode "<zipcode>"
    When I send a POST request to "<endpoint>" with base URI "<baseURI>"
    Then the response status code should be <statusCode>
    And the response should contain the message "<message>"
    And the latest member should have name "<name>", city "<city>", street "<street>", and zipcode "<zipcode>" with "<baseURI>"

    Examples:
      | name      | city    | street        | zipcode | endpoint   | baseURI               | statusCode | message                      |
      | John Doe  | Seoul   | 123 Main St   | 12345   | /members/member   | http://localhost:8091 | 201        | Member created successfully  |
      | Jane Doe  | Busan   | 456 Market St | 67890   | /members/member   | http://localhost:8092 | 201        | Member created successfully  |