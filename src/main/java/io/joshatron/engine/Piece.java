package io.joshatron.engine;

public class Piece {

    private boolean white;
    private PieceType type;

    public Piece(boolean white, PieceType type) {
        this.white = white;
        this.type = type;
    }

    public boolean isWhite() {
        return white;
    }

    public boolean isBlack() {
        return !white;
    }

    public PieceType getType() {
        return type;
    }
}
