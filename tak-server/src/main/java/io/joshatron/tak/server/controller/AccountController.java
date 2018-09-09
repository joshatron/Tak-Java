package io.joshatron.tak.server.controller;

import io.joshatron.tak.server.requestbody.Auth;
import io.joshatron.tak.server.requestbody.PassChange;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @PostMapping("/register")
    public void register(@RequestBody Auth auth) {
    }

    @PostMapping("/changepass")
    public void changePassword(@RequestBody PassChange passChange) {
    }
}
