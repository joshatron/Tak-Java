Tak Server
==========

#### NOTE: NOT AT FULL FUNCTIONALITY YET

The tak server is a RESTful api that allows people to play each other online.
It can keep track of friends and allow them to play games against each other.
It can also set up random games against random players.

The API is described [here](api.md).
A summary of the methods are described below:

| Call              | Description                                           |
|-------------------|-------------------------------------------------------|
| Register          | Register a new user.                                  |
| Change password   | Change your password.                                 |
| Friend request    | Send friend request.                                  |
| Friend response   | Respond to friend request.                            |
| Incoming requests | View all incoming friend requests.                    |
| Outgoing requests | See all your outgoing friend requests.                |
| Block user        | Block a user.                                         |
| Unblock user      | Unblock a user.                                       |
| List friends      | List all friends.                                     |
| List blocked      | List all blocked users.                               |
| Send message      | Send a message to another user.                       |
| Read messages     | Read your message with parameters.                    |
| Request game      | Request a game with a friend.                         |
| Respond to game   | Respond to a game request.                            |
| Incoming games    | View all incoming game invitations.                   |
| Outgoing games    | View all outgoing game invitations.                   |
| Random game       | Start a random game.                                  |
| List completed    | List all completed games.                             |
| List incomplete   | List all incomplete games.                            |
| Get game          | Get the state of a game.                              |
| Play turn         | Make a move in a game.                                |

Internally, there is a database which is defined [here](databse.md).
Below is a summary of the tables:

| Table             | Description                                           |
|-------------------|-------------------------------------------------------|
| Users             | Information about each user.                          |
| Friends           | Pairs of users who are friends.                       |
| Blocked           | Blocked users.                                        |
| Friend Requests   | Open friend requests.                                 |
| Messages          | Messages sent between users.                          |
| Random Requests   | Users looking for a random game.                      |
| Game Requests     | Users challenging other users to a game.              |
| Games             | Basic information about each game.                    |
| Turns             | The turns made in every game.                         |
