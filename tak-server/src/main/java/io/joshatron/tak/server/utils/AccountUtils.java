package io.joshatron.tak.server.utils;

import io.joshatron.tak.server.database.AccountDAO;
import io.joshatron.tak.server.exceptions.BadRequestException;
import io.joshatron.tak.server.request.Auth;
import io.joshatron.tak.server.request.UserChange;
import io.joshatron.tak.server.response.User;

public class AccountUtils {

    private AccountDAO accountDAO;

    public AccountUtils(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public boolean isAuthenticated(Auth auth) throws Exception {
        validateAuth(auth);
        return accountDAO.isAuthenticated(auth);
    }

    public void registerUser(Auth auth) throws Exception {
        validateAuth(auth);
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

    private void validateAuth(Auth auth) throws BadRequestException {
        if(auth == null || auth.getUsername() == null || auth.getUsername().length() == 0 ||
                auth.getPassword() == null || auth.getPassword().length() == 0) {
            throw new BadRequestException("The authorization is in an invalid format.");
        }
    }
}
