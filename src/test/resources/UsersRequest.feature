Feature: UsersCRUD

  Scenario: As an user I need to verify USERS crud capability

    Given I created and store an user token by sending POST request to "/api/user.json" with body
    """
      {
        "Email": "<email_placeholder>",
        "FullName": "Joe Blow",
        "Password": "pASswoRd"
      }
    """
    When I get the user created
    Then response code should be 200
    When send PUT request "/api/user/$ID_USER.json" with body
    """
    {
       "Email": "<email_placeholder>"
    }
    """
    Then response code should be 200
    When send DELETE request "/api/user/$ID_USER.json" with body
    """
    """
    Then response code should be 200

