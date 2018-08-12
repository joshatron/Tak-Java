package io.joshatron.tak.ai.cli;

import io.joshatron.tak.ai.neuralnet.*;
import org.jline.builtins.Completers;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.TerminalBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class App {

    public static void main(String[] args) {
        try {
            LineReader lineReader = LineReaderBuilder.builder()
                    .terminal(TerminalBuilder.terminal())
                    .completer(new StringsCompleter("train", "set train", "rate", "compare", "help", "exit"))
                    .build();

            System.out.println("--------------------");
            System.out.println("| Welcome to TakAI |");
            System.out.println("--------------------");
            System.out.println();

            while(true) {
                String input = lineReader.readLine("> ").trim().toLowerCase();
                if(input.equals("train")) {
                    runTraining();
                }
                else if(input.equals("set train")) {
                    runTrainingSet();
                }
                else if(input.equals("rate")) {
                    rateNet();
                }
                else if(input.equals("compare")) {
                    CompareNets.compareAllNets();
                }
                else if(input.equals("help")) {
                    System.out.println("The following is a list of what you can do:");
                    System.out.println("  train- prompts for learning parameters, then runs the backprop trainer.");
                    System.out.println("  set train- prompts for sets of learning parameters, then iterates through");
                    System.out.println("    every possibility of those with the backprop trainer.");
                    System.out.println("  rate- specify a net and it finds the percentage of the time it beats a random player.");
                    System.out.println("  compare- runs all the nets against each other in a round robin tournament.");
                    System.out.println("  help- displays this help message.");
                    System.out.println("  exit- exits the program.");
                }
                else if(input.equals("exit")) {
                    System.exit(0);
                }
                else {
                    System.out.println("Please choose a valid option. Type help to see options.");
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
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
            System.out.println(results.getPathPercentage() + "% by path, " +
                    results.getPiecePercentage() + "% by out of pieces, and " +
                    results.getFullPercentage() + "% by board full");
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
            String input = lineReader.readLine("What board size would you like? ").toLowerCase().trim();
            if(input.equals("exit")) {
                System.exit(0);
            }
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
            String input = lineReader.readLine("What in game rate would you like? ").toLowerCase().trim();
            if(input.equals("exit")) {
                System.exit(0);
            }
            try {
                inGameRate = Double.parseDouble(input);
                break;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        while(true) {
            String input = lineReader.readLine("What after game rate would you like? ").toLowerCase().trim();
            if(input.equals("exit")) {
                System.exit(0);
            }
            try {
                afterGameRate = Double.parseDouble(input);
                break;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        while(true) {
            String input = lineReader.readLine("What momentum would you like? ").toLowerCase().trim();
            if(input.equals("exit")) {
                System.exit(0);
            }
            try {
                momentum = Double.parseDouble(input);
                break;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        while(true) {
            String input = lineReader.readLine("How many hidden nodes would you like? ").toLowerCase().trim();
            if(input.equals("exit")) {
                System.exit(0);
            }
            try {
                hiddenSize = Integer.parseInt(input);
                break;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        while(true) {
            String input = lineReader.readLine("How many games do you want to run? ").toLowerCase().trim();
            if(input.equals("exit")) {
                System.exit(0);
            }
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
            String input = lineReader.readLine("What board size would you like? ").toLowerCase().trim();
            if(input.equals("exit")) {
                System.exit(0);
            }
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
            String input = lineReader.readLine("> ").toLowerCase().trim();
            if(input.equals("exit")) {
                System.exit(0);
            }
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
            String input = lineReader.readLine("> ").toLowerCase().trim();
            if(input.equals("exit")) {
                System.exit(0);
            }
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
            String input = lineReader.readLine("> ").toLowerCase().trim();
            if(input.equals("exit")) {
                System.exit(0);
            }
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
            String input = lineReader.readLine("> ").toLowerCase().trim();
            if(input.equals("exit")) {
                System.exit(0);
            }
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
            String input = lineReader.readLine("> ").toLowerCase().trim();
            if(input.equals("exit")) {
                System.exit(0);
            }
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
