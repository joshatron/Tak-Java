package io.joshatron.tak.server.utils;

import io.joshatron.tak.server.database.AccountDAO;
import io.joshatron.tak.server.database.GameDAO;
import io.joshatron.tak.server.database.SocialDAO;
import io.joshatron.tak.server.exceptions.GameServerException;
import io.joshatron.tak.server.request.Answer;
import io.joshatron.tak.server.request.GameRequest;
import io.joshatron.tak.server.request.UserInteraction;

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
}
