package io.joshatron.tak.server.request;

public class GameResponse {

    private Auth auth;
    private String id;
    private String response;


    public GameResponse(Auth auth, String id, String response) {
        this.auth = auth;
        this.id = id;
        this.response = response;
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

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
