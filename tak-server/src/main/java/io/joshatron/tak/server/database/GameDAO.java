package io.joshatron.tak.server.database;

import io.joshatron.tak.server.request.*;
import io.joshatron.tak.server.response.GameInfo;
import io.joshatron.tak.server.response.RequestInfo;

public interface GameDAO {

    boolean requestGame(GameRequest request) throws Exception;
    boolean requestRandomGame(RandomGame request) throws Exception;
    boolean respondToGame(GameResponse response) throws Exception;
    boolean playTurn(PlayTurn turn) throws Exception;

    RequestInfo[] checkIncomingGames(Auth auth) throws Exception;
    RequestInfo[] checkOutgoingGames(Auth auth) throws Exception;
    int[] listCompletedGames(ListCompleted completed) throws Exception;
    int[] listIncompleteGames(ListIncomplete incomplete) throws Exception;
    GameInfo getGame(GetGame game) throws Exception;
}
