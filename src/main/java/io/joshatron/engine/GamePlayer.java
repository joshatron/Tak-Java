package io.joshatron.engine;

import io.joshatron.player.HumanPlayer;
import io.joshatron.player.RandomPlayer;
import io.joshatron.player.TakPlayer;

import java.util.Scanner;

public class GamePlayer
{
    public static void runGameSet() {
        Scanner reader = new Scanner(System.in);
        int games;
        int size;
        boolean whiteFirst;
        TakPlayer whitePlayer;
        TakPlayer blackPlayer;

        while(true) {
            System.out.print("How many games would you like to play? ");
            String input = reader.nextLine();
            try {
                games = Integer.parseInt(input);
                break;
            }
            catch(Exception e) {
                System.out.println("Invalid number. Please enter a valid number greater than 0.");
            }
        }
        while(true) {
            System.out.print("What size board would you like to play on? ");
            String input = reader.nextLine();
            try {
                size = Integer.parseInt(input);
                break;
            }
            catch(Exception e) {
                System.out.println("Invalid number Please enter 3, 4, 5, 6, or 8.");
            }
        }
        while(true) {
            System.out.print("Who goes first, white or black? ");
            String input = reader.nextLine().toLowerCase();
            if(input.charAt(0) == 'w') {
                whiteFirst = true;
                break;
            }
            else if(input.charAt(0) == 'b') {
                whiteFirst = false;
                break;
            }
            else {
                System.out.println("Invalid input. Please enter black or white.");
            }
        }
        while(true) {
            System.out.print("Is white a human or AI? ");
            String input = reader.nextLine().toLowerCase();
            if(input.charAt(0) == 'h') {
                whitePlayer = new HumanPlayer(reader);
                break;
            }
            else if(input.charAt(0) == 'a') {
                /*try {
                    whitePlayer = new SimpleNeuralPlayer(new FeedForwardNeuralNetwork(new File("0.05_0.005_0.001_100_1000000.json")));
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                whitePlayer = new RandomPlayer();
                break;
            }
            else {
                System.out.println("Invalid input. Please enter human or ai.");
            }
        }
        while(true) {
            System.out.print("Is black a human or AI? ");
            String input = reader.nextLine().toLowerCase();
            if(input.charAt(0) == 'h') {
                blackPlayer = new HumanPlayer(reader);
                break;
            }
            else if(input.charAt(0) == 'a') {
                /*try {
                    blackPlayer = new SimpleNeuralPlayer(new FeedForwardNeuralNetwork(new File("0.005_0.1_0.001_50_10000000.json")));
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                blackPlayer = new RandomPlayer();
                break;
            }
            else {
                System.out.println("Invalid input. Please enter human or ai.");
            }
        }

        int whiteWins = 0;
        int blackWins = 0;
        for(int i = 0; i < games; i++) {
            System.out.println("----------");
            System.out.println("| Game " + (i + 1) + " |");
            System.out.println("----------");
            GameResult result = playGame(whiteFirst, size, whitePlayer, blackPlayer);
            if(result.getReason() == WinReason.SURRENDER) {
                System.exit(0);
            }
            else if(result.getWinner() == Player.WHITE) {
                whiteWins++;
            }
            else if(result.getWinner() == Player.BLACK) {
                blackWins++;
            }
            whiteFirst = !whiteFirst;
        }

        if(whiteWins > blackWins) {
            System.out.println("White is the winner " + whiteWins + ":" + blackWins);
        }
        else if(whiteWins < blackWins) {
            System.out.println("Black is the winner " + blackWins + ":" + whiteWins);
        }
        else {
            System.out.println("It's a tie! Both players won the same number of games.");
        }
    }

    //Plays game with the given conditions
    public static GameResult playGame(boolean whiteFirst, int size, TakPlayer whitePlayer, TakPlayer blackPlayer) {
        System.out.println("Initializing game...");
        GameState state = new GameState(whiteFirst, size);


        while(!state.checkForWinner().isFinished()) {
            if(state.isWhiteTurn()) {
                Turn turn = whitePlayer.getTurn(state);
                if(turn == null) {
                    System.out.println("White surrenders!");
                    return new GameResult(true, Player.BLACK, WinReason.SURRENDER);
                }
                state.executeTurn(turn);
                System.out.println("White played: " + turn.toString());
            }
            else {
                Turn turn = blackPlayer.getTurn(state);
                if(turn == null) {
                    System.out.println("Black surrenders!");
                    return new GameResult(true, Player.WHITE, WinReason.SURRENDER);
                }
                state.executeTurn(turn);
                System.out.println("Black played: " + turn.toString());
            }
        }

        state.printBoard();
        GameResult result = state.checkForWinner();

        if(result.getWinner() == Player.WHITE) {
            switch(result.getReason()) {
                case OUT_OF_PIECES:
                    System.out.println("White wins by out of pieces!");
                    break;
                case BOARD_FULL:
                    System.out.println("White wins by board full!");
                    break;
                case PATH:
                    System.out.println("White wins by path!");
                    break;
            }
        }
        else if(result.getWinner() == Player.BLACK) {
            switch(result.getReason()) {
                case OUT_OF_PIECES:
                    System.out.println("Black wins by out of pieces!");
                    break;
                case BOARD_FULL:
                    System.out.println("Black wins by board full!");
                    break;
                case PATH:
                    System.out.println("Black wins by path!");
                    break;
            }
        }
        else {
            switch(result.getReason()) {
                case OUT_OF_PIECES:
                    System.out.println("It's a tie by out of pieces!");
                    break;
                case BOARD_FULL:
                    System.out.println("It's a tie by board full!");
                    break;
            }
        }

        return result;
    }
}
