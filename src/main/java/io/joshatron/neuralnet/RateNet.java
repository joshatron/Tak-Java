package io.joshatron.neuralnet;

import io.joshatron.engine.GameState;
import io.joshatron.engine.Player;
import io.joshatron.player.RandomPlayer;
import io.joshatron.player.SimpleNeuralPlayer;
import io.joshatron.player.TakPlayer;

import java.io.File;
import java.io.IOException;

public class RateNet {

    public static void main(String[] args) {
        System.out.println("Beginning test...");

        TakPlayer white = null;
        try {
            white = new SimpleNeuralPlayer(new FeedForwardNeuralNetwork(new File("0.005_0.1_0.001_50_10000000.json")));
        } catch (IOException e) {
            System.out.println("Can't read file");
            System.exit(1);
        }
        TakPlayer black = new RandomPlayer();
        boolean first = false;
        int netWin = 0;

        for(int i = 0; i < 100; i++) {
            first = !first;
            GameState state = new GameState(first, 5);
            while(!state.checkForWinner().isFinished()) {
                if(state.isWhiteTurn()) {
                    state.executeTurn(white.getTurn(state));
                }
                else {
                    state.executeTurn(black.getTurn(state));
                }
            }

            if(state.checkForWinner().getWinner() == Player.WHITE) {
                netWin++;
            }
        }

        System.out.println("The net won " + netWin + "% of the time");
    }
}
