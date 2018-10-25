package io.joshatron.tak.server.controller;

import io.joshatron.tak.server.config.ApplicationConfig;
import io.joshatron.tak.server.database.SocialDAO;
import io.joshatron.tak.server.database.SocialDAOSqlite;
import io.joshatron.tak.server.exceptions.BadRequestException;
import io.joshatron.tak.server.exceptions.ForbiddenException;
import io.joshatron.tak.server.exceptions.NoAuthException;
import io.joshatron.tak.server.exceptions.ResourceNotFoundException;
import io.joshatron.tak.server.request.*;
import io.joshatron.tak.server.response.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/social")
public class SocialController {

    private SocialDAO socialDAO;

    public SocialController() {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        socialDAO = context.getBean(SocialDAOSqlite.class);
    }

    public SocialController(SocialDAO socialDAO) {
        this.socialDAO = socialDAO;
    }

    @PostMapping(value = "/request/create/{id}", produces = "application/json")
    public ResponseEntity requestFriend(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String other) {
        try {
            socialDAO.createFriendRequest(new UserInteraction(new Auth(auth), other));
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NoAuthException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/request/cancel/{id}", produces = "application/json")
    public ResponseEntity cancelFriendRequest(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String other) {
        try {
            socialDAO.deleteFriendRequest(new UserInteraction(new Auth(auth), other));
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NoAuthException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/request/respond/{id}/{answer}", produces = "application/json")
    public ResponseEntity respondToRequest(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String id, @PathVariable("answer") String friendResponse) {
        try {
            socialDAO.respondToFriendRequest(new FriendResponse(new Auth(auth), id, friendResponse));
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NoAuthException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/request/incoming", produces = "application/json")
    public ResponseEntity<Users> checkIncomingRequests(@RequestHeader(value="Authorization") String auth) {
        try {
            User[] incoming = socialDAO.listIncomingFriendRequests(new Auth(auth));
            return new ResponseEntity<>(new Users(incoming), HttpStatus.OK);
        } catch (NoAuthException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/request/outgoing", produces = "application/json")
    public ResponseEntity<Users> checkOutgoingRequests(@RequestHeader(value="Authorization") String auth) {
        try {
            User[] outgoing = socialDAO.listOutgoingFriendRequests(new Auth(auth));
            return new ResponseEntity<>(new Users(outgoing), HttpStatus.OK);
        } catch (NoAuthException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/user/unfriend/{id}", produces = "application/json")
    public ResponseEntity unfriend(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String other) {
        try {
            socialDAO.unfriend(new UserInteraction(new Auth(auth), other));
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NoAuthException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/user/block/{id}", produces = "application/json")
    public ResponseEntity blockUser(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String block) {
        try {
            socialDAO.blockUser(new UserInteraction(new Auth(auth), block));
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NoAuthException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/user/unblock/{id}", produces = "application/json")
    public ResponseEntity unblockUser(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String unblock) {
        try {
            socialDAO.unblockUser(new UserInteraction(new Auth(auth), unblock));
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NoAuthException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/user/blocked/{id}", produces = "application/json")
    public ResponseEntity isBlocked(@RequestHeader(value="Authorization") String auth, @PathVariable("id") String isBlocked) {
        try {
            if(socialDAO.isBlocked(new UserInteraction(new Auth(auth), isBlocked))) {
                throw new ResourceNotFoundException();
            }
            else {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
        } catch (NoAuthException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "/user/listfriends", produces = "application/json")
    public ResponseEntity<Users> listFriends(@RequestHeader(value="Authorization") String auth) {
        try {
            User[] friends = socialDAO.listFriends(new Auth(auth));
            return new ResponseEntity<>(new Users(friends), HttpStatus.OK);
        } catch (NoAuthException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/user/listblocking", produces = "application/json")
    public ResponseEntity<Users> listBlocked(@RequestHeader(value="Authorization") String auth) {
        try {
            User[] blocked = socialDAO.listBlocked(new Auth(auth));
            return new ResponseEntity<>(new Users(blocked), HttpStatus.OK);
        } catch (NoAuthException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/message/send", consumes = "application/json", produces = "application/json")
    public ResponseEntity sendMessage(@RequestHeader(value="Authorization") String auth, @RequestBody SendMessage sendMessage) {
        try {
            sendMessage.setAuth(new Auth(auth));
            socialDAO.sendMessage(sendMessage);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NoAuthException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/message/read", produces = "application/json")
    public ResponseEntity<Messages> readMessages(@RequestHeader(value="Authorization") String auth, @RequestParam("senders") String senders, @RequestParam("start") long start, @RequestParam("read") boolean read) {
        try {
            ReadMessages readMessages = new ReadMessages(new Auth(auth), senders.split(","), new Date(start), read);
            Message[] messages = socialDAO.listMessages(readMessages);
            return new ResponseEntity<>(new Messages(messages), HttpStatus.OK);
        } catch (NoAuthException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/message/markread", consumes = "application/json", produces = "application/json")
    public ResponseEntity markMessagesRead(@RequestHeader(value="Authorization") String auth, @RequestBody MarkRead markRead) {
        try {
            markRead.setAuth(new Auth(auth));
            socialDAO.markMessagesRead(markRead);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (NoAuthException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/notifications", produces = "application/json")
    public ResponseEntity<SocialNotifications> getNotifications(@RequestHeader(value="Authorization") String auth) {
        try {
            SocialNotifications notifications = socialDAO.getNotifications(new Auth(auth));
            return new ResponseEntity<>(notifications, HttpStatus.OK);
        } catch (NoAuthException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (BadRequestException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ForbiddenException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
