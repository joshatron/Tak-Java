package io.joshatron.tak.server.response;

public class GameTurn {

    private String turn;
    private int order;

    public GameTurn(String turn, int order) {
        this.turn = turn;
        this.order = order;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
