package io.joshatron.tak.server.database;

import io.joshatron.tak.server.request.Auth;
import io.joshatron.tak.server.request.PassChange;

public interface AccountDAO {

    boolean isAuthenticated(Auth auth);
    boolean registerUser(Auth auth);
    boolean updatePassword(PassChange change);
}
