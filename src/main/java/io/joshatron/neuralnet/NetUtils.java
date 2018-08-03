package io.joshatron.neuralnet;

import io.joshatron.engine.GameState;
import io.joshatron.engine.Piece;
import io.joshatron.engine.PieceType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NetUtils {

    public static double[] getInputs(GameState state, boolean whitePlayer) {
        double[] inputs = new double[(state.getBoardSize() * state.getBoardSize() * 3) + (state.getBoardSize() * 2) + 9];


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
                    if (piece.isWhite() == whitePlayer) {
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

                    if(whitePlayer) {
                        inputs[i] = (((double) white / (white + black)) * 2) - 1;
                    }
                    else {
                        inputs[i] = (((double) black / (white + black)) * 2) - 1;
                    }
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
                        if(piece.isWhite() && piece.getType() != PieceType.WALL &&
                           state.getBoard().getPosition(x, y + 1).getHeight() > 0 &&
                           state.getBoard().getPosition(x, y + 1).getTopPiece().isWhite()) {
                            whitePathPower++;
                        }
                        else if(piece.isBlack() && piece.getType() != PieceType.WALL &&
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
        if(whitePlayer) {
            inputs[i] = whitePathPower;
            i++;
            inputs[i] = blackPathPower;
            i++;
        }
        else {
            inputs[i] = blackPathPower;
            i++;
            inputs[i] = whitePathPower;
            i++;
        }

        //horizontal and vertical power
        for(int x = 0; x < state.getBoardSize(); x++) {
            int horizontalPower = 0;
            int verticalPower = 0;
            for(int y = 0; y < state.getBoardSize(); y++) {
                //horizontal power
                if(state.getBoard().getPosition(x, y).getHeight() != 0) {
                    ArrayList<Piece> stack = state.getBoard().getPosition(x, y).getPieces();
                    Piece piece = stack.get(stack.size() - 1);
                    if (piece.isWhite() == whitePlayer) {
                        switch (piece.getType()) {
                            case WALL:
                                horizontalPower--;
                                break;
                            case STONE:
                                horizontalPower++;
                                break;
                            case CAPSTONE:
                                horizontalPower += 2;
                                break;
                        }
                    } else {
                        switch (piece.getType()) {
                            case WALL:
                                horizontalPower -= 2;
                                break;
                            case STONE:
                                horizontalPower--;
                                break;
                            case CAPSTONE:
                                horizontalPower -= 3;
                                break;
                        }
                    }
                }
                //vertical power
                if(state.getBoard().getPosition(y, x).getHeight() != 0) {
                    ArrayList<Piece> stack = state.getBoard().getPosition(y, x).getPieces();
                    Piece piece = stack.get(stack.size() - 1);
                    if (piece.isWhite() == whitePlayer) {
                        switch (piece.getType()) {
                            case WALL:
                                horizontalPower--;
                                break;
                            case STONE:
                                horizontalPower++;
                                break;
                            case CAPSTONE:
                                horizontalPower += 2;
                                break;
                        }
                    } else {
                        switch (piece.getType()) {
                            case WALL:
                                horizontalPower -= 2;
                                break;
                            case STONE:
                                horizontalPower--;
                                break;
                            case CAPSTONE:
                                horizontalPower -= 3;
                                break;
                        }
                    }
                }
            }

            inputs[i] = horizontalPower;
            i++;
            inputs[i] = verticalPower;
            i++;
        }

        return inputs;
    }

    public static NetWithStats getNetFromFile(File file) {
         String name = file.getName();
         name = name.substring(0,name.length() - 5);
         String[] names = name.split("_");
         int boardSize = Integer.parseInt(names[0]);
         double inGameRate = Double.parseDouble(names[1]);
         double afterGameRate = Double.parseDouble(names[2]);
         double momentum = Double.parseDouble(names[3]);
         int hiddenSize = Integer.parseInt(names[4]);
         int games = Integer.parseInt(names[5]);
         String label = names[6];

        try {
            return new NetWithStats(inGameRate, afterGameRate, momentum, hiddenSize, games, boardSize, label, new FeedForwardNeuralNetwork(file));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
