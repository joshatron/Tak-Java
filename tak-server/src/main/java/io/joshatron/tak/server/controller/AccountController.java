package io.joshatron.tak.server.controller;

import io.joshatron.tak.server.config.ApplicationConfig;
import io.joshatron.tak.server.database.AccountDAO;
import io.joshatron.tak.server.database.AccountDAOSqlite;
import io.joshatron.tak.server.request.Auth;
import io.joshatron.tak.server.request.AuthWrapper;
import io.joshatron.tak.server.request.PassChange;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

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
            if(accountDAO.registerUser(auth)) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            else {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/changepass")
    public ResponseEntity changePassword(@RequestHeader(value="Authorization") String auth, @RequestBody PassChange passChange) {
        passChange.setAuth(new Auth(auth));
        try {
            if(accountDAO.updatePassword(passChange)) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            else {
                System.out.println();
                System.out.println(passChange.getAuth().getUsername());
                System.out.println(passChange.getAuth().getPassword());
                System.out.println(passChange.getUpdated());
                System.out.println();
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity authenticate(@RequestBody AuthWrapper authWrapper) {
        try {
            if(accountDAO.isAuthenticated(authWrapper.getAuth())) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            else {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
