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
        String getAuth = "SELECT username, auth " +
                "FROM users " +
                "WHERE username = ?;";

        PreparedStatement stmt = conn.prepareStatement(getAuth);
        stmt.setString(1, auth.getUsername());
        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            if(auth.getUsername().equals(rs.getString("username")) &&
                BCrypt.checkpw(auth.getPassword(), rs.getString("auth"))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean registerUser(Auth auth) throws SQLException {
        //make sure username isn't taken
        String checkUsername = "SELECT username " +
                "FROM users " +
                "WHERE username = ?;";

        PreparedStatement selectStmt = conn.prepareStatement(checkUsername);
        selectStmt.setString(1, auth.getUsername());
        ResultSet rs = selectStmt.executeQuery();

        if(rs.next()) {
            return false;
        }

        //insert new user if it isn't
        String insertUser = "INSERT INTO  users (username, auth) " +
                "VALUES (?,?)";

        PreparedStatement stmt = conn.prepareStatement(insertUser);
        stmt.setString(1, auth.getUsername());
        stmt.setString(2, BCrypt.hashpw(auth.getPassword(), BCrypt.gensalt()));
        stmt.executeUpdate();

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

        String changePass = "UPDATE users " +
                "SET auth = ? " +
                "WHERE username = ?;";

        PreparedStatement stmt = conn.prepareStatement(changePass);
        stmt.setString(1, BCrypt.hashpw(change.getUpdated(), BCrypt.gensalt()));
        stmt.setString(2, change.getAuth().getUsername());
        stmt.executeUpdate();

        return true;
    }

    public int idFromUsername(String username) throws SQLException {
        String checkUsername = "SELECT id " +
                "FROM users " +
                "WHERE username = ?;";

        PreparedStatement selectStmt = conn.prepareStatement(checkUsername);
        selectStmt.setString(1, username);
        ResultSet rs = selectStmt.executeQuery();

        if(rs.next()) {
            return rs.getInt("id");
        }

        return -9999;
    }

    public String usernameFromId(int id) throws SQLException {
        String checkUsername = "SELECT username " +
                "FROM users " +
                "WHERE id = ?;";

        PreparedStatement selectStmt = conn.prepareStatement(checkUsername);
        selectStmt.setInt(1, id);
        ResultSet rs = selectStmt.executeQuery();

        if(rs.next()) {
            return rs.getString("username");
        }

        return null;
    }

    public Connection getConnection() {
        return conn;
    }
}
