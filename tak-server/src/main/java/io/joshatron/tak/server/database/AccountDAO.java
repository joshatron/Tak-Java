package io.joshatron.tak.server.database;

import io.joshatron.tak.server.requestbody.Auth;

public interface AccountDAO {

    boolean isAuthenticated(String username, String auth);
    boolean registerUser(String username, String auth);
    boolean updatePassword(Auth auth, String newAuth);
}
