package io.joshatron.tak.server.response;

public class GameInfo {

    private String first;
    private String player;
    private int size;
    private String[] turns;

    public GameInfo(String first, String player, int size, String[] turns) {
        this.first = first;
        this.player = player;
        this.size = size;
        this.turns = turns;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String[] getTurns() {
        return turns;
    }

    public void setTurns(String[] turns) {
        this.turns = turns;
    }
}
