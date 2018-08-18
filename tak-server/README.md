Tak Server
==========

#### NOTE: NOT AT FULL FUNCTIONALITY YET

The tak server is a RESTful api that allows people to play each other online.
It can keep track of friends and allow them to play games against each other.
It can also set up random games against random players.

The API is as follows (a more descriptive version can be found [here](api.md)):

| Title           | Description                                                             |
|-----------------|-------------------------------------------------------------------------|
| Register        | Create a new user                                                       |
| Change password | Change password for a user                                              |
| Friend request  | Request new friend                                                      |
| Friend response | Accept or deny friend request                                           |
| Block user      | Block everything from user                                              |
| Unblock user    | Unblock user from everything                                            |
| List friends    | List all friends                                                        |
| List blocked    | List all blocked users                                                  |
| Request game    | Request game with friend                                                |
| Random game     | Try starting a game with a random person                                |
| List all games  | Get list of all completed games                                         |
| Get game        | Get state of a game                                                     |
| Play turn       | Play turn on a game                                                     |
| Send message    | Send a message to a user                                                |
| Read messages   | Read all messages with parameters                                       |
| Get incoming    | Get friend requests, messages, game invitations, and open games         |

Internally, there is a database with the following structure:

| Table     | Description                                           |
|-----------|-------------------------------------------------------|
| Users     | id, username, auth                                    |
| Friends   | pairs of users                                        |
| Requests  | open friend requests                                  |
| Random    | list of users looking for a random game               |
| Blocked   | denied friend requests and blocked users              |
| Games     | id, white player, black player, is finished           |
| Turns     | game id, order                                        |
| Messages  | from, to, message, opened                             |
