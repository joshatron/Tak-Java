package io.joshatron.tak.server.database;

import io.joshatron.tak.server.request.*;
import io.joshatron.tak.server.response.Message;

public interface SocialDAO {

    boolean createFriendRequest(UserInteraction request) throws Exception;
    boolean deleteFriendRequest(UserInteraction request) throws Exception;
    boolean respondToFriendRequest(FriendResponse response) throws Exception;
    boolean blockUser(UserInteraction block) throws Exception;
    boolean unblockUser(UserInteraction unblock) throws Exception;
    boolean sendMessage(SendMessage sendMessage) throws Exception;
    boolean isBlocked(String requester, String other) throws Exception;

    String[] listFriends(Auth auth) throws Exception;
    String[] listBlocked(Auth auth) throws Exception;
    String[] listIncomingFriendRequests(Auth auth) throws Exception;
    String[] listOutgoingFriendRequests(Auth auth) throws Exception;
    Message[] listMessages(ReadMessages readMessages) throws Exception;
}
