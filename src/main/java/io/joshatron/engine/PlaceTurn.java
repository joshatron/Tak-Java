package io.joshatron.engine;

public class PlaceTurn extends Turn {

    private BoardLocation location;
    private PieceType pieceType;

    public PlaceTurn(TurnType type, BoardLocation location, PieceType pieceType) {
        super(type);
        this.location = location;
        this.pieceType = pieceType;
    }

    public BoardLocation getLocation() {
        return location;
    }

    public PieceType getPieceType() {
        return pieceType;
    }
}
