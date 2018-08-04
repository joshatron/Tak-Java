package io.joshatron.cli;

import io.joshatron.neuralnet.*;
import org.jline.builtins.Completers;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.TerminalBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Trainer {

    public static void setupTraining() throws IOException {
        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(TerminalBuilder.terminal())
                .completer(new StringsCompleter("train", "set train", "rate", "compare"))
                .build();
        while(true) {
            String input = lineReader.readLine("What would you like to do? ").trim().toLowerCase();
            if(input.equals("train")) {
                runTraining();
                break;
            }
            else if(input.equals("set train")) {
                runTrainingSet();
                break;
            }
            else if(input.equals("rate")) {
                rateNet();
                break;
            }
            else if(input.equals("compare")) {
                CompareNets.compareAllNets();
                break;
            }
            else {
                System.out.println("Please choose a valid option.");
            }
        }
    }

    private static void rateNet() throws IOException {
        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(TerminalBuilder.terminal())
                .completer(new Completers.FilesCompleter(new File("")))
                .build();

        while(true) {
            String input = lineReader.readLine("What net would you like to rate (type exit to quit)? ").trim();
            if(input.toLowerCase().equals("exit")) {
                break;
            }
            RateNetResults results = RateNet.getWinPercent(new FeedForwardNeuralNetwork(new File(input)), Integer.parseInt(input.substring(0,1)));
            System.out.println("The net won " + results.getWinPercentage() + "% of the time");
        }
    }

    private static void runTraining() throws IOException {
        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(TerminalBuilder.terminal())
                .completer(new NullCompleter())
                .build();
        double inGameRate;
        double afterGameRate;
        double momentum;
        int hiddenSize;
        int games;
        int boardSize;

        while(true) {
            String input = lineReader.readLine("What board size would you like? ").trim();
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
        while(true) {
            String input = lineReader.readLine("What in game rate would you like? ").trim();
            try {
                inGameRate = Double.parseDouble(input);
                break;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        while(true) {
            String input = lineReader.readLine("What after game rate would you like? ").trim();
            try {
                afterGameRate = Double.parseDouble(input);
                break;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        while(true) {
            String input = lineReader.readLine("What momentum would you like? ").trim();
            try {
                momentum = Double.parseDouble(input);
                break;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        while(true) {
            String input = lineReader.readLine("How many hidden nodes would you like? ").trim();
            try {
                hiddenSize = Integer.parseInt(input);
                break;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        while(true) {
            String input = lineReader.readLine("How many games do you want to run? ").trim();
            try {
                games = Integer.parseInt(input);
                break;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        BackPropTrainer.train(inGameRate, afterGameRate, momentum, hiddenSize, games, boardSize);
    }

    private static void runTrainingSet() throws IOException {
        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(TerminalBuilder.terminal())
                .completer(new NullCompleter())
                .build();
        ArrayList<Double> inGameRates = new ArrayList<>();
        ArrayList<Double> afterGameRates = new ArrayList<>();
        ArrayList<Double> momentums = new ArrayList<>();
        ArrayList<Integer> hiddenSizes = new ArrayList<>();
        ArrayList<Integer> gameLengths = new ArrayList<>();
        int boardSize;

        while(true) {
            String input = lineReader.readLine("What board size would you like? ").trim();
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
            String input = lineReader.readLine("> ").trim();
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
            String input = lineReader.readLine("> ").trim();
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
            String input = lineReader.readLine("> ").trim();
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
            String input = lineReader.readLine("> ").trim();
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
            String input = lineReader.readLine("> ").trim();
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
                                BackPropTrainer.train(inGameRate, afterGameRate, momentum, hiddenSize, games, boardSize);
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
}
