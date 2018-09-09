package io.joshatron.tak.server.controller;

import io.joshatron.tak.server.requestbody.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/social")
public class SocialController {

    @PostMapping("/request")
    public void requestFriend(@RequestBody FriendRequest friendRequest) {
    }

    @PostMapping("/response")
    public void respondToRequest(@RequestBody FriendResponse friendResponse) {
    }

    @PostMapping("/checkincoming")
    public void checkIncomingRequests(@RequestBody AuthWrapper authWrapper) {
        Auth auth = authWrapper.getAuth();
    }

    @PostMapping("/checkoutgoing")
    public void checkOutgoingRequests(@RequestBody AuthWrapper authWrapper) {
        Auth auth = authWrapper.getAuth();
    }

    @PostMapping("/block")
    public void blockUser(@RequestBody Block block) {
    }

    @PostMapping("/unblock")
    public void unblockUser(@RequestBody Unblock unblock) {
    }

    @PostMapping("/friends")
    public void listFriends(@RequestBody AuthWrapper authWrapper) {
        Auth auth = authWrapper.getAuth();
    }

    @PostMapping("/blocked")
    public void listBlocked(@RequestBody AuthWrapper authWrapper) {
        Auth auth = authWrapper.getAuth();
    }

    @PostMapping("/sendmessage")
    public void sendMessage(@RequestBody SendMessage sendMessage) {
    }

    @PostMapping("/readmessages")
    public void readMessages(@RequestBody ReadMessages readMessages) {
    }
}
