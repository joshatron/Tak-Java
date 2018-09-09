package io.joshatron.tak.server.pojos;

public class ListCompleted {

    private Auth auth;
    private String[] opponents;
    private String start;
    private int size;

    public ListCompleted(Auth auth, String[] opponents, String start, int size) {
        this.auth = auth;
        this.opponents = opponents;
        this.start = start;
        this.size = size;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public String[] getOpponents() {
        return opponents;
    }

    public void setOpponents(String[] opponents) {
        this.opponents = opponents;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
