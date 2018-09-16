package io.joshatron.tak.server.response;

public class RequestInfo {

    private String username;
    private boolean white;
    private boolean first;
    private int size;

    public RequestInfo(String username, boolean white, boolean first, int size) {
        this.username = username;
        this.white = white;
        this.first = first;
        this.size = size;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean getWhite() {
        return white;
    }

    public void setWhite(boolean white) {
        this.white = white;
    }

    public boolean getFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
