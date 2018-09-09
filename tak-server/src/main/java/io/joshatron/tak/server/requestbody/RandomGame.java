package io.joshatron.tak.server.requestbody;

public class RandomGame {

    private Auth auth;
    private int size;

    public RandomGame(Auth auth, int size) {
        this.auth = auth;
        this.size = size;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
