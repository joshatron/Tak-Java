package io.joshatron.tak.server.database;

import io.joshatron.tak.server.requestbody.*;

public interface GameDAO {

    boolean requestGame(GameRequest request);
    boolean requestRandomGame(RandomGame request);
    boolean respondToGame(GameResponse response);
    boolean playTurn(PlayTurn turn);

    int[] checkIncomingGames(Auth auth);
    int[] checkOutgoingGames(Auth auth);
    int[] listCompletedGames(ListCompleted completed);
    int[] listIncompleteGames(ListIncomplete incomplete);
    String[] getGame(GetGame game);
}
