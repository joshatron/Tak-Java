package io.joshatron.tak.server.database;

import io.joshatron.tak.server.request.Auth;
import io.joshatron.tak.server.request.PassChange;

import java.sql.SQLException;

public interface AccountDAO {

    boolean isAuthenticated(Auth auth) throws SQLException;
    boolean registerUser(Auth auth) throws SQLException;
    boolean updatePassword(PassChange change) throws SQLException;
}
