package io.joshatron.tak.server.request;

public class FriendRequest {

    private Auth auth;
    private String friend;

    public FriendRequest(Auth auth, String friend) {
        this.auth = auth;
        this.friend = friend;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }
}
