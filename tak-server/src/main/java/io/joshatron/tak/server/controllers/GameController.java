package io.joshatron.tak.server.controllers;

import io.joshatron.tak.server.config.ApplicationConfig;
import io.joshatron.tak.server.request.*;
import io.joshatron.tak.server.response.GameInfo;
import io.joshatron.tak.server.response.GameRequests;
import io.joshatron.tak.server.response.Games;
import io.joshatron.tak.server.response.RequestInfo;
import io.joshatron.tak.server.utils.GameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class GameController {

    private GameUtils gameUtils;
    private Logger logger = LoggerFactory.getLogger(AccountController.class);

    public GameController() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        gameUtils = context.getBean(GameUtils.class);
    }

    public GameController(GameUtils gameUtils) {
        this.gameUtils = gameUtils;
    }

    @PostMapping(value = "/request/create/{id}", produces = "application/json")
    public ResponseEntity requestGame(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String other, @RequestBody GameRequest gameRequest) {
        try {
            logger.info("Requesting new game");
            gameRequest.setOpponent(other);
            gameRequest.setAuth(new Auth(auth));
            gameUtils.requestGame(gameRequest);
            logger.info("Request successfully made");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @DeleteMapping(value = "/request/cancel/{id}", produces = "application/json")
    public ResponseEntity cancelGameRequest(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String other) {
        try {
            logger.info("Deleting game request");
            gameUtils.deleteRequest(new UserInteraction(new Auth(auth), other));
            logger.info("Successfully deleted request");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @PostMapping("/request/respond/{id}/{answer}")
    public ResponseEntity respondToGameRequest(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String id, @PathVariable("answer") String answer) {
        try {
            logger.info("Responding to game request");
            gameUtils.respondToGame(new Answer(new Auth(auth), id, answer));
            logger.info("Successfully responded to game request");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @PostMapping("/checkincoming")
    public GameRequests checkIncomingGames(@RequestBody AuthWrapper authWrapper) {
        try {
            RequestInfo[] games = gameUtils.checkIncomingGames(authWrapper.getAuth());
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
            RequestInfo[] games = gameUtils.checkOutgoingGames(authWrapper.getAuth());
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
            if(gameUtils.requestRandomGame(randomGame)) {
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
            int[] games = gameUtils.listCompletedGames(listCompleted);
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
            int[] games = gameUtils.listIncompleteGames(listIncomplete);
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
            GameInfo info = gameUtils.getGame(getGame);
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
            if(gameUtils.playTurn(playTurn)) {
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
