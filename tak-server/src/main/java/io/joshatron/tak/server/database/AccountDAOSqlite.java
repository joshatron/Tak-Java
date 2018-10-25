package io.joshatron.tak.server.database;

import io.joshatron.tak.server.exceptions.BadRequestException;
import io.joshatron.tak.server.exceptions.ForbiddenException;
import io.joshatron.tak.server.exceptions.NoAuthException;
import io.joshatron.tak.server.exceptions.ResourceNotFoundException;
import io.joshatron.tak.server.request.Auth;
import io.joshatron.tak.server.request.PassChange;
import io.joshatron.tak.server.response.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAOSqlite implements AccountDAO {

    private Connection conn;

    public AccountDAOSqlite() {
        conn = DatabaseManager.getConnection();
    }

    public AccountDAOSqlite(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean isAuthenticated(Auth auth) throws SQLException, BadRequestException {
        if(auth == null || auth.getUsername() == null || auth.getPassword() == null) {
            throw new BadRequestException("The authorization is in an invalid format.");
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;

        String getAuth = "SELECT username, auth " +
                "FROM users " +
                "WHERE username = ?;";

        try {
            stmt = conn.prepareStatement(getAuth);
            stmt.setString(1, auth.getUsername());
            rs = stmt.executeQuery();

            if (rs.next()) {
                if (auth.getUsername().equals(rs.getString("username")) &&
                        BCrypt.checkpw(auth.getPassword(), rs.getString("auth"))) {
                    return true;
                }
            }

            return false;
        }
        finally {
            if(stmt != null) {
                stmt.close();
            }
            if(rs != null) {
                rs.close();
            }
        }
    }

    @Override
    public void registerUser(Auth auth) throws SQLException, ForbiddenException, BadRequestException {
        if(auth == null) {
            throw new BadRequestException();
        }
        if(auth.getUsername() == null || auth.getUsername().length() == 0) {
            throw new BadRequestException("The username is blank or missing.");
        }
        if(auth.getPassword() == null || auth.getPassword().length() == 0) {
            throw new BadRequestException("The password is blank or missing.");
        }

        if(auth.getUsername().contains(":")) {
            throw new BadRequestException("The username cannot contain a ':'.");
        }


        PreparedStatement stmt = null;
        ResultSet rs = null;

        //make sure username isn't taken, ignoring the case
        String checkUsername = "SELECT username " +
                "FROM users " +
                "WHERE username = ? " +
                "COLLATE NOCASE;";

        //insert new user if it isn't
        String insertUser = "INSERT INTO  users (username, auth) " +
                "VALUES (?,?);";

        try {
            stmt = conn.prepareStatement(checkUsername);
            stmt.setString(1, auth.getUsername());
            rs = stmt.executeQuery();

            if (rs.next()) {
                throw new ForbiddenException("That username is already taken.");
            }
            stmt.close();

            stmt = conn.prepareStatement(insertUser);
            stmt.setString(1, auth.getUsername());
            stmt.setString(2, BCrypt.hashpw(auth.getPassword(), BCrypt.gensalt()));
            stmt.executeUpdate();
        } finally {
            if(stmt != null) {
                stmt.close();
            }
            if(rs != null) {
                rs.close();
            }
        }
    }

    @Override
    public void updatePassword(PassChange change) throws SQLException, NoAuthException, BadRequestException {
        if(change.getUpdated() == null || change.getUpdated().length() == 0) {
            throw new BadRequestException("The new password is blank or missing.");
        }
        if(!isAuthenticated(change.getAuth())) {
            throw new NoAuthException();
        }

        PreparedStatement stmt = null;

        String changePass = "UPDATE users " +
                "SET auth = ? " +
                "WHERE username = ?;";

        try {
            stmt = conn.prepareStatement(changePass);
            stmt.setString(1, BCrypt.hashpw(change.getUpdated(), BCrypt.gensalt()));
            stmt.setString(2, change.getAuth().getUsername());
            stmt.executeUpdate();
        }
        finally {
            if(stmt != null) {
                stmt.close();
            }
        }
    }

    @Override
    public boolean userExists(String username) throws SQLException, BadRequestException {
        if(username == null || username.length() == 0) {
            throw new BadRequestException("The username is blank or missing.");
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;

        String checkUsername = "SELECT username " +
                "FROM users " +
                "WHERE username = ?;";

        try {
            stmt = conn.prepareStatement(checkUsername);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return true;
            }

            return false;
        }
        finally {
            if(stmt != null) {
                stmt.close();
            }
            if(rs != null) {
                rs.close();
            }
        }
    }

    @Override
    public User getUserFromUsername(String username) throws SQLException, BadRequestException, ResourceNotFoundException {
        if(username == null || username.length() == 0) {
            throw new BadRequestException("The username is blank or missing.");
        }

        PreparedStatement selectStmt = null;

        String checkUsername = "SELECT id " +
                "FROM users " +
                "WHERE username = ?;";

        try {
            selectStmt = conn.prepareStatement(checkUsername);
            selectStmt.setString(1, username);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                return new User(username, rs.getString("id"));
            }
            else {
                throw new ResourceNotFoundException("That user could not be found.");
            }
        }
        finally {
            if(selectStmt != null) {
                selectStmt.close();
            }
        }
    }

    @Override
    public User getUserFromId(String id) throws SQLException, ResourceNotFoundException {
        PreparedStatement selectStmt = null;

        String checkUsername = "SELECT username " +
                "FROM users " +
                "WHERE id = ?;";

        try {
            selectStmt = conn.prepareStatement(checkUsername);
            selectStmt.setString(1, id);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getString("username"), id);
            }
            else {
                throw new ResourceNotFoundException("That user could not be found.");
            }
        }
        finally {
            if(selectStmt != null) {
                selectStmt.close();
            }
        }
    }

    public Connection getConnection() {
        return conn;
    }
}
