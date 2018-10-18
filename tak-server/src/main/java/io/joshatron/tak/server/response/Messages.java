package io.joshatron.tak.server.response;

public class Messages {

    private Message[] messages;

    public Messages(Message[] messages) {
        this.messages = messages;
    }

    public Messages() {
        messages = new Message[0];
    }

    public Message[] getMessages() {
        return messages;
    }

    public void setMessages(Message[] messages) {
        this.messages = messages;
    }
}
