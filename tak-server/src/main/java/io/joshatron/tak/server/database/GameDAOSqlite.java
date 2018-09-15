package io.joshatron.tak.server.database;

import io.joshatron.tak.server.request.*;
import io.joshatron.tak.server.response.GameInfo;
import io.joshatron.tak.server.response.RequestInfo;

import java.sql.Connection;
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

        return false;
    }

    @Override
    public boolean requestRandomGame(RandomGame request) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(request.getAuth())) {
            return false;
        }
        return false;
    }

    @Override
    public boolean respondToGame(GameResponse response) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(response.getAuth())) {
            return false;
        }
        return false;
    }

    @Override
    public boolean playTurn(PlayTurn turn) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(turn.getAuth())) {
            return false;
        }
        return false;
    }

    @Override
    public RequestInfo[] checkIncomingGames(Auth auth) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(auth)) {
            return null;
        }
        return new RequestInfo[0];
    }

    @Override
    public RequestInfo[] checkOutgoingGames(Auth auth) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(auth)) {
            return null;
        }
        return new RequestInfo[0];
    }

    @Override
    public int[] listCompletedGames(ListCompleted completed) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(completed.getAuth())) {
            return null;
        }
        return new int[0];
    }

    @Override
    public int[] listIncompleteGames(ListIncomplete incomplete) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(incomplete.getAuth())) {
            return null;
        }
        return new int[0];
    }

    @Override
    public GameInfo getGame(GetGame game) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(game.getAuth())) {
            return null;
        }
        if(!authorizedForGame(game.getAuth().getUsername(), game.getId())) {
            return null;
        }

        return null;
    }

    private boolean authorizedForGame(String username, String gameID) {
        return false;
    }
}
