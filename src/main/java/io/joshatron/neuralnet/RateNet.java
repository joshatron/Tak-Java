package io.joshatron.neuralnet;

import io.joshatron.engine.GameState;
import io.joshatron.engine.Player;
import io.joshatron.engine.Turn;
import io.joshatron.player.RandomPlayer;
import io.joshatron.player.SimpleNeuralPlayer;
import io.joshatron.player.TakPlayer;

import java.io.File;
import java.io.IOException;

public class RateNet {

    public static void main(String[] args) {
        System.out.println("Beginning test...");
        try {
            int netWin = getWinPercent(new FeedForwardNeuralNetwork(new File("0.005_0.01_0.0_25_500000.json")));
            System.out.println("The net won " + netWin + "% of the time");
        } catch (IOException e) {
            System.out.println("Failed to find net");
            e.printStackTrace();
        }
    }

    //Returns a value between 0 and 100
    //Represents the win percentage against a random player
    public static int getWinPercent(FeedForwardNeuralNetwork net) {
        TakPlayer white = new SimpleNeuralPlayer(net);
        TakPlayer black = new RandomPlayer();
        Player first = Player.BLACK;
        int netWin = 0;

        for(int i = 0; i < 100; i++) {
            if(first == Player.WHITE) {
                first = Player.BLACK;
            }
            else {
                first = Player.WHITE;
            }
            GameState state = new GameState(first, 5);
            while(!state.checkForWinner().isFinished()) {
                state.printBoard();
                if(state.isWhiteTurn()) {
                    Turn turn = white.getTurn((GameState)state.clone());
                    System.out.println(turn);
                    state.executeTurn(turn);
                }
                else {
                    Turn turn = black.getTurn((GameState)state.clone());
                    System.out.println(turn);
                    state.executeTurn(turn);
                }
            }

            if(state.checkForWinner().getWinner() == Player.WHITE) {
                netWin++;
            }
        }

        return netWin;
    }
}
