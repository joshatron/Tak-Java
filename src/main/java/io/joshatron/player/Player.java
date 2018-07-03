package io.joshatron.player;

import io.joshatron.engine.GameState;
import io.joshatron.engine.Turn;

public interface Player {

    public Turn getTurn(GameState state);
}
