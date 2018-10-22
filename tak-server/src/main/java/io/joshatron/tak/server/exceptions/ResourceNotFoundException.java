package io.joshatron.tak.server.exceptions;

public class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException() {
        super("{\"reason\": \"The resource could not be found.\"}");
    }

    public ResourceNotFoundException(String message) {
        super("{\"reason\": \"" + message + "\"}");
    }
}
