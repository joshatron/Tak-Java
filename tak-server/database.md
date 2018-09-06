Database Structure
==================

The following is a description of the tables in the database.

### Accounts

#### Users

| Name      | Data Type | Parameters                | Description                               |
|-----------|-----------|---------------------------|-------------------------------------------|
| id        | int       | PRIMARY KEY AUTOINCREMENT | ID of the user.                           |
| username  | string    | UNIQUE NOT NULL           | Username of the user.                     |
| auth      | string    | NOT NULL                  | Password of the user.                     |

### Social

#### Friends

| Name      | Data Type | Parameters                | Description                               |
|-----------|-----------|---------------------------|-------------------------------------------|
| requester | int       | PRIMARY KEY               | ID of the person who requested it.        |
| acceptor  | int       | PRIMARY KEY               | ID of the person who accepted it.         |

#### Blocked

| Name      | Data Type | Parameters                | Description                               |
|-----------|-----------|---------------------------|-------------------------------------------|
| requester | int       | PRIMARY KEY               | ID of the person who requested it.        |
| blocked   | int       | PRIMARY KEY               | ID of the person who is blocked.          |

#### Friend\_Requests

| Name      | Data Type | Parameters                | Description                               |
|-----------|-----------|---------------------------|-------------------------------------------|
| requester | int       | PRIMARY KEY               | ID of the person who requested it.        |
| acceptor  | int       | PRIMARY KEY               | ID of the person who needs to accept.     |

#### Messages

| Name      | Data Type | Parameters                | Description                               |
|-----------|-----------|---------------------------|-------------------------------------------|
| id        | int       | PRIMARY KEY AUTOINCREMENT | Message ID.                               |
| from      | int       | NOT NULL                  | ID of the sender.                         |
| to        | int       | NOT NULL                  | ID of the recipient.                      |
| message   | string    | NOT NULL                  | Message body.                             |
| time      | string    | NOT NULL                  | Timestamp when it was sent.               |
| opened    | int       | NOT NULL                  | 0 for unopened, 1 for opened.             |

### Games

#### Random\_Requests

| Name      | Data Type | Parameters                | Description                               |
|-----------|-----------|---------------------------|-------------------------------------------|
| requester | int       | PRIMARY KEY               | ID of the person who requested it.        |
| size      | int       | NOT NULL                  | Size board to play on.                    |

#### Game\_Requests

| Name      | Data Type | Parameters                | Description                               |
|-----------|-----------|---------------------------|-------------------------------------------|
| requester | int       | PRIMARY KEY               | ID of the requester.                      |
| acceptor  | int       | PRIMARY KEY               | ID of the user to accept.                 |
| size      | int       | NOT NULL                  | Size of the board.                        |
| white     | int       | NOT NULL                  | 0 if the requester is black, 1 for white. |
| first     | int       | NOT NULL                  | 0 if the requester is last, 1 for first.  |

#### Games

| Name      | Data Type | Parameters                | Description                               |
|-----------|-----------|---------------------------|-------------------------------------------|
| id        | int       | PRIMARY KEY AUTOINCREMENT | Game ID.                                  |
| white     | int       | NOT NULL                  | User ID of the white player.              |
| black     | int       | NOT NULL                  | User ID of the black player.              |
| size      | int       | NOT NULL                  | Size of the board.                        |
| first     | int       | NOT NULL                  | 0 if white is first, 1 for black.         |
| done      | int       | NOT NULL                  | 0 if not done yet, 1 if finished.         |

#### Turns

| Name      | Data Type | Parameters                | Description                               |
|-----------|-----------|---------------------------|-------------------------------------------|
| id        | int       | PRIMARY KEY               | Game ID.                                  |
| order     | int       | PRIMARY KEY               | Which turn in the game, starting at 1.    |
| turn      | string    | NOT NULL                  | Turn made.                                |
