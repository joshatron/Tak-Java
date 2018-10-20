package io.joshatron.tak.server.exceptions;

public class BadRequestException extends Exception {

    public BadRequestException() {
        super("The input provided was invalid.");
    }
}
