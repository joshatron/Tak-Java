package io.joshatron.tak.server.database;

import io.joshatron.tak.server.request.Auth;
import io.joshatron.tak.server.request.UserChange;
import io.joshatron.tak.server.response.User;

public interface AccountDAO {

    boolean isAuthenticated(Auth auth) throws Exception;
    void addUser(Auth auth) throws Exception;
    void updatePassword(UserChange change) throws Exception;
    void updateUsername(UserChange change) throws Exception;
    boolean userExists(String username) throws Exception;
    User getUserFromId(String id) throws Exception;
    User getUserFromUsername(String username) throws Exception;
}
