package io.joshatron.tak.server.controllers;

import io.joshatron.tak.server.config.ApplicationConfig;
import io.joshatron.tak.server.exceptions.ForbiddenException;
import io.joshatron.tak.server.request.*;
import io.joshatron.tak.server.response.*;
import io.joshatron.tak.server.utils.SocialUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/social")
public class SocialController {

    private SocialUtils socialUtils;
    private Logger logger = LoggerFactory.getLogger(SocialController.class);

    public SocialController() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        socialUtils = context.getBean(SocialUtils.class);
    }

    public SocialController(SocialUtils socialUtils) {
        this.socialUtils = socialUtils;
    }

    @PostMapping(value = "/request/create/{id}", produces = "application/json")
    public ResponseEntity requestFriend(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String other) {
        try {
            logger.info("Creating friend request");
            socialUtils.createFriendRequest(new UserInteraction(new Auth(auth), other));
            logger.info("Successfully created friend request");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @DeleteMapping(value = "/request/cancel/{id}", produces = "application/json")
    public ResponseEntity cancelFriendRequest(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String other) {
        try {
            logger.info("Deleting friend request");
            socialUtils.deleteFriendRequest(new UserInteraction(new Auth(auth), other));
            logger.info("Successfully deleted friend request");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @PostMapping(value = "/request/respond/{id}/{answer}", produces = "application/json")
    public ResponseEntity respondToRequest(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String id, @PathVariable("answer") String friendResponse) {
        try {
            logger.info("Responding to friend request");
            socialUtils.respondToFriendRequest(new FriendResponse(new Auth(auth), id, friendResponse));
            logger.info("Successfully responded to request");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @GetMapping(value = "/request/incoming", produces = "application/json")
    public ResponseEntity checkIncomingRequests(@RequestHeader(value="Authorization") String auth) {
        try {
            logger.info("Requesting incoming friend requests");
            User[] incoming = socialUtils.listIncomingFriendRequests(new Auth(auth));
            logger.info("Returning requests");
            return new ResponseEntity<>(new Users(incoming), HttpStatus.OK);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @GetMapping(value = "/request/outgoing", produces = "application/json")
    public ResponseEntity checkOutgoingRequests(@RequestHeader(value="Authorization") String auth) {
        try {
            logger.info("Requesting outgoing friend requests");
            User[] outgoing = socialUtils.listOutgoingFriendRequests(new Auth(auth));
            logger.info("Returning requests");
            return new ResponseEntity<>(new Users(outgoing), HttpStatus.OK);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @PostMapping(value = "/user/unfriend/{id}", produces = "application/json")
    public ResponseEntity unfriend(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String other) {
        try {
            logger.info("Unfriending user");
            socialUtils.unfriend(new UserInteraction(new Auth(auth), other));
            logger.info("User successfully unfriended");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @PostMapping(value = "/user/block/{id}", produces = "application/json")
    public ResponseEntity blockUser(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String block) {
        try {
            logger.info("Blocking user");
            socialUtils.blockUser(new UserInteraction(new Auth(auth), block));
            logger.info("User successfully blocked");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @DeleteMapping(value = "/user/unblock/{id}", produces = "application/json")
    public ResponseEntity unblockUser(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String unblock) {
        try {
            logger.info("Unblocking user");
            socialUtils.unblockUser(new UserInteraction(new Auth(auth), unblock));
            logger.info("User successfully unblocked");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @GetMapping(value = "/user/blocked/{id}", produces = "application/json")
    public ResponseEntity isBlocked(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String isBlocked) {
        try {
            logger.info("Checking if user is blocked");
            if(socialUtils.isBlocked(new UserInteraction(new Auth(auth), isBlocked))) {
                logger.info("The user is blocked");
                throw new ForbiddenException("You are blocked from interacting with this user");
            }
            else {
                logger.info("The user is not blocked");
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }


    @GetMapping(value = "/user/listfriends", produces = "application/json")
    public ResponseEntity listFriends(@RequestHeader(value="Authorization") String auth) {
        try {
            logger.info("Getting list of friends");
            User[] friends = socialUtils.listFriends(new Auth(auth));
            logger.info("Returning friend list");
            return new ResponseEntity<>(new Users(friends), HttpStatus.OK);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @GetMapping(value = "/user/listblocking", produces = "application/json")
    public ResponseEntity listBlocked(@RequestHeader(value="Authorization") String auth) {
        try {
            logger.info("Getting list blocking users");
            User[] blocked = socialUtils.listBlocked(new Auth(auth));
            logger.info("Returning blocking list");
            return new ResponseEntity<>(new Users(blocked), HttpStatus.OK);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @PostMapping(value = "/message/send", consumes = "application/json", produces = "application/json")
    public ResponseEntity sendMessage(@RequestHeader(value="Authorization") String auth, @RequestBody SendMessage sendMessage) {
        try {
            logger.info("Sending a message");
            sendMessage.setAuth(new Auth(auth));
            socialUtils.sendMessage(sendMessage);
            logger.info("Message successfully sent");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @GetMapping(value = "/message/read", produces = "application/json")
    public ResponseEntity readMessages(@RequestHeader(value="Authorization") String auth, @RequestParam("senders") String senders, @RequestParam("start") long start, @RequestParam("read") boolean read) {
        try {
            logger.info("Reading messages");
            ReadMessages readMessages = new ReadMessages(new Auth(auth), senders.split(","), new Date(start), read);
            Message[] messages = socialUtils.listMessages(readMessages);
            logger.info("Messages found, returning");
            return new ResponseEntity<>(new Messages(messages), HttpStatus.OK);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @PostMapping(value = "/message/markread", consumes = "application/json", produces = "application/json")
    public ResponseEntity markMessagesRead(@RequestHeader(value="Authorization") String auth, @RequestBody MarkRead markRead) {
        try {
            logger.info("Marking messages as read");
            markRead.setAuth(new Auth(auth));
            socialUtils.markMessagesRead(markRead);
            logger.info("Successfully marked messages");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }

    @GetMapping(value = "/notifications", produces = "application/json")
    public ResponseEntity getNotifications(@RequestHeader(value="Authorization") String auth) {
        try {
            logger.info("Getting social notifications");
            SocialNotifications notifications = socialUtils.getNotifications(new Auth(auth));
            logger.info("Social notifications found, returning");
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (Exception e) {
            return ControllerUtils.handleExceptions(e, logger);
        }
    }
}
