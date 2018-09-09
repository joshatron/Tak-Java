package io.joshatron.tak.server.requestbody;

public class PassChange {

    private Auth auth;
    private String updated;

    public PassChange(Auth auth, String updated) {
        this.auth = auth;
        this.updated = updated;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
