package io.joshatron.tak.server.exceptions;

public class NoAuthException extends GameServerException {

    public NoAuthException() {
        super("There was an auth failure. Either the username or password is invalid.");
    }

    public NoAuthException(String message) {
        super(message);
    }
}
