Tak Server
==========

#### NOTE: NOT AT FULL FUNCTIONALITY YET

The tak server is a RESTful api that allows people to play each other online.
It can keep track of friends and allow them to play games against each other.
It can also set up random games against random players.

The API is described [here](api.md). A summary of the methods are mentioned below:

| Call              | Description                                           |
|-------------------|-------------------------------------------------------|
| Register          | Register a new user.                                  |
| Change password   | Change your password.                                 |
| Friend request    | Send friend request.                                  |
| Friend response   | Respond to friend request.                            |
| Block user        | Block a user.                                         |
| Unblock user      | Unblock a user.                                       |
| List friends      | List all friends.                                     |
| List blocked      | List all blocked users.                               |
| Send message      | Send a message to another user.                       |
| Read messages     | Read your message with parameters.                    |
| Request game      | Request a game with a friend.                         |
| Respond to game   | Respond to a game request.                            |
| Random game       | Start a random game.                                  |
| List completed    | List all completed games.                             |
| List incomplete   | List all incomplete games.                            |
| Get game          | Get the state of a game.                              |
| Play turn         | Make a move in a game.                                |
| Incoming requests | View all incoming friend requests.                    |
| Incoming games    | View all incoming game invitations.                   |

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
