package io.joshatron.tak.server.database;

import io.joshatron.tak.server.request.*;
import io.joshatron.tak.server.response.GameInfo;
import io.joshatron.tak.server.response.RequestInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
        String incoming = "SELECT users.username as username, size, white, first " +
                "FROM game_requests " +
                "LEFT OUTER JOIN users on game_requests.requester = users.id " +
                "WHERE acceptor = ?;";

        PreparedStatement requestStmt = conn.prepareStatement(incoming);
        requestStmt.setInt(1, accountDAO.idFromUsername(auth.getUsername()));
        ResultSet requestSet = requestStmt.executeQuery();

        ArrayList<RequestInfo> requests = new ArrayList<>();

        while(requestSet.next()) {
            String user = requestSet.getString("username");
            boolean white = requestSet.getInt("white") == 0;
            boolean first = requestSet.getInt("first") == 0;
            int size = requestSet.getInt("size");
            requests.add(new RequestInfo(user, white, first, size));
        }

        return requests.toArray(new RequestInfo[requests.size()]);
    }

    @Override
    public RequestInfo[] checkOutgoingGames(Auth auth) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(auth)) {
            return null;
        }

        //select all requests and convert to request infos
        String incoming = "SELECT users.username as username, size, white, first " +
                "FROM game_requests " +
                "LEFT OUTER JOIN users on game_requests.acceptor = users.id " +
                "WHERE requester = ?;";

        PreparedStatement requestStmt = conn.prepareStatement(incoming);
        requestStmt.setInt(1, accountDAO.idFromUsername(auth.getUsername()));
        ResultSet requestSet = requestStmt.executeQuery();

        ArrayList<RequestInfo> requests = new ArrayList<>();

        while(requestSet.next()) {
            String user = requestSet.getString("username");
            boolean white = requestSet.getInt("white") == 1;
            boolean first = requestSet.getInt("first") == 1;
            int size = requestSet.getInt("size");
            requests.add(new RequestInfo(user, white, first, size));
        }

        return requests.toArray(new RequestInfo[requests.size()]);
    }

    @Override
    public int[] listCompletedGames(ListCompleted completed) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(completed.getAuth())) {
            return null;
        }

        //find all completed games and get ids
        String games = "SELECT id " +
                "FROM games " +
                "WHERE done = 1 AND (white = ? or black = ?);";

        PreparedStatement gameStmt = conn.prepareStatement(games);
        gameStmt.setInt(1, accountDAO.idFromUsername(completed.getAuth().getUsername()));
        gameStmt.setInt(2, accountDAO.idFromUsername(completed.getAuth().getUsername()));
        ResultSet gameSet = gameStmt.executeQuery();

        ArrayList<Integer> ints = new ArrayList<>();

        while(gameSet.next()) {
            ints.add(gameSet.getInt("id"));
        }

        int[] toReturn = new int[ints.size()];

        for(int i = 0; i < toReturn.length; i++) {
            toReturn[i] = ints.get(i).intValue();
        }

        return toReturn;
    }

    @Override
    public int[] listIncompleteGames(ListIncomplete incomplete) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(incomplete.getAuth())) {
            return null;
        }

        //find all completed games and get ids
        String games = "SELECT id " +
                "FROM games " +
                "WHERE done = 0 AND (white = ? or black = ?);";

        PreparedStatement gameStmt = conn.prepareStatement(games);
        gameStmt.setInt(1, accountDAO.idFromUsername(incomplete.getAuth().getUsername()));
        gameStmt.setInt(2, accountDAO.idFromUsername(incomplete.getAuth().getUsername()));
        ResultSet gameSet = gameStmt.executeQuery();

        ArrayList<Integer> ints = new ArrayList<>();

        while(gameSet.next()) {
            ints.add(gameSet.getInt("id"));
        }

        int[] toReturn = new int[ints.size()];

        for(int i = 0; i < toReturn.length; i++) {
            toReturn[i] = ints.get(i).intValue();
        }

        return toReturn;
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
