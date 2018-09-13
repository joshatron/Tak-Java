package io.joshatron.tak.server.controller;

import io.joshatron.tak.server.database.AccountDAO;
import io.joshatron.tak.server.database.AccountDAOSqlite;
import io.joshatron.tak.server.request.Auth;
import io.joshatron.tak.server.request.PassChange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/account")
public class AccountController {

    private AccountDAO accountDAO;

    public AccountController() {
        accountDAO = new AccountDAOSqlite();
    }

    public AccountController(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody Auth auth) {
        try {
            if(accountDAO.registerUser(auth)) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            else {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/changepass")
    public ResponseEntity changePassword(@RequestBody PassChange passChange) {
        try {
            if(accountDAO.updatePassword(passChange)) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            else {
                return new ResponseEntity(HttpStatus.FORBIDDEN);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
