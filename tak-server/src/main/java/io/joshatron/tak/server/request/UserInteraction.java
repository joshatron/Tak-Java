package io.joshatron.tak.server.request;

public class UserInteraction {

    private Auth auth;
    private String other;

    public UserInteraction(Auth auth, String other) {
        this.auth = auth;
        this.other = other;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
