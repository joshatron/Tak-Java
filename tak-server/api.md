Tak API
=======

The following is a breakdown of the API spec of the tak server.

Common Responses
----------------

There are a few failure responses that are common to may calls.
Rather than copy them, they are detailed below.

#### Malformed Fields

| Description | This response is given if any fields are blank or missing.                  |
| Return code | 400                                                                         |
| Return body | Array of the bad fields.                                                    |

ex: if the user and turn fields were malformed, the response body would be:

    {
        [
            "user",
            "turn"
        ]
    }

#### Invalid Credentials

| Description | This response is given if the credentials given are invalid.                |
| Return code | 401                                                                         |
| Return body | A string containing the reason.                                             |

The response body will look like this:

    {
        "reason": "Invalid credentials"
    }

#### Blocked

| Description | For interactions between users, this is given if one user blocks another.   |
| Return code | 403                                                                         |
| Return body | A string containing the reason.                                             |

The response body will look like this:

    {
        "reason": "Tried to interact with blocked user"
    }

User Actions
------------

#### Register

| Description  | Registers a new user.                                                      |
| Request Type | POST                                                                       |
| Request Body | username, password                                                         |

#### Change Password

| Description  | Changes the password of a user.                                            |
| Request Type | POST                                                                       |
| Request Body | User credentials, new password                                             |

Friend Actions
--------------

#### Friend Request

| Description  | Request a new friend.                                                      |
| Request Type | POST                                                                       |
| Request Body | User credentials, username to request                                      |

#### Friend Response

| Description  | Accept or deny a friend request.                                           |
| Request Type | POST                                                                       |
| Request Body | User credentials, username of requester, accept or deny                    |

#### Block User

| Description  | Block a user.                                                              |
| Request Type | POST                                                                       |
| Request Body | User credentials, username to block                                        |

#### Unblock User

| Description  | Unblock a user.                                                            |
| Request Type | POST                                                                       |
| Request Body | User credentials, username to unblock                                      |

#### List Friends

| Description  | List all friends.                                                          |
| Request Type | GET                                                                        |
| Request Body | User credentials                                                           |

#### List Blocked

| Description  | List all blocked users.                                                    |
| Request Type | GET                                                                        |
| Request Body | User credentials                                                           |

#### Send Message

| Description  | Send a message to a user.                                                  |
| Request Type | POST                                                                       |
| Request Body | User credentials, recipient username, message                              |

#### Read Messages

| Description  | View all messages based on parameters.                                     |
| Request Type | GET                                                                        |
| Request Body | User credentials, from(optional), from time(optional), read(optional)      |

Game Actions
------------

#### Request Game

| Description  | Request game with a friend.                                                |
| Request Type | POST                                                                       |
| Request Body | User credentials, other user, board size, your color, first player         |

#### Random Game

| Description  | Request a random game.                                                     |
| Request Type | POST                                                                       |
| Request Body | User credentials, board size                                               |

#### List Completed Games

| Description  | Get a list of all game IDs for completed games.                            |
| Request Type | GET                                                                        |
| Request Body | User credentials                                                           |

#### Get Game

| Description  | Get log of a game from the game ID.                                        |
| Request Type | GET                                                                        |
| Request Body | User credentials, game ID                                                  |

#### Play Turn

| Description  | Play a turn on a given game.                                               |
| Request Type | POST                                                                       |
| Request Body | User credentials, game ID, turn                                            |

Notifications
-------------

#### Get Incoming

| Description  | Get incoming friend requests, messages, game invitations, and open games   |
| Request Type | GET                                                                        |
| Request Body | User credentials                                                           |
