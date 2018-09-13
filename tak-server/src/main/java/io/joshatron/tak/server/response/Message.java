package io.joshatron.tak.server.response;

public class Message {

    private String user;
    private String timestamp;
    private String message;

    public Message(String user, String timestamp, String message) {
        this.user = user;
        this.timestamp = timestamp;
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
