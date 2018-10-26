package io.joshatron.tak.server.controller;

import io.joshatron.tak.server.config.ApplicationConfig;
import io.joshatron.tak.server.database.AccountDAO;
import io.joshatron.tak.server.database.AccountDAOSqlite;
import io.joshatron.tak.server.exceptions.BadRequestException;
import io.joshatron.tak.server.exceptions.ForbiddenException;
import io.joshatron.tak.server.exceptions.NoAuthException;
import io.joshatron.tak.server.exceptions.ResourceNotFoundException;
import io.joshatron.tak.server.request.Auth;
import io.joshatron.tak.server.request.UserChange;
import io.joshatron.tak.server.response.User;
import io.joshatron.tak.server.utils.AccountUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/account", produces = "application/json")
public class AccountController {

    private AccountUtils accountUtils;

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
            accountUtils.registerUser(auth);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NoAuthException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/changepass", consumes = "application/json", produces = "application/json")
    public ResponseEntity changePassword(@RequestHeader(value="Authorization") String auth, @RequestBody UserChange passChange) {
        try {
            passChange.setAuth(new Auth(auth));
            accountUtils.updatePassword(passChange);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NoAuthException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/changename", consumes = "application/json", produces = "application/json")
    public ResponseEntity changeUsername(@RequestHeader(value="Authorization") String auth, @RequestBody UserChange userChange) {
        try {
            userChange.setAuth(new Auth(auth));
            accountUtils.updateUsername(userChange);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NoAuthException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/authenticate", produces = "application/json")
    public ResponseEntity authenticate(@RequestHeader(value="Authorization") String auth) {
        try {
            if(accountUtils.isAuthenticated(new Auth(auth))) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            else {
                throw new NoAuthException();
            }
        } catch (NoAuthException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/user", produces = "application/json")
    public ResponseEntity<User> findUser(@RequestParam(value="user") String username, @RequestParam("id") String id) {
        try {
            User user;
            if(username == null && id != null && id.length() > 0) {
                user = accountUtils.getUserFromId(id);
            }
            else if(id == null && username != null && username.length() > 0) {
                user = accountUtils.getUserFromUsername(username);
            }
            else {
                throw new BadRequestException("You can only specify the username or the ID.");
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (NoAuthException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
