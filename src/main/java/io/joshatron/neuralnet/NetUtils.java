package io.joshatron.neuralnet;

import io.joshatron.engine.GameState;
import io.joshatron.engine.Piece;

import java.util.ArrayList;

public class NetUtils {

    public static double[] getInputs(GameState state) {
        double[] inputs = new double[(state.getBoardSize() * state.getBoardSize() * 3) + 7];

        int i = 0;
        int filled = 0;
        int control = 0;
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
                            case WALL:
                                inputs[i] = 1;
                                break;
                            case STONE:
                                inputs[i] = 2;
                                control++;
                                break;
                            case CAPSTONE:
                                inputs[i] = 3;
                                control++;
                                break;
                        }
                    } else {
                        switch (piece.getType()) {
                            case WALL:
                                inputs[i] = -1;
                                break;
                            case STONE:
                                inputs[i] = -2;
                                control--;
                                break;
                            case CAPSTONE:
                                inputs[i] = -3;
                                control--;
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
        inputs[i] = state.getTurns().size() / 100.;
        i++;
        inputs[i] = (double)filled / (state.getBoardSize() * state.getBoardSize());
        i++;
        inputs[i] = control;

        return inputs;
    }
}
