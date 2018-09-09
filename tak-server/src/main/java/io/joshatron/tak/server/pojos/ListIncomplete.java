package io.joshatron.tak.server.pojos;

public class ListIncomplete {

    private Auth auth;
    private String pending;

    public ListIncomplete(Auth auth, String pending) {
        this.auth = auth;
        this.pending = pending;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }
}
