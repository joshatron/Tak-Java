package io.joshatron.tak.server.database;

import io.joshatron.tak.server.request.*;
import io.joshatron.tak.server.response.Message;

import java.sql.SQLException;

public interface SocialDAO {

    boolean createFriendRequest(FriendRequest request) throws SQLException;
    boolean deleteFriendRequest(CancelFriendRequest request) throws SQLException;
    boolean respondToFriendRequest(FriendResponse response) throws SQLException;
    boolean blockUser(Block block) throws SQLException;
    boolean unblockUser(Unblock unblock) throws SQLException;
    boolean sendMessage(SendMessage sendMessage) throws SQLException;
    boolean isBlocked(String requester, String other) throws SQLException;

    String[] listFriends(Auth auth) throws SQLException;
    String[] listBlocked(Auth auth) throws SQLException;
    String[] listIncomingFriendRequests(Auth auth) throws SQLException;
    String[] listOutgoingFriendRequests(Auth auth) throws SQLException;
    Message[] listMessages(ReadMessages readMessages) throws SQLException;
}
