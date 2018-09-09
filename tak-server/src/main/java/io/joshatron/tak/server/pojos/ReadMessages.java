package io.joshatron.tak.server.pojos;

public class ReadMessages {

    private Auth auth;
    private String[] senders;
    private String start;
    private String read;

    public ReadMessages(Auth auth, String[] senders, String start, String read) {
        this.auth = auth;
        this.senders = senders;
        this.start = start;
        this.read = read;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public String[] getSenders() {
        return senders;
    }

    public void setSenders(String[] senders) {
        this.senders = senders;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }
}
