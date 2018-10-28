package io.joshatron.tak.server.controllers;

import io.joshatron.tak.server.config.ApplicationConfig;
import io.joshatron.tak.server.exceptions.*;
import io.joshatron.tak.server.request.Auth;
import io.joshatron.tak.server.request.UserChange;
import io.joshatron.tak.server.response.User;
import io.joshatron.tak.server.utils.AccountUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/account", produces = "application/json")
public class AccountController {

    private AccountUtils accountUtils;
    private Logger logger = LoggerFactory.getLogger(AccountController.class);

    public AccountController() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        accountUtils = context.getBean(AccountUtils.class);
    }

    public AccountController(AccountUtils accountUtils) {
        this.accountUtils = accountUtils;
    }

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity register(@RequestBody Auth auth) {
        try {
            logger.info("Registering user");
            accountUtils.registerUser(auth);
            logger.info("Registered {}, returning no content", auth.getUsername());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @PostMapping(value = "/changepass", consumes = "application/json", produces = "application/json")
    public ResponseEntity changePassword(@RequestHeader(value="Authorization") String auth, @RequestBody UserChange passChange) {
        try {
            logger.info("Changing password");
            passChange.setAuth(new Auth(auth));
            accountUtils.updatePassword(passChange);
            logger.info("Changed password of {}, returning no content", passChange.getAuth().getUsername());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @PostMapping(value = "/changename", consumes = "application/json", produces = "application/json")
    public ResponseEntity changeUsername(@RequestHeader(value="Authorization") String auth, @RequestBody UserChange userChange) {
        try {
            logger.info("Changing username");
            userChange.setAuth(new Auth(auth));
            accountUtils.updateUsername(userChange);
            logger.info("Changed username of {}, returning no content", userChange.getAuth().getUsername());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @GetMapping(value = "/authenticate", produces = "application/json")
    public ResponseEntity authenticate(@RequestHeader(value="Authorization") String auth) {
        try {
            logger.info("Authenticating");
            Auth a = new Auth(auth);
            if(accountUtils.isAuthenticated(a)) {
                logger.info("Authenticated {}, returning no content", a.getUsername());
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            else {
                throw new NoAuthException();
            }
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @GetMapping(value = "/user", produces = "application/json")
    public ResponseEntity findUser(@RequestParam(value="user") String username, @RequestParam("id") String id) {
        try {
            logger.info("Finding user info");
            User user;
            if(username == null && id != null) {
                logger.info("Finding user by ID");
                user = accountUtils.getUserFromId(id);
            }
            else if(id == null && username != null) {
                logger.info("Finding user by username");
                user = accountUtils.getUserFromUsername(username);
            }
            else {
                throw new BadRequestException("You can only specify the username or the ID.");
            }
            logger.info("User {} found, returning OK with user info", user.getUsername());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }
}
