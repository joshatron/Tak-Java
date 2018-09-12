package io.joshatron.tak.server.request;

public class AuthWrapper {

    private Auth auth;

    public AuthWrapper(Auth auth) {
        this.auth = auth;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }
}
