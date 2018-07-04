package io.joshatron.player;

import io.joshatron.engine.GameState;
import io.joshatron.engine.Turn;
import io.joshatron.neuralnet.FeedForwardNeuralNetwork;
import io.joshatron.neuralnet.NetUtils;

public class MiniMaxPlayer implements Player {

    FeedForwardNeuralNetwork net;

    public MiniMaxPlayer(FeedForwardNeuralNetwork net) {
        this.net = net;
    }

    @Override
    public Turn getTurn(GameState state) {
        int depth = 1;

        double best = -9999;
        Turn bestTurn = null;
        for(Turn turn : state.getPossibleTurns()) {
            state.executeTurn(turn);
            double value = getTurnValue(state, false, !state.isWhiteTurn(), depth);

            if(value > best) {
                best = value;
                bestTurn = turn;
            }

            state.undoTurn();
        }

        return bestTurn;
    }

    private double getTurnValue(GameState state, boolean max, boolean white, int depth) {
        int winner = state.checkForWinner();
        if(winner == 1) {
            if(white) {
                return 999;
            }
            else {
                return -999;
            }
        }
        else if(winner == 2) {
            if(white) {
                return -999;
            }
            else {
                return 999;
            }
        }

        if(depth == 0) {
            double[] out = net.compute(NetUtils.getInputs(state));

            if(state.isWhiteTurn()) {
                return out[0];
            }
            else {
                return out[1];
            }
        }

        double extreme = 0;
        if(max) {
            extreme = -9999;
        }
        else {
            extreme = 9999;
        }

        for(Turn turn : state.getPossibleTurns()) {
            state.executeTurn(turn);
            double value = getTurnValue(state, !max, white, depth - 1);
            if(max) {
                if(value > extreme) {
                    extreme = value;
                }
            }
            else {
                if(value < extreme) {
                    extreme = value;
                }
            }

            state.undoTurn();
        }

        return extreme;
    }
}
