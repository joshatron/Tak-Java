package io.joshatron.tak.server.response;

public class GameRequest {

    private String username;
    private String player;
    private String first;
    private int size;
    private String id;

    public GameRequest(String username, String player, String first, int size, String id) {
        this.username = username;
        this.player = player;
        this.first = first;
        this.size = size;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
