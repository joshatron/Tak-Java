package io.joshatron.tak.server.database;

import io.joshatron.tak.server.requestbody.*;

public interface SocialDAO {

    boolean createFriendRequest(FriendRequest request);
    boolean deleteFriendRequest(CancelFriendRequest request);
    boolean respondToFriendRequest(FriendResponse response);
    boolean blockUser(Block block);
    boolean unblockUser(Unblock unblock);
    boolean sendMessage(SendMessage sendMessage);

    String[] listFriends(Auth auth);
    String[] listBlocked(Auth auth);
    String[] listIncomingFriendRequests(Auth auth);
    String[] listOutgoingFriendRequests(Auth auth);
    String[] listMessages(ReadMessages readMessages);
}
