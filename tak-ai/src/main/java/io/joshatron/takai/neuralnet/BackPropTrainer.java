package io.joshatron.takai.neuralnet;

import io.joshatron.takengine.engine.*;
import io.joshatron.takai.player.SimpleNeuralPlayer;
import io.joshatron.takengine.turn.Turn;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class BackPropTrainer {

    public static void train(double inGameRate, double afterGameRate, double momentum,
                             int hiddenSize, int games, int boardSize) {
        System.out.println("Initializing training...");
        String label = "master";

        int inputSize = (boardSize * boardSize * 3) + (boardSize * 2) + 9;
        FeedForwardNeuralNetwork net = new FeedForwardNeuralNetwork(1, new int[]{inputSize, hiddenSize, 2}, ActivationFunction.LOGISTIC, momentum, inGameRate);

        System.out.println("Training with the following parameters:");
        System.out.println("In game rate: " + inGameRate);
        System.out.println("After game rate: " + afterGameRate);
        System.out.println("Momentum: " + momentum);
        System.out.println("Hidden size: " + hiddenSize);
        System.out.println("Initial win rate: " + RateNet.getWinPercent(net, boardSize).getWinPercentage() + "%");
        System.out.println();

        long firstTime = new Date().getTime();

        for (int i = 0; i < games; i++) {
            //code for making it try for paths for the first 3/4 of the game
            /*if (i < (games * 3) / 4) {
                playGame(net, inGameRate, afterGameRate, true, boardSize);
            } else {
                playGame(net, inGameRate, afterGameRate, false, boardSize);
            }*/
            playGame(net, inGameRate, afterGameRate, false, boardSize);

            if (i % 1000 == 0 && i != 0) {
                long thisTime = new Date().getTime();
                long timeLeft = (games - i) * ((thisTime - firstTime) / i) / 1000 / 60;
                RateNetResults results = RateNet.getWinPercent(net, boardSize);
                System.out.printf("Game %8d- %3d%% win rate, %3d%% of which by path, %3d:%02d remaining, label: %s\n", i, (int)results.getWinPercentage(), (int)results.getPathPercentage(), timeLeft / 60, timeLeft % 60, label);
                try {
                    net.export(new File( boardSize + "_" + inGameRate + "_" + afterGameRate + "_" + momentum + "_" + hiddenSize + "_" + games + "_" + label + ".json"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            net.export(new File(boardSize + "_" + inGameRate + "_" + afterGameRate + "_" + momentum + "_" + hiddenSize + "_" + games + "_" + label + ".json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void playGame(FeedForwardNeuralNetwork net, double inGameRate, double afterGameRate, boolean pathOnly, int boardSize) {
        net.setLearningRate(inGameRate);
        GameState state = new GameState(Player.WHITE, boardSize);
        SimpleNeuralPlayer player = new SimpleNeuralPlayer(net);

        int round = 0;
        while(!state.checkForWinner().isFinished()) {
            if(round > 200) {
                break;
            }
            round++;
            double[] lastInputs = NetUtils.getInputs(state, state.isWhiteTurn());
            Turn turn = player.getTurn((GameState)state.clone());

            if(turn != null) {
                state.executeTurn(turn);
            }
            else {
                break;
            }
            net.backprop(lastInputs, net.compute(NetUtils.getInputs(state, state.isWhiteTurn())));
        }

        GameResult result = state.checkForWinner();

        double[] finalOut = new double[]{0,0};
        if(!pathOnly || result.getReason() == WinReason.PATH) {
            finalOut[0] = 1;
        }

        double[] finalIn = NetUtils.getInputs(state, !state.isWhiteTurn());

        net.setLearningRate(afterGameRate);
        net.backprop(finalIn, finalOut);

        finalOut[0] = 0;
        if(!pathOnly || result.getReason() == WinReason.PATH) {
            finalOut[1] = 1;
        }

        finalIn = NetUtils.getInputs(state, state.isWhiteTurn());

        net.backprop(finalIn, finalOut);
    }
}
