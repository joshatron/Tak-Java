package io.joshatron.tak.server.database;

import io.joshatron.tak.server.request.*;
import io.joshatron.tak.server.response.GameInfo;
import io.joshatron.tak.server.response.RequestInfo;

import java.sql.SQLException;

public interface GameDAO {

    boolean requestGame(GameRequest request) throws SQLException;
    boolean requestRandomGame(RandomGame request) throws SQLException;
    boolean respondToGame(GameResponse response) throws SQLException;
    boolean playTurn(PlayTurn turn) throws SQLException;

    RequestInfo[] checkIncomingGames(Auth auth) throws SQLException;
    RequestInfo[] checkOutgoingGames(Auth auth) throws SQLException;
    int[] listCompletedGames(ListCompleted completed) throws SQLException;
    int[] listIncompleteGames(ListIncomplete incomplete) throws SQLException;
    GameInfo getGame(GetGame game) throws SQLException;
}
