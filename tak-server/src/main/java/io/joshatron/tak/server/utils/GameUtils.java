package io.joshatron.tak.server.utils;

import io.joshatron.tak.server.database.AccountDAO;
import io.joshatron.tak.server.database.GameDAO;
import io.joshatron.tak.server.database.SocialDAO;
import io.joshatron.tak.server.exceptions.GameServerException;
import io.joshatron.tak.server.request.*;
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

    public void requestGame(GameRequest gameRequest) throws GameServerException {
    }

    public void deleteRequest(UserInteraction userInteraction) throws GameServerException {
    }

    public void respondToGame(Answer answer) throws GameServerException {
    }

    public void requestRandomGame(RandomGame randomGame) throws GameServerException {
    }

    public RequestInfo[] checkIncomingRequests(Auth auth) throws GameServerException {
        return null;
    }

    public RequestInfo[] checkOutgoingRequests(Auth auth) throws GameServerException {
        return null;
    }

    public void deleteRandomRequest(RandomGame randomGame) throws GameServerException {
    }

    public int[] checkRandomSizes(Auth auth) throws GameServerException {
        return null;
    }
}
