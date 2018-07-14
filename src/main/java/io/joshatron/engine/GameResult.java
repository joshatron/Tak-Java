package io.joshatron.engine;

public class GameResult {
    private boolean finished;
    private Player winner;
    private WinReason reason;

    public GameResult(boolean finished, Player winner, WinReason reason) {
        this.finished = finished;
        this.winner = winner;
        this.reason = reason;
    }

    public GameResult() {
        finished = false;
        winner = Player.NONE;
        reason = WinReason.NONE;
    }

    public boolean isFinished() {
        return finished;
    }

    public Player getWinner() {
        return winner;
    }

    public WinReason getReason() {
        return reason;
    }
}
