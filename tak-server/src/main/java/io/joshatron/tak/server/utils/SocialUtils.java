package io.joshatron.tak.server.utils;

import io.joshatron.tak.server.database.AccountDAO;
import io.joshatron.tak.server.database.SocialDAO;
import io.joshatron.tak.server.request.*;
import io.joshatron.tak.server.response.Message;
import io.joshatron.tak.server.response.SocialNotifications;
import io.joshatron.tak.server.response.User;

public class SocialUtils {

    private SocialDAO socialDAO;
    private AccountDAO accountDAO;

    public SocialUtils(SocialDAO socialDAO, AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
        this.socialDAO = socialDAO;
    }

    public void createFriendRequest(UserInteraction request) {
    }

    public void deleteFriendRequest(UserInteraction delete) {
    }

    public void respondToFriendRequest(Answer response) {
    }

    public User[] listIncomingFriendRequests(Auth auth) {
        return null;
    }

    public User[] listOutgoingFriendRequests(Auth auth) {
        return null;
    }

    public void unfriend(UserInteraction userInteraction) {
    }

    public void blockUser(UserInteraction userInteraction) {
    }

    public void unblockUser(UserInteraction userInteraction) {
    }

    public boolean isBlocked(UserInteraction userInteraction) {
        return false;
    }

    public User[] listFriends(Auth auth) {
        return null;
    }

    public User[] listBlocked(Auth auth) {
        return null;
    }

    public void sendMessage(SendMessage sendMessage) {
    }

    public Message[] listMessages(ReadMessages readMessages) {
        return null;
    }

    public void markMessagesRead(MarkRead markRead) {
    }

    public SocialNotifications getNotifications(Auth auth) {
        return null;
    }
}
