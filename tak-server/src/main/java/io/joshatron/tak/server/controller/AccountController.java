package io.joshatron.tak.server.controller;

import io.joshatron.tak.server.config.ApplicationConfig;
import io.joshatron.tak.server.database.AccountDAO;
import io.joshatron.tak.server.database.AccountDAOSqlite;
import io.joshatron.tak.server.exceptions.BadRequestException;
import io.joshatron.tak.server.exceptions.ForbiddenException;
import io.joshatron.tak.server.exceptions.NoAuthException;
import io.joshatron.tak.server.exceptions.ResourceNotFoundException;
import io.joshatron.tak.server.request.Auth;
import io.joshatron.tak.server.request.PassChange;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/account", produces = "application/json")
public class AccountController {

    private AccountDAO accountDAO;

    public AccountController() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        accountDAO = context.getBean(AccountDAOSqlite.class);
    }

    public AccountController(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @PutMapping("/register")
    public ResponseEntity register(@RequestBody Auth auth) {
        try {
            accountDAO.registerUser(auth);
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

    @PostMapping("/changepass")
    public ResponseEntity changePassword(@RequestHeader(value="Authorization") String auth, @RequestBody PassChange passChange) {
        try {
            passChange.setAuth(new Auth(auth));
            accountDAO.updatePassword(passChange);
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

    @GetMapping("/authenticate")
    public ResponseEntity authenticate(@RequestHeader(value="Authorization") String auth) {
        try {
            if(accountDAO.isAuthenticated(new Auth(auth))) {
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
}
