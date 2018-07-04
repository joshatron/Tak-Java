package io.joshatron.neuralnet;

import io.joshatron.engine.GameState;
import io.joshatron.engine.Turn;

import java.io.File;
import java.io.IOException;

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

        while(state.checkForWinner() == 0) {
            double[] lastInputs = NetUtils.getInputs(state);
            double[] max = new double[]{0,0};
            Turn maxTurn = null;
            for(Turn turn : state.getPossibleTurns()) {
                state.executeTurn(turn);
                double[] out = net.compute(NetUtils.getInputs(state));
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

        double[] finalIn = NetUtils.getInputs(state);

        net.setLearningRate(afterGameRate);
        net.backprop(finalIn, finalOut);
    }
}
