package io.joshatron.tak.server.request;

public class CancelFriendRequest {

    private Auth auth;
    private String acceptor;

    public CancelFriendRequest(Auth auth, String acceptor) {
        this.auth = auth;
        this.acceptor = acceptor;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public String getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
    }
}
