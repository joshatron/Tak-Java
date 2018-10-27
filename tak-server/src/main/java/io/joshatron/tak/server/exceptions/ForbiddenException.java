package io.joshatron.tak.server.exceptions;

public class ForbiddenException extends GameServerException {

    public ForbiddenException() {
        super("The user is forbidden from performing that action.");
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
