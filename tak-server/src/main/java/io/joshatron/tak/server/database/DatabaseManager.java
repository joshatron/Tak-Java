package io.joshatron.tak.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    public static Connection getConnection() {
        String database = "server.db";

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + database);
            initializeDatabase(conn);

            return conn;
        }
        catch(SQLException e) {
            System.out.println("Failed to connect to database.");
            e.printStackTrace();
        }

        return null;
    }

    private static void initializeDatabase(Connection conn) {
        String userTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id integer PRIMARY KEY AUTOINCREMENT," +
                "username text UNIQUE NOT NULL," +
                "auth text NOT NULL);";

        String friendTable = "CREATE TABLE IF NOT EXISTS friends (" +
                "id integer PRIMARY KEY AUTOINCREMENT," +
                "requester integer NOT NULL," +
                "acceptor integer NOT NULL);";

        String blockedTable = "CREATE TABLE IF NOT EXISTS blocked (" +
                "id integer PRIMARY KEY AUTOINCREMENT," +
                "requester integer NOT NULL," +
                "blocked integer NOT NULL);";

        String friendRequestsTable = "CREATE TABLE IF NOT EXISTS friend_requests (" +
                "id integer PRIMARY KEY AUTOINCREMENT," +
                "requester integer NOT NULL," +
                "acceptor integer NOT NULL);";

        String messagesTable = "CREATE TABLE IF NOT EXISTS messages(" +
                "id integer PRIMARY KEY AUTOINCREMENT," +
                "sender integer NOT NULL," +
                "recipient integer NOT NULL," +
                "message text NOT NULL," +
                "time integer NOT NULL," +
                "opened integer NOT NULL);";

        String randomRequestsTable = "CREATE TABLE IF NOT EXISTS random_requests (" +
                "requester integer PRIMARY KEY," +
                "size integer NOT NULL);";

        String gameRequestsTable = "CREATE TABLE IF NOT EXISTS game_requests (" +
                "id integer PRIMARY KEY AUTOINCREMENT," +
                "requester integer NOT NULL," +
                "acceptor integer NOT NULL," +
                "size integer NOT NULL," +
                "white integer NOT NULL," +
                "first integer NOT NULL);";

        String gamesTable = "CREATE TABLE IF NOT EXISTS games (" +
                "id text PRIMARY KEY," +
                "white integer NOT NULL," +
                "black integer NOT NULL," +
                "size integer NOT NULL," +
                "first integer NOT NULL," +
                "start integer NOT NULL," +
                "end integer," +
                "done integer NOT NULL);";

        String turnsTable = "CREATE TABLE IF NOT EXISTS turns (" +
                "id integer PRIMARY KEY AUTOINCREMENT," +
                "game_id text NOT NULL," +
                "turn_order integer NOT NULL," +
                "turn text NOT NULL);";

        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(userTable);
            stmt.executeUpdate(friendTable);
            stmt.executeUpdate(blockedTable);
            stmt.executeUpdate(friendRequestsTable);
            stmt.executeUpdate(messagesTable);
            stmt.executeUpdate(randomRequestsTable);
            stmt.executeUpdate(gameRequestsTable);
            stmt.executeUpdate(gamesTable);
            stmt.executeUpdate(turnsTable);
            stmt.close();

        } catch (SQLException e) {
            System.out.println("Failed to initialize database.");
            e.printStackTrace();
        }
    }
}
