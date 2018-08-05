package io.joshatron.neuralnet;

import io.joshatron.engine.GameResult;
import io.joshatron.engine.GameState;
import io.joshatron.engine.Player;
import io.joshatron.engine.Turn;
import io.joshatron.player.RandomPlayer;
import io.joshatron.player.SimpleNeuralPlayer;
import io.joshatron.player.TakPlayer;

import java.io.File;
import java.io.IOException;

public class RateNet {

    //Returns a value between 0 and 100
    //Represents the win percentage against a random player
    public static RateNetResults getWinPercent(FeedForwardNeuralNetwork net, int boardSize) {
        TakPlayer white = new SimpleNeuralPlayer(net);
        TakPlayer black = new RandomPlayer();
        Player first = Player.BLACK;
        RateNetResults results = new RateNetResults();

        for(int i = 0; i < 100; i++) {
            if(first == Player.WHITE) {
                first = Player.BLACK;
            }
            else {
                first = Player.WHITE;
            }
            GameState state = new GameState(first, boardSize);
            while(!state.checkForWinner().isFinished()) {
                if(state.isWhiteTurn()) {
                    Turn turn = white.getTurn((GameState)state.clone());
                    state.executeTurn(turn);
                }
                else {
                    Turn turn = black.getTurn((GameState)state.clone());
                    state.executeTurn(turn);
                }
            }

            GameResult gameResult = state.checkForWinner();
            if(gameResult.getWinner() == Player.WHITE) {
                results.addWin(gameResult.getReason());
            }
            else {
                results.addLoss();
            }
        }

        return results;
    }
}
