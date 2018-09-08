package io.joshatron.tak.server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/social")
public class SocialController {

    @PostMapping("/request")
    public void requestFriend() {

    }

    @PostMapping("/response")
    public void respondToRequest() {

    }

    @PostMapping("/checkincoming")
    public void checkIncomingRequests() {

    }

    @PostMapping("/checkoutgoing")
    public void checkOutgoingRequests() {

    }

    @PostMapping("/block")
    public void blockUser() {

    }

    @PostMapping("/unblock")
    public void unblockUser() {

    }

    @PostMapping("/friends")
    public void listFriends() {

    }

    @PostMapping("/blocked")
    public void listBlocked() {

    }

    @PostMapping("/sendmessage")
    public void sendMessage() {

    }

    @PostMapping("/readmessages")
    public void readMessages() {

    }
}
