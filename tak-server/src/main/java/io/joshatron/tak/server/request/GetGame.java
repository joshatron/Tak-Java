package io.joshatron.tak.server.request;

public class GetGame {

    private Auth auth;
    private String id;

    public GetGame(Auth auth, String id) {
        this.auth = auth;
        this.id = id;
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
}
