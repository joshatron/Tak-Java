package io.joshatron.tak.server.exceptions;

public class NoAuthException extends Exception {

    public NoAuthException() {
        super("{\"reason\": \"There was an auth failure. Either the username or password is invalid.\"}");
    }

    public NoAuthException(String message) {
        super("{\"reason\": \"" + message + "\"}");
    }
}
