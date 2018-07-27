package io.joshatron.neuralnet;

import io.joshatron.engine.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class BackPropTrainer {

    public static void runTrainingSet() {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Double> inGameRates = new ArrayList<>();
        ArrayList<Double> afterGameRates = new ArrayList<>();
        ArrayList<Double> momentums = new ArrayList<>();
        ArrayList<Integer> hiddenSizes = new ArrayList<>();
        ArrayList<Integer> gameLengths = new ArrayList<>();

        System.out.println("What in games rates do you want? (leave blank to move on)");
        while(true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if(input.length() == 0) {
                break;
            }
            try {
                double inGameRate = Double.parseDouble(input);
                inGameRates.add(inGameRate);
            }
            catch(Exception e) {
                System.out.println("Invalid number. Please enter a valid floating point number.");
            }
        }
        System.out.println("What after games rates do you want? (leave blank to move on)");
        while(true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if(input.length() == 0) {
                break;
            }
            try {
                double afterGameRate = Double.parseDouble(input);
                afterGameRates.add(afterGameRate);
            }
            catch(Exception e) {
                System.out.println("Invalid number. Please enter a valid floating point number.");
            }
        }
        System.out.println("What momentums do you want? (leave blank to move on)");
        while(true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if(input.length() == 0) {
                break;
            }
            try {
                double momentum = Double.parseDouble(input);
                momentums.add(momentum);
            }
            catch(Exception e) {
                System.out.println("Invalid number. Please enter a valid floating point number.");
            }
        }
        System.out.println("What hidden layer sizes do you want? (leave blank to move on)");
        while(true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if(input.length() == 0) {
                break;
            }
            try {
                int hidden = Integer.parseInt(input);
                hiddenSizes.add(hidden);
            }
            catch(Exception e) {
                System.out.println("Invalid number. Please enter a valid integer.");
            }
        }
        System.out.println("What game rounds do you want? (leave blank to move on)");
        while(true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if(input.length() == 0) {
                break;
            }
            try {
                int games = Integer.parseInt(input);
                gameLengths.add(games);
            }
            catch(Exception e) {
                System.out.println("Invalid number. Please enter a valid integer.");
            }
        }
        if(inGameRates.size() > 0 && afterGameRates.size() > 0 && momentums.size() > 0 &&
           hiddenSizes.size() > 0 && gameLengths.size() > 0) {
            for(double inGameRate : inGameRates) {
                for(double afterGameRate : afterGameRates) {
                    for(double momentum : momentums) {
                        for(int hiddenSize : hiddenSizes) {
                            for(int games : gameLengths) {
                                train(inGameRate, afterGameRate, momentum, hiddenSize, games);
                            }
                        }
                    }
                }
            }
        }
        else {
            System.out.println("Invalid parameters. Quitting.");
        }

    }

    public static void train(double inGameRate, double afterGameRate, double momentum,
                             int hiddenSize, int games) {
        System.out.println("Initializing training...");

        FeedForwardNeuralNetwork net = new FeedForwardNeuralNetwork(1, new int[]{94, hiddenSize, 2}, ActivationFunction.LOGISTIC, momentum, inGameRate);

        System.out.println("Training with the following parameters:");
        System.out.println("In game rate: " + inGameRate);
        System.out.println("After game rate: " + afterGameRate);
        System.out.println("Momentum: " + momentum);
        System.out.println("Hidden size: " + hiddenSize);
        System.out.println("Initial win rate: " + RateNet.getWinPercent(net) + "%");
        System.out.println();

        long firstTime = new Date().getTime();

        for (int i = 0; i < games; i++) {
            if (i < (games * 3) / 4) {
                playGame(net, inGameRate, afterGameRate, true);
            } else {
                playGame(net, inGameRate, afterGameRate, false);
            }

            if (i % 1000 == 0 && i != 0) {
                long thisTime = new Date().getTime();
                long timeLeft = (games - i) * ((thisTime - firstTime) / i) / 1000 / 60;
                int winPercent = RateNet.getWinPercent(net);
                System.out.printf("Game %d- %d%% win rate, %d:%02d remaining\n", i, winPercent, timeLeft / 60, timeLeft % 60);
                try {
                    net.export(new File(inGameRate + "_" + afterGameRate + "_" + momentum + "_" + hiddenSize + "_" + games + ".json"));
                } catch (Exception e) {
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

    private static void playGame(FeedForwardNeuralNetwork net, double inGameRate, double afterGameRate, boolean pathOnly) {
        net.setLearningRate(inGameRate);
        GameState state = new GameState(Player.WHITE, 5);

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
