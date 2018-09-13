package io.joshatron.tak.server.response;

public class GameRequests {

    private RequestInfo[] games;

    public GameRequests(RequestInfo[] games) {
        this.games = games;
    }

    public RequestInfo[] getGames() {
        return games;
    }

    public void setGames(RequestInfo[] games) {
        this.games = games;
    }
}
