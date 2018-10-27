package io.joshatron.tak.server.utils;

import io.joshatron.tak.server.database.AccountDAO;
import io.joshatron.tak.server.exceptions.ForbiddenException;
import io.joshatron.tak.server.exceptions.NoAuthException;
import io.joshatron.tak.server.request.Auth;
import io.joshatron.tak.server.request.UserChange;
import io.joshatron.tak.server.response.User;
import io.joshatron.tak.server.validation.AccountValidator;

public class AccountUtils {

    public static final int ID_LENGTH = 15;

    private AccountDAO accountDAO;

    public AccountUtils(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public boolean isAuthenticated(Auth auth) throws Exception {
        AccountValidator.validateAuth(auth);

        return accountDAO.isAuthenticated(auth);
    }

    public void registerUser(Auth auth) throws Exception {
        AccountValidator.validateAuth(auth);
        if(accountDAO.userExists(auth.getUsername())) {
            throw new ForbiddenException("That username is already taken.");
        }

        accountDAO.addUser(auth);
    }

    public void updatePassword(UserChange change) throws Exception {
        AccountValidator.validatePassChange(change);
        if(!accountDAO.isAuthenticated(change.getAuth())) {
            throw new NoAuthException();
        }

        accountDAO.updatePassword(change.getAuth().getUsername(), change.getUpdated());
    }

    public void updateUsername(UserChange change) throws Exception {
        AccountValidator.validateUserChange(change);
        if(!accountDAO.isAuthenticated(change.getAuth())) {
            throw new NoAuthException();
        }

        accountDAO.updateUsername(change.getAuth().getUsername(), change.getUpdated());
    }

    public boolean userExists(String username) throws Exception {
        AccountValidator.validateUsername(username);

        return accountDAO.userExists(username);
    }

    public User getUserFromId(String id) throws Exception {
        AccountValidator.validateUserId(id);

        return accountDAO.getUserFromId(id);
    }

    public User getUserFromUsername(String username) throws Exception {
        AccountValidator.validateUsername(username);

        return accountDAO.getUserFromUsername(username);
    }
}
