package io.joshatron.tak.server.exceptions;

public class ServerErrorException extends GameServerException{

    public ServerErrorException() {
        super("The server encountered an extenal error");
    }

    public ServerErrorException(String message) {
        super(message);
    }
}
