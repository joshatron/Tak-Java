package io.joshatron.tak.server.database;

import io.joshatron.tak.server.exceptions.BadRequestException;
import io.joshatron.tak.server.exceptions.ForbiddenException;
import io.joshatron.tak.server.exceptions.NoAuthException;
import io.joshatron.tak.server.request.Auth;
import io.joshatron.tak.server.request.PassChange;
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
        if(auth.getUsername() == null || auth.getPassword() == null) {
            throw new BadRequestException();
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
    public void registerUser(Auth auth) throws SQLException, ForbiddenException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        //make sure username isn't taken
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
                throw new ForbiddenException();
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
            throw new BadRequestException();
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
        if(username == null) {
            throw new BadRequestException();
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

    public int idFromUsername(String username) throws SQLException, BadRequestException, ForbiddenException {
        if(username == null) {
            throw new BadRequestException();
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
                return rs.getInt("id");
            }
            else {
                throw new ForbiddenException();
            }
        }
        finally {
            if(selectStmt != null) {
                selectStmt.close();
            }
        }
    }

    public String usernameFromId(int id) throws SQLException, ForbiddenException {
        PreparedStatement selectStmt = null;

        String checkUsername = "SELECT username " +
                "FROM users " +
                "WHERE id = ?;";

        try {
            selectStmt = conn.prepareStatement(checkUsername);
            selectStmt.setInt(1, id);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                return rs.getString("username");
            }
            else {
                throw new ForbiddenException();
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
