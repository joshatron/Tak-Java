package io.joshatron.player;

import io.joshatron.engine.GameState;
import io.joshatron.engine.Piece;
import io.joshatron.engine.Turn;
import io.joshatron.neuralnet.FeedForwardNeuralNetwork;

import java.util.ArrayList;

public class SimpleNeuralPlayer implements Player {

    FeedForwardNeuralNetwork net;

    public SimpleNeuralPlayer(FeedForwardNeuralNetwork net) {
        this.net = net;
    }

    @Override
    public Turn getTurn(GameState state) {
        ArrayList<Turn> turns = state.getPossibleTurns();

        double max = 0;
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
        double[] inputs = new double[(state.getBoardSize() * state.getBoardSize()) + 6];

        int i = 0;
        int filled = 0;
        for(int x = 0; x < state.getBoardSize(); x++) {
            for(int y = 0; y < state.getBoardSize(); y++) {
                if(state.getBoard().getPosition(x, y).getHeight() == 0) {
                    inputs[i] = 0;
                    i++;
                    inputs[i] = 0;
                    i++;
                    inputs[i] = 0;
                    i++;
                }
                else {
                    filled++;

                    ArrayList<Piece> stack = state.getBoard().getPosition(x, y).getPieces();
                    Piece piece = stack.get(stack.size() - 1);
                    if (piece.isWhite()) {
                        switch (piece.getType()) {
                            case STONE:
                                inputs[i] = 1;
                                break;
                            case WALL:
                                inputs[i] = 2;
                                break;
                            case CAPSTONE:
                                inputs[i] = 3;
                                break;
                        }
                    } else {
                        switch (piece.getType()) {
                            case STONE:
                                inputs[i] = -1;
                                break;
                            case WALL:
                                inputs[i] = -2;
                                break;
                            case CAPSTONE:
                                inputs[i] = -3;
                                break;
                        }
                    }
                    i++;

                    inputs[i] = stack.size();
                    i++;

                    int white = 0;
                    int black = 0;
                    for (Piece p : stack) {
                        if (p.isWhite()) {
                            white++;
                        } else {
                            black++;
                        }
                    }

                    inputs[i] = (((double) white / (white + black)) * 2) - 1;
                    i++;
                }
            }
        }

        inputs[i] = state.getWhiteNormalPiecesLeft();
        i++;
        inputs[i] = state.getWhiteCapstonesLeft();
        i++;
        inputs[i] = state.getBlackNormalPiecesLeft();
        i++;
        inputs[i] = state.getBlackCapstonesLeft();
        i++;
        inputs[i] = state.getTurns().size();
        i++;
        inputs[i] = (double)filled / (state.getBoardSize() * state.getBoardSize());

        double[] outputs = net.compute(inputs);

        if(state.isWhiteTurn()) {
            return outputs[0];
        }
        else {
            return outputs[1];
        }
    }
}
