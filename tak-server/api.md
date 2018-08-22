Tak API
=======

The following is a breakdown of the API spec of the tak server.

Common Responses
----------------

There are a few failure responses that are common to may calls.
Rather than copy them, they are detailed below.

#### Malformed Fields

| Part        | Value                                                                       |
|-------------|-----------------------------------------------------------------------------|
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

Responses:

Malformed fields

| Part        | Value                                                                       |
|-------------|-----------------------------------------------------------------------------|
| Description | Success, user created.                                                      |
| Return code | 204                                                                         |
| Return body | Blank                                                                       |

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

Responses:

Malformed fields, invalid credentials

| Part        | Value                                                                       |
|-------------|-----------------------------------------------------------------------------|
| Description | Success, pasword changed.                                                   |
| Return code | 204                                                                         |
| Return body | Blank                                                                       |

Friend Actions
--------------

#### Friend Request

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Request a new friend.                                                      |
| Request Type | POST                                                                       |
| Request Body | User credentials, username to request                                      |

Responses:

Malformed fields, invalid credentials, blocked

| Part        | Value                                                                       |
|-------------|-----------------------------------------------------------------------------|
| Description | Success, friend request sent.                                               |
| Return code | 204                                                                         |
| Return body | Blank                                                                       |

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

Responses:

Malformed fields, invalid credentials

| Part        | Value                                                                       |
|-------------|-----------------------------------------------------------------------------|
| Description | Success, friend response sent.                                              |
| Return code | 204                                                                         |
| Return body | Blank                                                                       |

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

#### Unblock User

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Unblock a user.                                                            |
| Request Type | POST                                                                       |
| Request Body | User credentials, username to unblock                                      |

#### List Friends

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | List all friends.                                                          |
| Request Type | GET                                                                        |
| Request Body | User credentials                                                           |

#### List Blocked

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | List all blocked users.                                                    |
| Request Type | GET                                                                        |
| Request Body | User credentials                                                           |

#### Send Message

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Send a message to a user.                                                  |
| Request Type | POST                                                                       |
| Request Body | User credentials, recipient username, message                              |

#### Read Messages

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | View all messages based on parameters.                                     |
| Request Type | GET                                                                        |
| Request Body | User credentials, from(optional), from time(optional), read(optional)      |

Game Actions
------------

#### Request Game

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Request game with a friend.                                                |
| Request Type | POST                                                                       |
| Request Body | User credentials, other user, board size, your color, first player         |

#### Random Game

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Request a random game.                                                     |
| Request Type | POST                                                                       |
| Request Body | User credentials, board size                                               |

#### List Completed Games

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Get a list of all game IDs for completed games.                            |
| Request Type | GET                                                                        |
| Request Body | User credentials                                                           |

#### Get Game

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Get log of a game from the game ID.                                        |
| Request Type | GET                                                                        |
| Request Body | User credentials, game ID                                                  |

#### Play Turn

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Play a turn on a given game.                                               |
| Request Type | POST                                                                       |
| Request Body | User credentials, game ID, turn                                            |

Notifications
-------------

#### Get Incoming

| Part         | Value                                                                      |
|--------------|----------------------------------------------------------------------------|
| Description  | Get incoming friend requests, messages, game invitations, and open games   |
| Request Type | GET                                                                        |
| Request Body | User credentials                                                           |
