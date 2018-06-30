package io.joshatron.engine;

public class PlaceTurn extends Turn {

    private BoardLocation location;
    private PieceType pieceType;

    public PlaceTurn(BoardLocation location, PieceType pieceType) {
        super(TurnType.PLACE);
        this.location = location;
        this.pieceType = pieceType;
    }

    public PlaceTurn(int x, int y, PieceType pieceType) {
        super(TurnType.PLACE);
        this.location = new BoardLocation(x, y);
        this.pieceType = pieceType;
    }

    public BoardLocation getLocation() {
        return location;
    }

    public PieceType getPieceType() {
        return pieceType;
    }
}
