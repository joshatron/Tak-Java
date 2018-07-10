package io.joshatron.neuralnet;

import io.joshatron.engine.GameState;
import io.joshatron.engine.Turn;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class BackPropTrainer {

    public static void main(String[] args) {
        double inGameRate = .005;
        double afterGameRate = .1;
        double momentum = .001;
        int hiddenSize = 50;
        int games = 10000000;

        FeedForwardNeuralNetwork net = new FeedForwardNeuralNetwork(1, new int[]{82, hiddenSize, 2}, ActivationFunction.LOGISTIC, momentum, inGameRate);

        long firstTime = new Date().getTime();

        System.out.println("Initializing training...");

        for(int i = 0; i < games; i++) {
            playGame(net, inGameRate, afterGameRate);

            if(i % 1000 == 0 && i != 0) {
                long thisTime = new Date().getTime();
                long timeLeft = (games - i) * ((thisTime - firstTime) / i) / 1000 / 60;
                System.out.printf("Game %d- %.2f hours left\n", i, timeLeft / 60.);
                try {
                    net.export(new File(inGameRate + "_" + afterGameRate + "_" + momentum + "_" + hiddenSize + "_" + games + ".json"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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

            if(maxTurn != null) {
                state.executeTurn(maxTurn);
            }
            else {
                break;
            }
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
