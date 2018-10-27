package io.joshatron.tak.server.exceptions;

public class ResourceNotFoundException extends GameServerException {

    public ResourceNotFoundException() {
        super("The resource could not be found.");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
