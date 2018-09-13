package io.joshatron.tak.server.controller;

import io.joshatron.tak.server.database.GameDAO;
import io.joshatron.tak.server.request.*;
import io.joshatron.tak.server.response.GameInfo;
import io.joshatron.tak.server.response.GameRequests;
import io.joshatron.tak.server.response.Games;
import io.joshatron.tak.server.response.RequestInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
public class GameController {

    private GameDAO gameDAO;

    public GameController() {

    }

    public GameController(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    @PostMapping("/request")
    public ResponseEntity requestGame(@RequestBody GameRequest gameRequest) {
        try {
            if(gameDAO.requestGame(gameRequest)) {
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

    @PostMapping("/response")
    public ResponseEntity respondToGameRequest(@RequestBody GameResponse gameResponse) {
        try {
            if(gameDAO.respondToGame(gameResponse)) {
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

    @PostMapping("/checkincoming")
    public GameRequests checkIncomingGames(@RequestBody AuthWrapper authWrapper) {
        try {
            RequestInfo[] games = gameDAO.checkIncomingGames(authWrapper.getAuth());
            if(games != null) {
                return new GameRequests(games);
            }
            else {
                //return forbidden
            }
        } catch (Exception e) {
            e.printStackTrace();
            //return server error
        }

        return null;
    }

    @PostMapping("/checkoutgoing")
    public GameRequests checkOutgoingGames(@RequestBody AuthWrapper authWrapper) {
        try {
            RequestInfo[] games = gameDAO.checkOutgoingGames(authWrapper.getAuth());
            if(games != null) {
                return new GameRequests(games);
            }
            else {
                //return forbidden
            }
        } catch (Exception e) {
            e.printStackTrace();
            //return server error
        }

        return null;
    }

    @PostMapping("/random")
    public ResponseEntity requestRandomGame(@RequestBody RandomGame randomGame) {
        try {
            if(gameDAO.requestRandomGame(randomGame)) {
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

    @PostMapping("/completed")
    public Games listCompletedGames(@RequestBody ListCompleted listCompleted) {
        try {
            int[] games = gameDAO.listCompletedGames(listCompleted);
            if(games != null) {
                return new Games(games);
            }
            else {
                //return forbidden
            }
        } catch (Exception e) {
            e.printStackTrace();
            //return server error
        }

        return null;
    }

    @PostMapping("/incomplete")
    public Games listIncompleteGames(@RequestBody ListIncomplete listIncomplete) {
        try {
            int[] games = gameDAO.listIncompleteGames(listIncomplete);
            if(games != null) {
                return new Games(games);
            }
            else {
                //return forbidden
            }
        } catch (Exception e) {
            e.printStackTrace();
            //return server error
        }

        return null;
    }

    @PostMapping("/game")
    public GameInfo getGame(@RequestBody GetGame getGame) {
        try {
            GameInfo info = gameDAO.getGame(getGame);
            if(info != null) {
                return info;
            }
            else {
                //return forbidden
            }
        } catch (Exception e) {
            e.printStackTrace();
            //return server error
        }

        return null;
    }

    @PostMapping("/play")
    public ResponseEntity playTurn(@RequestBody PlayTurn playTurn) {
        try {
            if(gameDAO.playTurn(playTurn)) {
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
