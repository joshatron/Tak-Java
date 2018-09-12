package io.joshatron.tak.server.request;

public class Unblock {

    private Auth auth;
    private String unblock;

    public Unblock(Auth auth, String unblock) {
        this.auth = auth;
        this.unblock = unblock;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public String getUnblock() {
        return unblock;
    }

    public void setUnblock(String unblock) {
        this.unblock = unblock;
    }
}
