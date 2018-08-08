package io.joshatron.tak.ai.neuralnet;

public class NetWithStats {

    private double inGameRate;
    private double afterGameRate;
    private double momentum;
    private int hiddenSize;
    private int games;
    private int boardSize;
    private String label;
    private int wins;
    private int played;
    private FeedForwardNeuralNetwork net;


    public NetWithStats(double inGameRate, double afterGameRate, double momentum, int hiddenSize, int games, int boardSize, String label, FeedForwardNeuralNetwork net) {
        this.inGameRate = inGameRate;
        this.afterGameRate = afterGameRate;
        this.momentum = momentum;
        this.hiddenSize = hiddenSize;
        this.games = games;
        this.boardSize = boardSize;
        this .label = label;
        this.net = net;
        wins = 0;
        played = 0;
    }

    public void addWin() {
        wins++;
        played++;
    }

    public void addLoss() {
        played++;
    }

    public double getInGameRate() {
        return inGameRate;
    }

    public double getAfterGameRate() {
        return afterGameRate;
    }

    public double getMomentum() {
        return momentum;
    }

    public int getHiddenSize() {
        return hiddenSize;
    }

    public int getGames() {
        return games;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public String getLabel() {
        return label;
    }

    public int getWins() {
        return wins;
    }

    public int getPlayed() {
        return played;
    }

    public double getWinRate() {
        return wins / (double)played;
    }

    public FeedForwardNeuralNetwork getNet() {
        return net;
    }

    public String toString() {
        return "In game rate: " + inGameRate + "\n" +
               "After game rate: " + afterGameRate + "\n" +
               "Momentum: " + momentum + "\n" +
               "Hidden size: " + hiddenSize + "\n" +
               "Games: " + games + "\n" +
               "Win rate: " + getWinRate();
    }
}
