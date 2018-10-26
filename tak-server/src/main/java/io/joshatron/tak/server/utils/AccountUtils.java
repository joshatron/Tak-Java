package io.joshatron.tak.server.utils;

import io.joshatron.tak.server.database.AccountDAO;
import io.joshatron.tak.server.request.Auth;
import io.joshatron.tak.server.request.UserChange;
import io.joshatron.tak.server.response.User;

public class AccountUtils {

    private AccountDAO accountDAO;

    public boolean isAuthenticated(Auth auth) throws Exception {
        return false;
    }

    public void registerUser(Auth auth) throws Exception {

    }

    public void updatePassword(UserChange change) throws Exception {

    }

    public void updateUsername(UserChange change) throws Exception {

    }

    public boolean userExists(String username) throws Exception {
        return false;
    }

    public User getUserFromId(String id) throws Exception {
        return null;
    }

    public User getUserFromUsername(String username) throws Exception {
        return null;
    }
}
