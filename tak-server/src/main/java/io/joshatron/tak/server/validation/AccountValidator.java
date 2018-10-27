package io.joshatron.tak.server.validation;

import io.joshatron.tak.server.exceptions.BadRequestException;
import io.joshatron.tak.server.request.Auth;
import io.joshatron.tak.server.request.UserChange;
import io.joshatron.tak.server.utils.AccountUtils;

public class AccountValidator {

    private AccountValidator() {
        throw new IllegalStateException("This is a utility class");
    }

    public static void validateAuth(Auth auth) throws BadRequestException {
        if(auth == null) {
            throw new BadRequestException("The authorization is in an invalid format.");
        }
        validateUsername(auth.getUsername());
        validatePassword(auth.getPassword());
    }

    public static void validatePassChange(UserChange passChange) throws BadRequestException {
        if(passChange == null) {
            throw new BadRequestException("The request is improperly formatted.");
        }
        validateAuth(passChange.getAuth());
        validatePassword(passChange.getUpdated());
    }

    public static void validateUserChange(UserChange userChange) throws BadRequestException {
        if(userChange == null) {
            throw new BadRequestException("The request is improperly formatted.");
        }
        validateAuth(userChange.getAuth());
        validateUsername(userChange.getUpdated());
    }

    public static void validateUsername(String username) throws BadRequestException {
        if(username == null || username.length() == 0) {
            throw new BadRequestException("The username is blank or missing.");
        }

        if(username.matches("^.*[^a-zA-Z0-9 ].*$")) {
            throw new BadRequestException("The username must be alphanumeric.");
        }
    }

    public static void validatePassword(String password) throws BadRequestException {
        if(password == null || password.length() == 0) {
            throw new BadRequestException("The password is blank or missing.");
        }
    }

    public static void validateUserId(String id) throws BadRequestException {
        if(id == null || id.length() == 0) {
            throw new BadRequestException("The ID is blank or missing.");
        }

        if(id.length() != AccountUtils.ID_LENGTH) {
            throw new BadRequestException("The ID is not the correct length.");
        }

        if(id.matches("^.*[^A-Z0-9 ].*$")) {
            throw new BadRequestException("The ID must contain only numbers and capital letters.");
        }
    }
}
