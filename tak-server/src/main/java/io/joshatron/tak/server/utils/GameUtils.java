package io.joshatron.tak.server.utils;

import io.joshatron.tak.engine.turn.Turn;
import io.joshatron.tak.server.database.AccountDAO;
import io.joshatron.tak.server.database.GameDAO;
import io.joshatron.tak.server.database.SocialDAO;
import io.joshatron.tak.server.exceptions.GameServerException;
import io.joshatron.tak.server.request.*;
import io.joshatron.tak.server.response.GameInfo;
import io.joshatron.tak.server.response.GameNotification;
import io.joshatron.tak.server.response.GameTurn;
import io.joshatron.tak.server.response.RequestInfo;

public class GameUtils {

    private GameDAO gameDAO;
    private SocialDAO socialDAO;
    private AccountDAO accountDAO;

    public GameUtils(GameDAO gameDAO, SocialDAO socialDAO, AccountDAO accountDAO) {
        this.gameDAO = gameDAO;
        this.socialDAO = socialDAO;
        this.accountDAO = accountDAO;
    }

    public void requestGame(Auth auth, String other, GameRequest gameRequest) throws GameServerException {
    }

    public void deleteRequest(Auth auth, String other) throws GameServerException {
    }

    public void respondToGame(Auth auth, String id, String answer) throws GameServerException {
    }

    public RequestInfo[] checkIncomingRequests(Auth auth) throws GameServerException {
        return null;
    }

    public RequestInfo[] checkOutgoingRequests(Auth auth) throws GameServerException {
        return null;
    }

    public void requestRandomGame(Auth auth, int size) throws GameServerException {
    }

    public void deleteRandomRequest(Auth auth, int size) throws GameServerException {
    }

    public int[] checkRandomSizes(Auth auth) throws GameServerException {
        return null;
    }

    public GameInfo getGameInfo(Auth auth, String gameId) throws GameServerException {
        return null;
    }

    public Turn[] getTurns(Auth auth, String gameId) throws GameServerException {
        return null;
    }

    public void playTurn(Auth auth, String gameId, GameTurn turn) throws GameServerException {
    }

    public GameNotification getNotifications(Auth auth) {
        return null;
    }
}
