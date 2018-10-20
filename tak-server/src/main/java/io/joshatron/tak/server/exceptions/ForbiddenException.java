package io.joshatron.tak.server.exceptions;

public class ForbiddenException extends Exception {

    public ForbiddenException() {
        super("The user is forbidden from performing that action.");
    }
}
