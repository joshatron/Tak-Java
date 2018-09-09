package io.joshatron.tak.server.pojos;

public class PlayTurn {

    private Auth auth;
    private String id;
    private String turn;

    public PlayTurn(Auth auth, String id, String turn) {
        this.auth = auth;
        this.id = id;
        this.turn = turn;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }
}
