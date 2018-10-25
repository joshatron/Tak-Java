package io.joshatron.tak.server.database;

import io.joshatron.tak.server.request.*;
import io.joshatron.tak.server.response.Message;
import io.joshatron.tak.server.response.SocialNotifications;
import io.joshatron.tak.server.response.User;

public interface SocialDAO {

    void createFriendRequest(UserInteraction request) throws Exception;
    void deleteFriendRequest(UserInteraction request) throws Exception;
    void respondToFriendRequest(FriendResponse response) throws Exception;
    void unfriend(UserInteraction unfriend) throws Exception;
    void blockUser(UserInteraction block) throws Exception;
    void unblockUser(UserInteraction unblock) throws Exception;
    void sendMessage(SendMessage sendMessage) throws Exception;
    void markMessagesRead(MarkRead markRead) throws Exception;
    boolean isBlocked(UserInteraction isBlocked) throws Exception;

    User[] listFriends(Auth auth) throws Exception;
    User[] listBlocked(Auth auth) throws Exception;
    User[] listIncomingFriendRequests(Auth auth) throws Exception;
    User[] listOutgoingFriendRequests(Auth auth) throws Exception;
    Message[] listMessages(ReadMessages readMessages) throws Exception;
    SocialNotifications getNotifications(Auth auth) throws Exception;
}
