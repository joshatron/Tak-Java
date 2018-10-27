package io.joshatron.tak.server.database;

import io.joshatron.tak.server.exceptions.BadRequestException;
import io.joshatron.tak.server.exceptions.ForbiddenException;
import io.joshatron.tak.server.exceptions.NoAuthException;
import io.joshatron.tak.server.exceptions.ResourceNotFoundException;
import io.joshatron.tak.server.request.Auth;
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
    public boolean isAuthenticated(Auth auth) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String getAuth = "SELECT username, auth " +
                "FROM users " +
                "WHERE username = ?;";

        try {
            stmt = conn.prepareStatement(getAuth);
            stmt.setString(1, auth.getUsername());
            rs = stmt.executeQuery();

            return (rs.next() && auth.getUsername().equals(rs.getString("username")) &&
                    BCrypt.checkpw(auth.getPassword(), rs.getString("auth")));
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
    public void addUser(Auth auth) throws SQLException, ForbiddenException, BadRequestException {
        PreparedStatement stmt = null;

        //insert new user if it isn't
        String insertUser = "INSERT INTO  users (username, auth) " +
                "VALUES (?,?);";

        try {
            stmt = conn.prepareStatement(insertUser);
            stmt.setString(1, auth.getUsername());
            stmt.setString(2, BCrypt.hashpw(auth.getPassword(), BCrypt.gensalt()));
            stmt.executeUpdate();
        } finally {
            if(stmt != null) {
                stmt.close();
            }
        }
    }

    @Override
    public void updatePassword(String username, String password) throws SQLException, NoAuthException, BadRequestException {
        PreparedStatement stmt = null;

        String changePass = "UPDATE users " +
                "SET auth = ? " +
                "WHERE username = ?;";

        try {
            stmt = conn.prepareStatement(changePass);
            stmt.setString(1, BCrypt.hashpw(password, BCrypt.gensalt()));
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
        finally {
            if(stmt != null) {
                stmt.close();
            }
        }
    }

    @Override
    public void updateUsername(String oldUsername, String newUsername) throws SQLException {
        PreparedStatement stmt = null;

        String changeUser = "UPDATE users " +
                "SET username = ? " +
                "WHERE username = ?;";

        try {
            stmt = conn.prepareStatement(changeUser);
            stmt.setString(1, newUsername);
            stmt.setString(2, oldUsername);
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
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String checkUsername = "SELECT username " +
                "FROM users " +
                "WHERE username = ?;";

        try {
            stmt = conn.prepareStatement(checkUsername);
            stmt.setString(1, username);
            rs = stmt.executeQuery();

            return rs.next();
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
    public User getUserFromId(String id) throws SQLException, ResourceNotFoundException {
        PreparedStatement stmt = null;

        String checkUsername = "SELECT username " +
                "FROM users " +
                "WHERE id = ?;";

        try {
            stmt = conn.prepareStatement(checkUsername);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(rs.getString("username"), id);
            }
            else {
                throw new ResourceNotFoundException("That user could not be found.");
            }
        }
        finally {
            if(stmt != null) {
                stmt.close();
            }
        }
    }

    @Override
    public User getUserFromUsername(String username) throws SQLException, BadRequestException, ResourceNotFoundException {
        PreparedStatement stmt = null;

        String checkUsername = "SELECT id " +
                "FROM users " +
                "WHERE username = ?;";

        try {
            stmt = conn.prepareStatement(checkUsername);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(username, rs.getString("id"));
            }
            else {
                throw new ResourceNotFoundException("That user could not be found.");
            }
        }
        finally {
            if(stmt != null) {
                stmt.close();
            }
        }
    }

    public Connection getConnection() {
        return conn;
    }
}
