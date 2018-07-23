package io.joshatron.neuralnet;

import io.joshatron.engine.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class BackPropTrainer {

    public static void main(String[] args) {
        double[] inGameRate = {.005,.01,.015,.02,.025};
        double[] afterGameRate = {.01,.05,.1};
        double[] momentum = {0};
        int[] hiddenSize = {40};
        int games = 500000;

        System.out.println("Initializing training...");

        for(double inGame : inGameRate) {
            for(double afterGame : afterGameRate) {
                for(double mom : momentum) {
                    for(int hidden : hiddenSize) {
                        FeedForwardNeuralNetwork net = new FeedForwardNeuralNetwork(1, new int[]{94, hidden, 2}, ActivationFunction.LOGISTIC, mom, inGame);
                        System.out.println("Training with the following parameters:");
                        System.out.println("In game rate: " + inGame);
                        System.out.println("After game rate: " + afterGame);
                        System.out.println("Momentum: " + mom);
                        System.out.println("Hidden size: " + hidden);
                        System.out.println("Initial win rate: " + RateNet.getWinPercent(net) + "%");
                        System.out.println();

                        long firstTime = new Date().getTime();

                        for (int i = 0; i < games; i++) {
                            if (i < games / 2) {
                                playGame(net, inGame, afterGame, true);
                            } else {
                                playGame(net, inGame, afterGame, false);
                            }

                            if (i % 1000 == 0 && i != 0) {
                                long thisTime = new Date().getTime();
                                long timeLeft = (games - i) * ((thisTime - firstTime) / i) / 1000 / 60;
                                int winPercent = RateNet.getWinPercent(net);
                                System.out.printf("Game %d- %d%% win rate, %d:%02d remaining\n", i, winPercent, timeLeft / 60, timeLeft % 60);
                                try {
                                    net.export(new File(inGame + "_" + afterGame + "_" + mom + "_" + hidden + "_" + games + ".json"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        try {
                            net.export(new File(inGame + "_" + afterGame + "_" + mom + "_" + hidden + "_" + games + ".json"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private static void playGame(FeedForwardNeuralNetwork net, double inGameRate, double afterGameRate, boolean pathOnly) {
        net.setLearningRate(inGameRate);
        GameState state = new GameState(true, 5);

        int round = 0;
        while(!state.checkForWinner().isFinished()) {
            if(round > 200) {
                break;
            }
            round++;
            double[] lastInputs = NetUtils.getInputs(state, state.isWhiteTurn());
            double[] max = new double[]{0,0};
            Turn maxTurn = null;
            for(Turn turn : state.getPossibleTurns()) {
                state.executeTurn(turn);
                double[] out = net.compute(NetUtils.getInputs(state, state.isWhiteTurn()));
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

            if(maxTurn != null) {
                state.executeTurn(maxTurn);
            }
            else {
                break;
            }
            net.backprop(lastInputs, max);
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
