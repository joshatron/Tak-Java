package io.joshatron.neuralnet;

import io.joshatron.engine.GameState;
import io.joshatron.engine.Piece;
import io.joshatron.engine.Turn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BackPropTrainer {

    public static void main(String[] args) {
        double inGameRate = .05;
        double afterGameRate = .005;
        double momentum = .001;
        int hiddenSize = 100;
        int games = 1000000;

        FeedForwardNeuralNetwork net = new FeedForwardNeuralNetwork(1, new int[]{81, hiddenSize, 2}, ActivationFunction.LOGISTIC, momentum, inGameRate);

        for(int i = 0; i < games; i++) {
            if(i % 100 == 0) {
                System.out.println("Game " + i);
            }
            playGame(net, inGameRate, afterGameRate);
        }

        try {
            net.export(new File(inGameRate + "_" + afterGameRate + "_" + momentum + "_" + hiddenSize + "_" + games + ".json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void playGame(FeedForwardNeuralNetwork net, double inGameRate, double afterGameRate) {
        net.setLearningRate(inGameRate);
        GameState state = new GameState(true, 5);
        double[] lastInputs = getInputs(state);

        while(state.checkForWinner() == 0) {
            double[] max = new double[]{0,0};
            Turn maxTurn = null;
            for(Turn turn : state.getPossibleTurns()) {
                state.executeTurn(turn);
                double[] out = net.compute(getInputs(state));
                if(state.isWhiteTurn()) {
                    if(out[0] > max[0]) {
                        max = out;
                        maxTurn = turn;
                    }
                }
                else {
                    if(out[1] > max[1]) {
                        max = out;
                        maxTurn = turn;
                    }
                }
                state.undoTurn();
            }

            state.executeTurn(maxTurn);
            net.backprop(lastInputs, max);
        }

        double[] finalOut = new double[]{0,0};
        if(state.checkForWinner() == 1) {
            finalOut[0] = 1;
        }
        else if(state.checkForWinner() == 2) {
            finalOut[1] = 1;
        }

        double[] finalIn = getInputs(state);

        net.setLearningRate(afterGameRate);
        net.backprop(finalIn, finalOut);
    }

    private static double[] getInputs(GameState state) {
        double[] inputs = new double[(state.getBoardSize() * state.getBoardSize() * 3) + 6];

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

        return inputs;
    }
}
