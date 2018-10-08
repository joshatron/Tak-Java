package io.joshatron.tak.server.database;

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

            if (rs.next()) {
                if (auth.getUsername().equals(rs.getString("username")) &&
                        BCrypt.checkpw(auth.getPassword(), rs.getString("auth"))) {
                    return true;
                }
            }
        }
        finally {
            if(stmt != null) {
                stmt.close();
            }
            if(rs != null) {
                rs.close();
            }
        }

        return false;
    }

    @Override
    public boolean registerUser(Auth auth) throws SQLException {
        PreparedStatement selectStmt = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        //make sure username isn't taken
        String checkUsername = "SELECT username " +
                "FROM users " +
                "WHERE username = ?;";

        //insert new user if it isn't
        String insertUser = "INSERT INTO  users (username, auth) " +
                "VALUES (?,?)";

        try {
            selectStmt = conn.prepareStatement(checkUsername);
            selectStmt.setString(1, auth.getUsername());
            rs = selectStmt.executeQuery();

            if (rs.next()) {
                return false;
            }

            stmt = conn.prepareStatement(insertUser);
            stmt.setString(1, auth.getUsername());
            stmt.setString(2, BCrypt.hashpw(auth.getPassword(), BCrypt.gensalt()));
            stmt.executeUpdate();
        }
        finally {
            if(selectStmt != null) {
                selectStmt.close();
            }
            if(stmt != null) {
                stmt.close();
            }
            if(rs != null) {
                rs.close();
            }
        }

        return true;
    }

    @Override
    public boolean updatePassword(PassChange change) throws SQLException {
        if(!isAuthenticated(change.getAuth())) {
            return false;
        }
        if(change.getUpdated() == null || change.getUpdated().length() == 0) {
            return false;
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

        return true;
    }

    public int idFromUsername(String username) throws SQLException {
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
        }
        finally {
            if(selectStmt != null) {
                selectStmt.close();
            }
        }

        return -9999;
    }

    public String usernameFromId(int id) throws SQLException {
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
        }
        finally {
            if(selectStmt != null) {
                selectStmt.close();
            }
        }

        return null;
    }

    public Connection getConnection() {
        return conn;
    }
}
