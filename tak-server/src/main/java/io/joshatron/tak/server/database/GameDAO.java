package io.joshatron.tak.server.database;

import io.joshatron.tak.server.request.*;
import io.joshatron.tak.server.response.GameInfo;
import io.joshatron.tak.server.response.RequestInfo;

public interface GameDAO {

    boolean requestGame(GameRequest request);
    boolean requestRandomGame(RandomGame request);
    boolean respondToGame(GameResponse response);
    boolean playTurn(PlayTurn turn);

    RequestInfo[] checkIncomingGames(Auth auth);
    RequestInfo[] checkOutgoingGames(Auth auth);
    int[] listCompletedGames(ListCompleted completed);
    int[] listIncompleteGames(ListIncomplete incomplete);
    GameInfo getGame(GetGame game);
}
