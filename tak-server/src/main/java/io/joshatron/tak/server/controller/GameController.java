package io.joshatron.tak.server.controller;

import io.joshatron.tak.server.pojos.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {

    @PostMapping("/request")
    public void requestGame(@RequestBody GameRequest gameRequest) {
    }

    @PostMapping("/response")
    public void respondToGameRequest(@RequestBody GameResponse gameResponse) {
    }

    @PostMapping("/checkincoming")
    public void checkIncomingGames(@RequestBody AuthWrapper authWrapper) {
        Auth auth = authWrapper.getAuth();
    }

    @PostMapping("/checkoutgoing")
    public void checkOutgoingGames(@RequestBody AuthWrapper authWrapper) {
        Auth auth = authWrapper.getAuth();
    }

    @PostMapping("/random")
    public void requestRandomGame(@RequestBody RandomGame randomGame) {
    }

    @PostMapping("/completed")
    public void listCompletedGames(@RequestBody ListCompleted listCompleted) {
    }

    @PostMapping("/incomplete")
    public void listIncompleteGames(@RequestBody ListIncomplete listIncomplete) {
    }

    @PostMapping("/game")
    public void getGame(@RequestBody GetGame getGame) {
    }

    @PostMapping("/play")
    public void playTurn(@RequestBody PlayTurn playTurn) {
    }
}
