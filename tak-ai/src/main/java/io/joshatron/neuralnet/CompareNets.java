package io.joshatron.neuralnet;

import io.joshatron.engine.GameState;
import io.joshatron.engine.Player;
import io.joshatron.player.SimpleNeuralPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;

/*
 * The goal of this class is to compare the different nets to each other to see which is best
 * It looks for all json files and runs a round robin tournament, then prints the results
 */
public class CompareNets {

    public static void compareAllNets() {
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

        if(nets.size() == 0) {
            System.out.println("No nets to test. Quitting");
            return;
        }

        //run round robin tournament each player plays each other as both black and white. White always goes first
        for(NetWithStats white : nets) {
            for(NetWithStats black : nets) {
                //don't play against yourself
                if(white != black) {
                    GameState state = new GameState(Player.WHITE, white.getBoardSize());
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

        int best = 0;
        NetWithStats bestNet = nets.get(0);
        //display results
        for(NetWithStats net : nets) {
            System.out.println(net);
            System.out.println();

            if(net.getWins() > best) {
                best = net.getWins();
                bestNet = net;
            }
        }

        System.out.println("Best net:");
        System.out.println(bestNet);
        System.out.println();

        //run the analysis
        ArrayList<Analysis> inGameRate = new ArrayList<>();
        ArrayList<Analysis> afterGameRate = new ArrayList<>();

        for(NetWithStats net : nets) {
            //in game rate
            int index = inGameRate.indexOf(new Analysis(Double.toString(net.getInGameRate())));

            if(index == -1) {
                Analysis a = new Analysis(Double.toString(net.getInGameRate()));
                a.addValue(net.getWins());
                inGameRate.add(a);
            }
            else {
                inGameRate.get(index).addValue(net.getWins());
            }

            //after game rate
            index = afterGameRate.indexOf(new Analysis(Double.toString(net.getAfterGameRate())));

            if(index == -1) {
                Analysis a = new Analysis(Double.toString(net.getAfterGameRate()));
                a.addValue(net.getWins());
                afterGameRate.add(a);
            }
            else {
                afterGameRate.get(index).addValue(net.getWins());
            }
        }

        inGameRate.sort(Comparator.comparing(Analysis::getName));
        afterGameRate.sort(Comparator.comparing(Analysis::getName));

        System.out.println("In game rates");
        for(Analysis inGame : inGameRate) {
            System.out.println(inGame.getName() + ": " + inGame.getAverage());
        }
        System.out.println();
        System.out.println("After game rates");
        for(Analysis afterGame : afterGameRate) {
            System.out.println(afterGame.getName() + ": " + afterGame.getAverage());
        }
    }
}
