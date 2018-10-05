package io.joshatron.tak.server.controller;

import io.joshatron.tak.server.config.ApplicationConfig;
import io.joshatron.tak.server.database.SocialDAO;
import io.joshatron.tak.server.database.SocialDAOSqlite;
import io.joshatron.tak.server.request.*;
import io.joshatron.tak.server.response.Message;
import io.joshatron.tak.server.response.Messages;
import io.joshatron.tak.server.response.Users;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/request")
    public ResponseEntity requestFriend(@RequestBody UserInteraction friendRequest) {
        try {
            if(socialDAO.createFriendRequest(friendRequest)) {
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

    @PostMapping("/cancel")
    public ResponseEntity cancelFriendRequest(@RequestBody UserInteraction cancelFriendRequest) {
        try {
            if(socialDAO.deleteFriendRequest(cancelFriendRequest)) {
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
    public ResponseEntity respondToRequest(@RequestBody FriendResponse friendResponse) {
        try {
            if(socialDAO.respondToFriendRequest(friendResponse)) {
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

    @PostMapping("/incoming")
    public Users checkIncomingRequests(@RequestBody AuthWrapper authWrapper) {
        try {
            String[] incoming = socialDAO.listIncomingFriendRequests(authWrapper.getAuth());
            if(incoming != null) {
                return new Users(incoming);
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

    @PostMapping("/outgoing")
    public Users checkOutgoingRequests(@RequestBody AuthWrapper authWrapper) {
        try {
            String[] outgoing = socialDAO.listOutgoingFriendRequests(authWrapper.getAuth());
            if(outgoing != null) {
                return new Users(outgoing);
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

    @PostMapping("/block")
    public ResponseEntity blockUser(@RequestBody UserInteraction block) {
        try {
            if(socialDAO.blockUser(block)) {
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

    @PostMapping("/unblock")
    public ResponseEntity unblockUser(@RequestBody UserInteraction unblock) {
        try {
            if(socialDAO.unblockUser(unblock)) {
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

    @PostMapping("/friends")
    public Users listFriends(@RequestBody AuthWrapper authWrapper) {
        try {
            String[] friends = socialDAO.listFriends(authWrapper.getAuth());
            if(friends != null) {
                return new Users(friends);
            }
            else {
                //return fobidden
            }
        } catch (Exception e) {
            e.printStackTrace();
            //return server error
        }

        return null;
    }

    @PostMapping("/blocked")
    public Users listBlocked(@RequestBody AuthWrapper authWrapper) {
        try {
            String[] blocked = socialDAO.listBlocked(authWrapper.getAuth());
            if(blocked != null) {
                return new Users(blocked);
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

    @PostMapping("/send")
    public ResponseEntity sendMessage(@RequestBody SendMessage sendMessage) {
        try {
            if(socialDAO.sendMessage(sendMessage)) {
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

    @PostMapping("/read")
    public Messages readMessages(@RequestBody ReadMessages readMessages) {
        try {
            Message[] messages = socialDAO.listMessages(readMessages);
            if(messages != null) {
                return new Messages(messages);
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
}
