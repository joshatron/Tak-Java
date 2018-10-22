package io.joshatron.tak.server.request;

import java.util.Date;

public class ReadMessages {

    private Auth auth;
    private String[] senders;
    private Date start;
    private boolean read;

    public ReadMessages(Auth auth, String[] senders, long start, boolean read) {
        this.auth = auth;
        this.senders = senders;
        this.start = new Date(start);
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

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setStart(long start) {
        this.start = new Date(start);
    }

    public boolean getRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
