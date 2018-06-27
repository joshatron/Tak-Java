package io.joshatron.engine;

public class MoveTurn extends Turn {

    private BoardLocation startLocation;
    private int pickedUp;
    private Direction direction;
    private int[] placed;

    public MoveTurn(TurnType type, BoardLocation startLocation, int pickedUp,
                    Direction direction, int[] placed) {
        super(type);
        this.startLocation = startLocation;
        this.pickedUp = pickedUp;
        this.direction = direction;
        this.placed = placed;
    }

    public BoardLocation getStartLocation() {
        return startLocation;
    }

    public int getPickedUp() {
        return pickedUp;
    }

    public Direction getDirection() {
        return direction;
    }

    public int[] getPlaced() {
        return placed;
    }
}
