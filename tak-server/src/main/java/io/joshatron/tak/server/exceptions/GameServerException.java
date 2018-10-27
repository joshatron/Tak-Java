package io.joshatron.tak.server.exceptions;

public class GameServerException extends Exception {

    public GameServerException() {
        super("The server encountered an error.");
    }

    public GameServerException(String message) {
        super(message);
    }

    public String getJsonMessage() {
        return "{\"reason\": \"" + getMessage() + "\"}";
    }
}
