package io.joshatron.tak.server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @PostMapping("/register")
    public void register() {

    }

    @PostMapping("/changepass")
    public void changePassword() {

    }
}
