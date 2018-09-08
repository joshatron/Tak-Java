package io.joshatron.tak.server;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TakController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }

    @RequestMapping("/account/register")
    public void register() {

    }

    @RequestMapping("/account/changepass")
    public void changePassword() {

    }

    @RequestMapping("/social/friendrequest")
    public void requestFriend() {

    }

    @RequestMapping("/social/friendresponse")
    public void respondToRequest() {

    }

    @RequestMapping("/social/checkincoming")
    public void checkIncomingRequests() {

    }

    @RequestMapping("/social/checkoutgoing")
    public void checkOutgoingRequests() {

    }

    @RequestMapping("/social/block")
    public void blockUser() {

    }

    @RequestMapping("/social/unblock")
    public void unblockUser() {

    }

    @RequestMapping("/social/friends")
    public void listFriends() {

    }

    @RequestMapping("/social/blocked")
    public void listBlocked() {

    }

    @RequestMapping("/social/sendmessage")
    public void sendMessage() {

    }

    @RequestMapping("/social/readmessages")
    public void readMessages() {

    }

    @RequestMapping("/game/gamerequest")
    public void requestGame() {

    }

    @RequestMapping("/game/gameresponse")
    public void respondToGameRequest() {

    }

    @RequestMapping("/game/checkincoming")
    public void checkIncomingGames() {

    }

    @RequestMapping("/game/checkoutgoing")
    public void checkOutgoingGames() {

    }

    @RequestMapping("/game/random")
    public void requestRandomGame() {

    }

    @RequestMapping("/game/completed")
    public void listCompletedGames() {

    }

    @RequestMapping("/game/incomplete")
    public void listIncompleteGames() {

    }

    @RequestMapping("/game/game")
    public void getGame() {

    }

    @RequestMapping("/game/play")
    public void playTurn() {

    }
}
