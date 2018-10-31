package io.joshatron.tak.server.controllers;

import io.joshatron.tak.engine.turn.Turn;
import io.joshatron.tak.server.config.ApplicationConfig;
import io.joshatron.tak.server.request.*;
import io.joshatron.tak.server.response.*;
import io.joshatron.tak.server.utils.GameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/games")
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
            gameUtils.requestGame(new Auth(auth), other, gameRequest);
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
            gameUtils.deleteRequest(new Auth(auth), other);
            logger.info("Successfully deleted request");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @PostMapping(value = "/request/respond/{id}", produces = "application/json")
    public ResponseEntity respondToGameRequest(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String id, @RequestBody Text answer) {
        try {
            logger.info("Responding to game request");
            gameUtils.respondToGame(new Auth(auth), id, answer);
            logger.info("Successfully responded to game request");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @GetMapping(value = "/request/incoming", produces = "application/json")
    public ResponseEntity checkIncomingGames(@RequestHeader(value="Authorization") String auth) {
        try {
            logger.info("Requesting incoming games");
            RequestInfo[] games = gameUtils.checkIncomingRequests(new Auth(auth));
            logger.info("Incoming games found");
            return new ResponseEntity<>(games, HttpStatus.OK);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @GetMapping(value = "/request/outgoing", produces = "application/json")
    public ResponseEntity checkOutgoingGames(@RequestHeader(value="Authorization") String auth) {
        try {
            logger.info("Requesting outgoing games");
            RequestInfo[] games = gameUtils.checkOutgoingRequests(new Auth(auth));
            logger.info("Outgoing games found");
            return new ResponseEntity<>(games, HttpStatus.OK);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @PostMapping(value = "/request/random/create/{size}", produces = "application/json")
    public ResponseEntity requestRandomGame(@RequestHeader(value="Authorization") String auth, @PathVariable("size") int size) {
        try {
            logger.info("Requesting a random game");
            gameUtils.requestRandomGame(new Auth(auth), size);
            logger.info("Random game successfully made");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @DeleteMapping(value = "/request/random/cancel/{size}", produces = "application/json")
    public ResponseEntity cancelRandomGameRequest(@RequestHeader(value="Authorization") String auth, @PathVariable("size") int size) {
        try {
            logger.info("Deleting random game request");
            gameUtils.deleteRandomRequest(new Auth(auth), size);
            logger.info("Successfully deleted random request");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @GetMapping(value = "/request/random/outgoing", produces = "application/json")
    public ResponseEntity getOutgoingRandomRequests(@RequestHeader(value="Authorization") String auth) {
        try {
            logger.info("Getting outgoing random request sizes");
            int[] sizes = gameUtils.checkRandomSizes(new Auth(auth));
            logger.info("Outing random games found");
            return new ResponseEntity<>(sizes, HttpStatus.OK);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity findGames(@RequestHeader(value="Authorization") String auth, @RequestParam(value = "opponents", required = false) String opponents,
                                    @RequestParam(value = "start", required = false) long start, @RequestParam(value = "end", required = false) long end,
                                    @RequestParam(value = "complete", required = false) boolean complete, @RequestParam(value = "pending", required = false) boolean pending,
                                    @RequestParam(value = "sizes", required = false) String sizes, @RequestParam(value = "winner", required = false) String winner,
                                    @RequestParam(value = "color", required = false) String color) {
        try {
            logger.info("Searching for games");
            GameInfo[] games = gameUtils.findGames(new Auth(auth), opponents, new Date(start), new Date(end), complete, pending, sizes, winner, color);
            logger.info("Games found");
            return new ResponseEntity(games, HttpStatus.OK);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @GetMapping(value = "/game/{id}", produces = "application/json")
    public ResponseEntity getGameInfo(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String gameId) {
        try {
            logger.info("Getting game info");
            GameInfo info = gameUtils.getGameInfo(new Auth(auth), gameId);
            logger.info("Game info found");
            return new ResponseEntity<>(info, HttpStatus.OK);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @GetMapping(value = "/game/{id}/turns", produces = "application/json")
    public ResponseEntity getPossibleTurns(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String gameId) {
        try {
            logger.info("Getting all possible turns for a game");
            Turn[] turns = gameUtils.getTurns(new Auth(auth), gameId);
            logger.info("Turns found");
            return new ResponseEntity<>(turns, HttpStatus.OK);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @PostMapping(value = "/game/{id}/play", produces = "application/json")
    public ResponseEntity playTurn(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String gameId, @RequestBody Text turn) {
        try {
            logger.info("Trying to play a turn");
            gameUtils.playTurn(new Auth(auth), gameId, turn);
            logger.info("Turn successfully made");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @GetMapping(value = "/notifications", produces = "application/json")
    public ResponseEntity getNotifications(@RequestHeader(value="Authorization") String auth) {
        try {
            logger.info("Getting notifications");
            GameNotifications gameNotifications = gameUtils.getNotifications(new Auth(auth));
            logger.info("Notifications found");
            return new ResponseEntity(gameNotifications, HttpStatus.OK);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }
}
