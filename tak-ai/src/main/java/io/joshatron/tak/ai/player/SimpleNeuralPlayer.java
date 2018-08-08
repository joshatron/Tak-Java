package io.joshatron.tak.ai.player;

import io.joshatron.tak.engine.game.GameState;
import io.joshatron.tak.engine.player.TakPlayer;
import io.joshatron.tak.engine.turn.Turn;
import io.joshatron.tak.ai.neuralnet.FeedForwardNeuralNetwork;
import io.joshatron.tak.ai.neuralnet.NetUtils;

import java.util.ArrayList;

public class SimpleNeuralPlayer implements TakPlayer {

    FeedForwardNeuralNetwork net;

    public SimpleNeuralPlayer(FeedForwardNeuralNetwork net) {
        this.net = net;
    }

    @Override
    public Turn getTurn(GameState state) {
        ArrayList<Turn> turns = state.getPossibleTurns();

        double max = -1;
        Turn maxTurn = null;
        for(Turn turn : turns) {
            state.executeTurn(turn);
            double output = computeState(state);
            if(output > max) {
                max = output;
                maxTurn = turn;
            }
            state.undoTurn();
        }

        return maxTurn;
    }

    private double computeState(GameState state) {
        double[] inputs = NetUtils.getInputs(state, state.isWhiteTurn());

        double[] outputs = net.compute(inputs);

        if(state.isWhiteTurn()) {
            return outputs[0];
        }
        else {
            return outputs[1];
        }
    }
}
