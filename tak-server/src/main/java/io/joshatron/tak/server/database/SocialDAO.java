package io.joshatron.tak.server.database;

import io.joshatron.tak.server.requestbody.Auth;

public interface SocialDAO {

    boolean createFriendRequest(Auth auth, String acceptor);
    boolean deleteFriendRequest(Auth auth, String toDelete);
    boolean acceptFriendRequest(Auth auth, String requester);
    boolean denyFriendRequest(Auth auth, String requester);
    boolean blockUser(Auth auth, String toBlock);
    boolean unblockUser(Auth auth, String toUnblock);
    boolean sendMessage(Auth auth, String recipient, String message);

    String[] listFriends(Auth auth);
    String[] listBlocked(Auth auth);
    String[] listIncomingFriendRequests(Auth auth);
    String[] listOutgoingFriendRequests(Auth auth);
    String[] listMessages(Auth auth, String[] senders, String start, boolean read);
}
