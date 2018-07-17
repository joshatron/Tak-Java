package io.joshatron.neuralnet;

import io.joshatron.engine.GameState;
import io.joshatron.engine.Player;
import io.joshatron.player.SimpleNeuralPlayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/*
 * The goal of this class is to compare the different nets to each other to see which is best
 * It looks for all json files and runs a round robin tournament, then prints the results
 */
public class CompareNets {

    public static void main(String[] args) {
        ArrayList<NetWithStats> nets = new ArrayList<>();

        //Collect all networks
        final File netFolder = new File("./");
        for(final File file : netFolder.listFiles()) {
            if(file.isDirectory()) {
                continue;
            }

            if(file.getName().endsWith(".json")) {
                nets.add(NetUtils.getNetFromFile(file));
            }
        }

        //run round robin tournament each player plays each other as both black and white. White always goes first
        for(NetWithStats white : nets) {
            for(NetWithStats black : nets) {
                //don't play against yourself
                if(white != black) {
                    GameState state = new GameState(true, 5);
                    SimpleNeuralPlayer whitePlayer = new SimpleNeuralPlayer(white.getNet());
                    SimpleNeuralPlayer blackPlayer = new SimpleNeuralPlayer(black.getNet());
                    while (!state.checkForWinner().isFinished()) {
                        if (state.isWhiteTurn()) {
                            state.executeTurn(whitePlayer.getTurn(state));
                        } else {
                            state.executeTurn(blackPlayer.getTurn(state));
                        }
                    }

                    if(state.checkForWinner().getWinner() == Player.WHITE) {
                        white.addWin();
                        black.addLoss();
                    }
                    else if(state.checkForWinner().getWinner() == Player.BLACK) {
                        white.addLoss();
                        black.addWin();
                    }
                    else {
                        white.addLoss();
                        black.addLoss();
                    }
                }
            }
        }

        //TODO: run analysis on effect of different factors

        //display results
        for(NetWithStats net : nets) {
            System.out.println(net);
            System.out.println();
        }
    }
}
