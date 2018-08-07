package io.joshatron.takai.player;

import io.joshatron.takengine.engine.GameState;
import io.joshatron.takengine.player.TakPlayer;
import io.joshatron.takengine.turn.Turn;

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
