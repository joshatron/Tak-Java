package io.joshatron.tak.server.controllers;

import io.joshatron.tak.server.exceptions.*;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ControllerUtils {

    private ControllerUtils() {
        throw new IllegalStateException("This is a utility class");
    }

    public static ResponseEntity handleExceptions(Exception e, Logger logger) {

        if(e instanceof NoAuthException) {
            NoAuthException noAuth = (NoAuthException) e;
            logger.warn("An unauthorized request was made: {}", noAuth.getMessage());
            return new ResponseEntity<>(noAuth.getJsonMessage(), HttpStatus.UNAUTHORIZED);
        }
        else if(e instanceof BadRequestException) {
            BadRequestException badRequest = (BadRequestException) e;
            logger.warn("A bad request was made: {}", badRequest.getMessage());
            return new ResponseEntity<>(badRequest.getJsonMessage(), HttpStatus.BAD_REQUEST);
        }
        else if(e instanceof ForbiddenException) {
            ForbiddenException forbidden = (ForbiddenException) e;
            logger.warn("A forbidden request was made: {}", forbidden.getMessage());
            return new ResponseEntity<>(forbidden.getJsonMessage(), HttpStatus.FORBIDDEN);
        }
        else if(e instanceof ResourceNotFoundException) {
            ResourceNotFoundException resourceNotFound = (ResourceNotFoundException) e;
            logger.warn("The resource could not be found: {}", resourceNotFound.getMessage());
            return new ResponseEntity<>(resourceNotFound.getJsonMessage(), HttpStatus.NOT_FOUND);
        }
        else if(e instanceof ServerErrorException) {
            ServerErrorException serverError = (ServerErrorException) e;
            logger.error("Exception occurred, returning server error", serverError);
            return new ResponseEntity<>(serverError.getJsonMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else {
            logger.error("Unknown exception occurred, returning server error", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
