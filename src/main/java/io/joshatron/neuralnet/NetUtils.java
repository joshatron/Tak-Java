package io.joshatron.neuralnet;

import io.joshatron.engine.GameState;
import io.joshatron.engine.Piece;

import java.util.ArrayList;

public class NetUtils {

    public static double[] getInputs(GameState state) {
        double[] inputs = new double[(state.getBoardSize() * state.getBoardSize() * 3) + 9];

        int i = 0;
        int filled = 0;
        int control = 0;
        int whitePathPower = 0;
        int blackPathPower = 0;
        for(int x = 0; x < state.getBoardSize(); x++) {
            for(int y = 0; y < state.getBoardSize(); y++) {
                //if the board spot is empty
                if(state.getBoard().getPosition(x, y).getHeight() == 0) {
                    inputs[i] = 0;
                    i++;
                    inputs[i] = 0;
                    i++;
                    inputs[i] = 0;
                    i++;
                }
                else {
                    //increase percent filled
                    filled++;

                    //top piece
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

                    //stack height
                    inputs[i] = stack.size();
                    i++;

                    //stack own position
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

                    if(x < state.getBoardSize() - 1) {
                        if(piece.isWhite() &&
                           state.getBoard().getPosition(x + 1, y).getHeight() > 0 &&
                           state.getBoard().getPosition(x + 1, y).getTopPiece().isWhite()) {
                            whitePathPower++;
                        }
                        else if(piece.isBlack() &&
                                state.getBoard().getPosition(x + 1, y).getHeight() > 0 &&
                                state.getBoard().getPosition(x + 1, y).getTopPiece().isBlack()) {
                            blackPathPower++;
                        }
                    }

                    if(y < state.getBoardSize() - 1) {
                        if(piece.isWhite() &&
                                state.getBoard().getPosition(x, y + 1).getHeight() > 0 &&
                                state.getBoard().getPosition(x, y + 1).getTopPiece().isWhite()) {
                            whitePathPower++;
                        }
                        else if(piece.isBlack() &&
                                state.getBoard().getPosition(x, y + 1).getHeight() > 0 &&
                                state.getBoard().getPosition(x, y + 1).getTopPiece().isBlack()) {
                            blackPathPower++;
                        }
                    }
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
        i++;
        inputs[i] = whitePathPower;
        i++;
        inputs[i] = blackPathPower;

        return inputs;
    }
}
