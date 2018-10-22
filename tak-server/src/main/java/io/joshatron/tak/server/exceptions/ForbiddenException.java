package io.joshatron.tak.server.exceptions;

public class ForbiddenException extends Exception {

    public ForbiddenException() {
        super("{\"reason\": \"The user is forbidden from performing that action.\"}");
    }

    public ForbiddenException(String message) {
        super("{\"reason\": \"" + message + "\"}");
    }
}
