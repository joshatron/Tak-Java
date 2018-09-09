package io.joshatron.tak.server.requestbody;

public class FriendResponse {

    private Auth auth;
    private String friend;
    private String response;

    public FriendResponse(Auth auth, String friend, String response) {
        this.auth = auth;
        this.friend = friend;
        this.response = response;
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

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
