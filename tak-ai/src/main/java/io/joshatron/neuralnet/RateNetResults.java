package io.joshatron.neuralnet;

import io.joshatron.engine.WinReason;

public class RateNetResults {
    private int gamesWon;
    private int wonByPath;
    private int wonByPieces;
    private int wonByFull;
    private int games;

    public RateNetResults() {
        gamesWon = 0;
        wonByPath = 0;
        wonByPieces = 0;
        wonByFull = 0;
        games = 0;
    }

    public void addWin(WinReason reason) {
        switch(reason) {
            case PATH:
                wonByPath++;
                break;
            case OUT_OF_PIECES:
                wonByPieces++;
                break;
            case BOARD_FULL:
                wonByFull++;
                break;
        }

        gamesWon++;
        games++;
    }

    public void addLoss() {
        games++;
    }

    public double getWinPercentage() {
        return (double)gamesWon / games * 100.;
    }

    public double getPathPercentage() {
        return (double)wonByPath / gamesWon * 100.;
    }

    public double getPiecePercentage() {
        return (double)wonByPieces / gamesWon * 100.;
    }

    public double getFullPercentage() {
        return (double)wonByFull / gamesWon * 100.;
    }
}
