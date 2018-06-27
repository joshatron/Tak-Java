package io.joshatron.engine;

public class BoardLocation {

    private int x;
    private int y;

    public BoardLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(Direction direction) {
        switch(direction) {
            case NORTH:
                y -= 1;
                break;
            case SOUTH:
                y += 1;
                break;
            case EAST:
                x += 1;
                break;
            case WEST:
                x -= 1;
                break;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
