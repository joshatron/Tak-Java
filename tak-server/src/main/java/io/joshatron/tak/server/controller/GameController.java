package io.joshatron.tak.server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {

    @PostMapping("/request")
    public void requestGame() {

    }

    @PostMapping("/response")
    public void respondToGameRequest() {

    }

    @PostMapping("/checkincoming")
    public void checkIncomingGames() {

    }

    @PostMapping("/checkoutgoing")
    public void checkOutgoingGames() {

    }

    @PostMapping("/random")
    public void requestRandomGame() {

    }

    @PostMapping("/completed")
    public void listCompletedGames() {

    }

    @PostMapping("/incomplete")
    public void listIncompleteGames() {

    }

    @PostMapping("/game")
    public void getGame() {

    }

    @PostMapping("/play")
    public void playTurn() {

    }
}
