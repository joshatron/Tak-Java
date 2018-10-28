package io.joshatron.tak.server.database;

import io.joshatron.tak.server.exceptions.GameServerException;
import io.joshatron.tak.server.request.Auth;
import io.joshatron.tak.server.response.User;

public interface AccountDAO {

    boolean isAuthenticated(Auth auth) throws GameServerException;
    void addUser(Auth auth, int idLength) throws GameServerException;
    void updatePassword(String username, String password) throws GameServerException;
    void updateUsername(String oldUsername, String newUsername) throws GameServerException;
    boolean userExists(String username) throws GameServerException;
    User getUserFromId(String id) throws GameServerException;
    User getUserFromUsername(String username) throws GameServerException;
}
