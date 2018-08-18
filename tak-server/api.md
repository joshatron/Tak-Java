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

| Description |                                                                             |
| Return code |                                                                             |
| Return body |                                                                             |

#### Change Password

| Description |                                                                             |
| Return code |                                                                             |
| Return body |                                                                             |

Friend Actions
--------------

#### Friend Request

| Description |                                                                             |
| Return code |                                                                             |
| Return body |                                                                             |

#### Friend Response

| Description |                                                                             |
| Return code |                                                                             |
| Return body |                                                                             |

#### Block User

| Description |                                                                             |
| Return code |                                                                             |
| Return body |                                                                             |

#### Unblock User

| Description |                                                                             |
| Return code |                                                                             |
| Return body |                                                                             |

#### List Friends

| Description |                                                                             |
| Return code |                                                                             |
| Return body |                                                                             |

#### List Blocked

| Description |                                                                             |
| Return code |                                                                             |
| Return body |                                                                             |

#### Send Message

| Description |                                                                             |
| Return code |                                                                             |
| Return body |                                                                             |

#### Read Messages

| Description |                                                                             |
| Return code |                                                                             |
| Return body |                                                                             |

Game Actions
------------

#### Request Game

| Description |                                                                             |
| Return code |                                                                             |
| Return body |                                                                             |

#### Random Game

| Description |                                                                             |
| Return code |                                                                             |
| Return body |                                                                             |

#### List Completed Games

| Description |                                                                             |
| Return code |                                                                             |
| Return body |                                                                             |

#### Get Game

| Description |                                                                             |
| Return code |                                                                             |
| Return body |                                                                             |

#### Play Turn

| Description |                                                                             |
| Return code |                                                                             |
| Return body |                                                                             |

Notifications
-------------

#### Get Incoming

| Description |                                                                             |
| Return code |                                                                             |
| Return body |                                                                             |
