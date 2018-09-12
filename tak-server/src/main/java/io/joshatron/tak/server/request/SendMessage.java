package io.joshatron.tak.server.request;

public class SendMessage {

    private Auth auth;
    private String recipient;
    private String message;

    public SendMessage(Auth auth, String recipient, String message) {
        this.auth = auth;
        this.recipient = recipient;
        this.message = message;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
