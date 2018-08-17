Tak Server
==========

#### NOTE: NOT AT FULL FUNCTIONALITY YET

The tak server is a RESTful api that allows people to play each other online.
It can keep track of friends and allow them to play games against each other.
It can also set up random games against random players.

The API is as follows:


---------------------------------------------------------------------------------------------
| Title           | Description                                                             |
---------------------------------------------------------------------------------------------
| Register        | create a new user                                                       |
| Friend request  | request new friend                                                      |
| Friend response | accept or deny friend request                                           |
| Block user      | block everything from user                                              |
| Unblock user    | unblock user from everything                                            |
| List friends    | list all friends                                                        |
| List blocked    | list all blocked users                                                  |
| Request game    | request game with friend                                                |
| Random game     | try starting a game with a random person                                |
| List all games  | get list of all completed games                                         |
| Get game        | get state of a game                                                     |
| Play turn       | play turn on a game                                                     |
| Send message    | send a message to a user                                                |
| Get incoming    | get friend requests, messages, game invitations, and open games         |
---------------------------------------------------------------------------------------------

Internally, there is a database with the following structure:

---------------------------------------------------------------------
| Table     | Description                                           |
---------------------------------------------------------------------
| Users     | id, username, auth                                    |
| Friends   | pairs of users                                        |
| Requests  | open friend requests                                  |
| Random    | list of users looking for a random game               |
| Blocked   | denied friend requests and blocked users              |
| Games     | id, white player, black player, is finished           |
| Turns     | game id, order                                        |
| Messages  | from, to, message, opened                             |
---------------------------------------------------------------------
