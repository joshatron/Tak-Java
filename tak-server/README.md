Tak Server
==========

#### NOTE: NOT AT FULL FUNCTIONALITY YET

The tak server is a RESTful api that allows people to play each other online.
It can keep track of friends and allow them to play games against each other.
It can also set up random games against random players.

The API is described [here](api.md).

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
