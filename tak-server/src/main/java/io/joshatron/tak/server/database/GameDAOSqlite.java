package io.joshatron.tak.server.database;

import io.joshatron.tak.server.request.*;
import io.joshatron.tak.server.response.GameInfo;
import io.joshatron.tak.server.response.RequestInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GameDAOSqlite implements GameDAO {

    AccountDAOSqlite accountDAO;
    SocialDAOSqlite socialDAO;
    Connection conn;

    public GameDAOSqlite() {
        accountDAO = new AccountDAOSqlite();
        socialDAO = new SocialDAOSqlite();
        conn = DatabaseManager.getConnection();
    }

    public GameDAOSqlite(AccountDAOSqlite accountDAO, SocialDAOSqlite socialDAO, Connection conn) {
        this.accountDAO = accountDAO;
        this.socialDAO = socialDAO;
        this.conn = conn;
    }

    @Override
    public boolean requestGame(GameRequest request) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(request.getAuth())) {
            return false;
        }
        //if user blocked, return false
        if(socialDAO.isBlocked(request.getAuth().getUsername(), request.getOpponent())) {
            return false;
        }

        //check that request doesn't already exist
        String checkExists = "SELECT requester, acceptor " +
                "FROM game_requests " +
                "WHERE (requester = ? AND acceptor = ?) OR (requester = ? AND acceptor = ?);";

        PreparedStatement checkStmt = conn.prepareStatement(checkExists);
        checkStmt.setInt(1, accountDAO.idFromUsername(request.getAuth().getUsername()));
        checkStmt.setInt(2, accountDAO.idFromUsername(request.getOpponent()));
        checkStmt.setInt(3, accountDAO.idFromUsername(request.getOpponent()));
        checkStmt.setInt(4, accountDAO.idFromUsername(request.getAuth().getUsername()));
        ResultSet checkSet = checkStmt.executeQuery();

        if(checkSet.next()) {
            return false;
        }

        //Create game request
        String addRequest = "INSERT INTO game_requests (requester,acceptor,size,white,first) " +
                "VALUES (?,?,?,?,?);";

        PreparedStatement insertStmt = conn.prepareStatement(addRequest);
        insertStmt.setInt(1, accountDAO.idFromUsername(request.getAuth().getUsername()));
        insertStmt.setInt(2, accountDAO.idFromUsername(request.getOpponent()));
        insertStmt.setInt(3, request.getSize());
        if(request.getColor().toLowerCase().equals("white")) {
            insertStmt.setInt(4, 1);
        }
        else {
            insertStmt.setInt(4, 0);
        }
        if(request.getFirst().toLowerCase().equals("true")) {
            insertStmt.setInt(5, 1);
        }
        else {
            insertStmt.setInt(5, 0);
        }
        insertStmt.executeUpdate();

        return false;
    }

    @Override
    public boolean requestRandomGame(RandomGame request) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(request.getAuth())) {
            return false;
        }
        //check if any requests match and if they do create game
        //if nothing matched create request
        return false;
    }

    @Override
    public boolean respondToGame(GameResponse response) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(response.getAuth())) {
            return false;
        }
        //get request and verify everything correct
        //delete request
        //if accept, create game
        return false;
    }

    @Override
    public boolean playTurn(PlayTurn turn) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(turn.getAuth())) {
            return false;
        }
        //verify it is that player's turn
        //verify the move is legal
        //add turn
        return false;
    }

    @Override
    public RequestInfo[] checkIncomingGames(Auth auth) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(auth)) {
            return null;
        }
        //select all requests and convert to request infos
        return new RequestInfo[0];
    }

    @Override
    public RequestInfo[] checkOutgoingGames(Auth auth) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(auth)) {
            return null;
        }
        //select all requests and convert to request infos
        return new RequestInfo[0];
    }

    @Override
    public int[] listCompletedGames(ListCompleted completed) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(completed.getAuth())) {
            return null;
        }
        //find all completed games and get ids
        return new int[0];
    }

    @Override
    public int[] listIncompleteGames(ListIncomplete incomplete) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(incomplete.getAuth())) {
            return null;
        }
        //find all incomplete games and get ids
        return new int[0];
    }

    @Override
    public GameInfo getGame(GetGame game) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(game.getAuth())) {
            return null;
        }
        //make sure user is a player
        if(!authorizedForGame(game.getAuth().getUsername(), game.getId())) {
            return null;
        }
        //get info on game
        return null;
    }

    private boolean authorizedForGame(String username, String gameID) throws SQLException {
        String getGame = "SELECT white, black " +
                "FROM games " +
                "WHERE id = ?;";

        PreparedStatement gameStmt = conn.prepareStatement(getGame);
        gameStmt.setString(1, gameID);
        ResultSet gameSet = gameStmt.executeQuery();

        if(gameSet.next()) {
            int userID = accountDAO.idFromUsername(username);

            if(userID == gameSet.getInt("white") || userID == gameSet.getInt("black")) {
                return true;
            }
        }

        return false;
    }
}
