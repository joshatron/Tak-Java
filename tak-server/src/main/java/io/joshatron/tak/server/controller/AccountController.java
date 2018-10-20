package io.joshatron.tak.server.controller;

import io.joshatron.tak.server.config.ApplicationConfig;
import io.joshatron.tak.server.database.AccountDAO;
import io.joshatron.tak.server.database.AccountDAOSqlite;
import io.joshatron.tak.server.exceptions.BadRequestException;
import io.joshatron.tak.server.exceptions.ForbiddenException;
import io.joshatron.tak.server.exceptions.NoAuthException;
import io.joshatron.tak.server.request.Auth;
import io.joshatron.tak.server.request.PassChange;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
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
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/changepass")
    public ResponseEntity changePassword(@RequestHeader(value="Authorization") Auth auth, @RequestBody PassChange passChange) {
        try {
            passChange.setAuth(auth);
            accountDAO.updatePassword(passChange);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NoAuthException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/authenticate")
    public ResponseEntity authenticate(@RequestHeader(value="Authorization") Auth auth) {
        try {
            if(accountDAO.isAuthenticated(auth)) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            else {
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            }
        } catch (NoAuthException e) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
