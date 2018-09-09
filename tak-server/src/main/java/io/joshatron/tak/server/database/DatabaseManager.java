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
                "requester integer PRIMARY KEY," +
                "acceptor integer PRIMARY KEY);";

        String blockedTable = "CREATE TABLE IF NOT EXISTS blocked (" +
                "requester integer PRIMARY KEY," +
                "blocked integer PRIMARY KEY);";

        String friendRequestsTable = "CREATE TABLE IF NOT EXISTS friend_requests (" +
                "requester integer PRIMARY KEY," +
                "acceptor integer PRIMARY KEY);";

        String messagesTable = "CREATE TABLE IF NOT EXISTS messages(" +
                "id integer PRIMARY KEY AUTOINCREMENT," +
                "from integer NOT NULL," +
                "to integer NOT NULL," +
                "message text NOT NULL," +
                "time integer NOT NULL," +
                "opened integer NOT NULL);";

        String randomRequestsTable = "CREATE TABLE IF NOT EXISTS random_requests (" +
                "requester integer PRIMARY KEY," +
                "size integer NOT NULL);";

        String gameRequestsTable = "CREATE TABLE IF NOT EXISTS game_requests (" +
                "requester integer PRIMARY KEY," +
                "acceptor integer PRIMARY KEY," +
                "size integer NOT NULL," +
                "white integer NOT NULL," +
                "first integer NOT NULL);";

        String gamesTable = "CREATE TABLE IF NOT EXISTS games (" +
                "id integer PRIMARY KEY AUTOINCREMENT," +
                "white integer NOT NULL," +
                "black integer NOT NULL," +
                "size integer NOT NULL," +
                "first integer NOT NULL," +
                "start integer NOT NULL," +
                "end integer," +
                "done integer NOT NULL);";

        String turnsTable = "CREATE TABLE IF NOT EXISTS turns (" +
                "id integer PRIMARY KEY," +
                "order integer PRIMARY KEY," +
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
