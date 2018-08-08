package io.joshatron.tak.ai.player;

import io.joshatron.tak.engine.game.GameState;
import io.joshatron.tak.engine.player.TakPlayer;
import io.joshatron.tak.engine.turn.Turn;

import java.util.ArrayList;
import java.util.Random;

public class RandomPlayer implements TakPlayer {
    Random rand;

    public RandomPlayer() {
        rand = new Random();
    }

    @Override
    public Turn getTurn(GameState state) {
        ArrayList<Turn> turns = state.getPossibleTurns();

        return turns.get(rand.nextInt(turns.size()));
    }
}
