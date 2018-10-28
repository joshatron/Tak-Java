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
            logger.error("Exception occurred, returning unauthorized", noAuth);
            return new ResponseEntity<>(noAuth.getJsonMessage(), HttpStatus.UNAUTHORIZED);
        }
        else if(e instanceof BadRequestException) {
            BadRequestException badRequest = (BadRequestException) e;
            logger.error("Exception occurred, returning bad request", badRequest);
            return new ResponseEntity<>(badRequest.getJsonMessage(), HttpStatus.BAD_REQUEST);
        }
        else if(e instanceof ForbiddenException) {
            ForbiddenException forbidden = (ForbiddenException) e;
            logger.error("Exception occurred, returning forbidden", forbidden);
            return new ResponseEntity<>(forbidden.getJsonMessage(), HttpStatus.FORBIDDEN);
        }
        else if(e instanceof ResourceNotFoundException) {
            ResourceNotFoundException resourceNotFound = (ResourceNotFoundException) e;
            logger.error("Exception occurred, returning not found", resourceNotFound);
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
