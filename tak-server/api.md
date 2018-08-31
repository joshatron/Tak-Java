Tak API
=======

The following is a breakdown of the API spec of the tak server.

Common Parts
------------

### Requests

The following are common aspects of requests

#### User Credentials

Several calls use user credentials, an example of which is shown below.
Future versions will have a better setup.

    {
        "auth": {
            "username": "joshatron",
            "password": "password"
        }
    }

### Responses

There are a few responses that are common to may calls.
Rather than copy them, they are detailed below.

#### Sucess

| Part        | Value                                                                       |
|-------------|-----------------------------------------------------------------------------|
| Description | Success, no response body needed.                                           |
| Return code | 204                                                                         |
| Return body | Blank                                                                       |

#### Malformed Fields

| Part        | Value                                                                       |
|-------------|-----------------------------------------------------------------------------|
| Description | This response is given if any fields are blank, missing, or invalid.        |
| Return code | 400                                                                         |
| Return body | Array of the bad fields.                                                    |

ex: if the user was invalid and the turn field was malformed, the response body would be:

    {
        [
            "user",
            "turn"
        ]
    }

#### Invalid Credentials

| Part        | Value                                                                       |
|-------------|-----------------------------------------------------------------------------|
| Description | This response is given if the credentials given are invalid.                |
| Return code | 401                                                                         |
| Return body | A string containing the reason.                                             |

The response body will look like this:

    {
        "reason": "Invalid credentials"
    }

#### Blocked

| Part        | Value                                                                       |
|-------------|-----------------------------------------------------------------------------|
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

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Registers a new user.                                                      |
| Request Type | POST                                                                       |
| Request Body | username, password                                                         |

ex:

    {
        "username": "joshatron",
        "password": "password"
    }

Responses:

Malformed fields, sucess

| Part        | Value                                                                       |
|-------------|-----------------------------------------------------------------------------|
| Description | Failure, username in use.                                                   |
| Return code | 403                                                                         |
| Return body | A string containing the reason.                                             |

The response body for the username in use failure would look like this:

    {
        "reason": "Username is already registered."
    }

#### Change Password

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Changes the password of a user.                                            |
| Request Type | POST                                                                       |
| Request Body | User credentials, new password                                             |

ex:

    {
        "auth": {
            "username": "joshatron",
            "password": "password"
        },
        "password": "new-password"
    }

Responses:

Malformed fields, invalid credentials, success

Friend Actions
--------------

#### Friend Request

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Request a new friend.                                                      |
| Request Type | POST                                                                       |
| Request Body | User credentials, username to request                                      |

ex:

    {
        "auth": {
            "username": "joshatron",
            "password": "password"
        },
        "friend": "melissa"
    }

Responses:

Malformed fields, invalid credentials, blocked, sucess

| Part        | Value                                                                       |
|-------------|-----------------------------------------------------------------------------|
| Description | Failure, already a friend.                                                  |
| Return code | 403                                                                         |
| Return body | A string containing the reason.                                             |

The response body for the already a friend failure would look like this:

    {
        "reason": "You are already friends with that user."
    }

#### Friend Response

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Accept or deny a friend request.                                           |
| Request Type | POST                                                                       |
| Request Body | User credentials, username of requester, accept or deny                    |

ex:

    {
        "auth": {
            "username": "melissa",
            "password": "password"
        },
        "friend": "joshatron",
        "response": "accept"
    }

Responses:

Malformed fields, invalid credentials, sucess

| Part        | Value                                                                       |
|-------------|-----------------------------------------------------------------------------|
| Description | Failure, not a current friend request.                                      |
| Return code | 403                                                                         |
| Return body | A string containing the reason.                                             |

The response body for the not a current friend request failure would look like this:

    {
        "reason": "There is no friend request from that user."
    }

#### Block User

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Block a user.                                                              |
| Request Type | POST                                                                       |
| Request Body | User credentials, username to block                                        |

ex:

    {
        "auth": {
            "username": "joshatron",
            "password": "password"
        },
        "block": "brian"
    }

Responses:

Malformed fields, invalid credentials, sucess

| Part        | Value                                                                       |
|-------------|-----------------------------------------------------------------------------|
| Description | Failure, user has already been blocked.                                     |
| Return code | 403                                                                         |
| Return body | A string containing the reason.                                             |

The response body for the not a current friend request failure would look like this:

    {
        "reason": "You have already blocked this user."
    }

#### Unblock User

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Unblock a user.                                                            |
| Request Type | POST                                                                       |
| Request Body | User credentials, username to unblock                                      |

ex:

    {
        "auth": {
            "username": "joshatron",
            "password": "password"
        },
        "unblock": "brian"
    }

Responses:

Malformed fields, invalid credentials, success

| Part        | Value                                                                       |
|-------------|-----------------------------------------------------------------------------|
| Description | Failure, user is not currently blocked.                                     |
| Return code | 403                                                                         |
| Return body | A string containing the reason.                                             |

The response body for the not a current friend request failure would look like this:

    {
        "reason": "The user is not currently blocked."
    }

#### List Friends

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | List all friends.                                                          |
| Request Type | GET                                                                        |
| Request Body | User credentials                                                           |

ex:

    {
        "auth": {
            "username": "joshatron",
            "password": "password"
        }
    }

Responses:

Malformed fields, invalid credentials

| Part        | Value                                                                       |
|-------------|-----------------------------------------------------------------------------|
| Description | Success, usernames listed.                                                  |
| Return code | 200                                                                         |
| Return body | Array of all friends.                                                       |

The response body for the success case would look like this:

    {
        ["Joshatron", "Fred", "Mary"]
    }

#### List Blocked

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | List all blocked users.                                                    |
| Request Type | GET                                                                        |
| Request Body | User credentials                                                           |

ex:

    {
        "auth": {
            "username": "joshatron",
            "password": "password"
        }
    }

Responses:

Malformed fields, invalid credentials

| Part        | Value                                                                       |
|-------------|-----------------------------------------------------------------------------|
| Description | Success, blocked users listed.                                              |
| Return code | 200                                                                         |
| Return body | Array of all blocked users.                                                 |

The response body for the success case would look like this:

    {
        ["George", "Marissa"]
    }

#### Send Message

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Send a message to a user.                                                  |
| Request Type | POST                                                                       |
| Request Body | User credentials, recipient username, message                              |

ex:

    {
        "auth": {
            "username": "joshatron",
            "password": "password"
        },
        "recipient": "melissa",
        "message": "hi"
    }

Responses:

Malformed fields, invalid credentials, blocked, success

#### Read Messages

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | View all messages based on parameters.                                     |
| Request Type | GET                                                                        |
| Request Body | User credentials, from(optional), from time(optional), read(optional)      |

ex:

    {
        "auth": {
            "username": "joshatron",
            "password": "password"
        },
        "senders": ["melissa", "taryn"],
        "start": "06/06/2018 10:00:56",
        "read": "true"
    }

Responses:

Malformed fields, invalid credentials

| Part        | Value                                                                       |
|-------------|-----------------------------------------------------------------------------|
| Description | Success, messages listed.                                                   |
| Return code | 200                                                                         |
| Return body | List of the messages.                                                       |

The response body for the success case would look like this:

    {
        [
            {
                "user": "taryn",
                "timestamp": "08/10/18 10:58:16",
                "message": "Hello!"
            },
            {
                "user": "melissa",
                "timestamp": "09/01/18 16:03:05",
                "message": "It's your turn"
            }
        ]
    }

Game Actions
------------

#### Request Game

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Request game with a friend.                                                |
| Request Type | POST                                                                       |
| Request Body | User credentials, other user, board size, your color, first player         |

ex:

    {
        "auth": {
            "username": "joshatron",
            "password": "password"
        },
        "opponent": "melissa",
        "size": 5,
        "color": "white",
        "first": "true"
    }

Responses:

Malformed fields, invalid credentials, blocked, success

#### Respond to Game

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Respond to a request for a game.                                           |
| Request Type | POST                                                                       |
| Request Body | User credentials, game id, accept or deny                                  |

ex:

    {
        "auth": {
            "username": "melissa",
            "password": "password"
        },
        "game_id": "12345",
        "response": "deny"
    }

Responses:

Malformed fields, invalid credentials, success

#### Random Game

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Request a random game.                                                     |
| Request Type | POST                                                                       |
| Request Body | User credentials, board size                                               |

ex:

    {
        "auth": {
            "username": "joshatron",
            "password": "password"
        },
        "size": 3
    }

#### List Completed Games

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Get a list of all game IDs for completed games.                            |
| Request Type | GET                                                                        |
| Request Body | User credentials                                                           |

ex:

    {
        "auth": {
            "username": "joshatron",
            "password": "password"
        }
    }

#### List Incomplete Games

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Get a list of all game IDs for incomplete games.                           |
| Request Type | GET                                                                        |
| Request Body | User credentials                                                           |

ex:

    {
        "auth": {
            "username": "joshatron",
            "password": "password"
        }
    }

#### Get Game

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Get log of a game from the game ID.                                        |
| Request Type | GET                                                                        |
| Request Body | User credentials, game ID                                                  |

ex:

    {
        "auth": {
            "username": "joshatron",
            "password": "password"
        },
        "game_id": "12345"
    }

#### Play Turn

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Play a turn on a given game.                                               |
| Request Type | POST                                                                       |
| Request Body | User credentials, game ID, turn                                            |

ex:

    {
        "auth": {
            "username": "joshatron",
            "password": "password"
        },
        "game_id": "12345",
        "turn": "pc a1"
    }

Notifications
-------------

#### Get Incoming Friend Requests

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Get incoming friend requests                                               |
| Request Type | GET                                                                        |
| Request Body | User credentials                                                           |

ex:

    {
        "auth": {
            "username": "joshatron",
            "password": "password"
        }
    }

#### Get Incoming Game Invitations

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Get incoming game invitations                                              |
| Request Type | GET                                                                        |
| Request Body | User credentials                                                           |

ex:

    {
        "auth": {
            "username": "joshatron",
            "password": "password"
        }
    }
