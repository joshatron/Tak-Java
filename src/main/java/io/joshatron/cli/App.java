package io.joshatron.cli;

import com.sun.xml.internal.ws.util.StringUtils;
import io.joshatron.neuralnet.FeedForwardNeuralNetwork;
import io.joshatron.player.*;
import io.joshatron.engine.*;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        int games = -1;
        int size = -1;
        boolean whiteFirst = true;
        Player whitePlayer = null;
        Player blackPlayer = null;

        System.out.println("---------------------");
        System.out.println("| Welcome to TakCLI |");
        System.out.println("---------------------");
        System.out.println();
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
            String input = StringUtils.decapitalize(reader.nextLine());
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
            String input = StringUtils.decapitalize(reader.nextLine());
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
            String input = StringUtils.decapitalize(reader.nextLine());
            if(input.charAt(0) == 'h') {
                blackPlayer = new HumanPlayer(reader);
                break;
            }
            else if(input.charAt(0) == 'a') {
                try {
                    blackPlayer = new MiniMaxPlayer(new FeedForwardNeuralNetwork(new File("0.005_0.05_0.0_50_5000000.json")));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //blackPlayer = new RandomPlayer();
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
            int result = -1;
            result = playGame(whiteFirst, size, whitePlayer, blackPlayer);
            if(result == 1) {
                whiteWins++;
            }
            else if(result == 2) {
                blackWins++;
            }
            else if(result == 0) {
                System.exit(0);
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
    //Returns a boolean whether white won
    private static int playGame(boolean whiteFirst, int size, Player whitePlayer, Player blackPlayer) {
        System.out.println("Initializing game...");
        GameState state = new GameState(whiteFirst, size);


        while(state.checkForWinner() == 0) {
            if(state.isWhiteTurn()) {
                Turn turn = whitePlayer.getTurn(state);
                if(turn == null) {
                    System.out.println("White surrenders!");
                    return 0;
                }
                state.executeTurn(turn);
                System.out.println("White played: " + turn.toString());
            }
            else {
                Turn turn = blackPlayer.getTurn(state);
                if(turn == null) {
                    System.out.println("Black surrenders!");
                    return 0;
                }
                state.executeTurn(turn);
                System.out.println("Black played: " + turn.toString());
            }
        }

        state.printBoard();

        if(state.checkForWinner() == 1) {
            System.out.println("White wins!");
            return 1;
        }
        else if(state.checkForWinner() == 2) {
            System.out.println("Black wins!");
            return 2;
        }
        else {
            System.out.println("It's a tie!");
            return 3;
        }
    }
}
