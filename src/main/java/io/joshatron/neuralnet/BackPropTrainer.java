package io.joshatron.neuralnet;

import io.joshatron.engine.*;
import io.joshatron.player.SimpleNeuralPlayer;

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
        int boardSize;

        while(true) {
            System.out.print("What board size would you like? ");
            String input = scanner.nextLine().trim();
            try {
                int size = Integer.parseInt(input);
                if (size == 3 || size == 4 || size == 5 || size == 6 || size == 8) {
                    boardSize = size;
                    break;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

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
                                train(inGameRate, afterGameRate, momentum, hiddenSize, games, boardSize);
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
        System.out.println("Initial win rate: " + RateNet.getWinPercent(net).getWinPercentage() + "%");
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
                RateNetResults results = RateNet.getWinPercent(net);
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

    private static void playGame(FeedForwardNeuralNetwork net, double inGameRate, double afterGameRate, boolean pathOnly) {
        net.setLearningRate(inGameRate);
        GameState state = new GameState(Player.WHITE, 5);
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
