package io.joshatron.tak.server.exceptions;

public class BadRequestException extends Exception {

    public BadRequestException() {
        super("{\"reason\": \"The input provided was invalid.\"}");
    }

    public BadRequestException(String message) {
        super("{\"reason\": \"" + message + "\"}");
    }
}
