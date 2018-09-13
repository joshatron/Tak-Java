package io.joshatron.tak.server.response;

public class Games {

    private int[] games;

    public Games(int[] games) {
        this.games = games;
    }

    public int[] getGames() {
        return games;
    }

    public void setGames(int[] games) {
        this.games = games;
    }
}
