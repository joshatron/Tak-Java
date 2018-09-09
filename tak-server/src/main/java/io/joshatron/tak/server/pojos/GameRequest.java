package io.joshatron.tak.server.pojos;

public class GameRequest {

    private Auth auth;
    private String opponent;
    private int size;
    private String color;
    private String first;

    public GameRequest(Auth auth, String opponent, int size, String color, String first) {
        this.auth = auth;
        this.opponent = opponent;
        this.size = size;
        this.color = color;
        this.first = first;
    }

    public boolean isFirst() {
        return first.toLowerCase().equals("true");
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }
}
