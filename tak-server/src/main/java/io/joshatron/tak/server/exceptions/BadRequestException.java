package io.joshatron.tak.server.exceptions;

public class BadRequestException extends GameServerException {

    public BadRequestException() {
        super("The input provided was invalid.");
    }

    public BadRequestException(String message) {
        super(message);
    }
}
