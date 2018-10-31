package io.joshatron.tak.server.utils;

import io.joshatron.tak.server.database.AccountDAO;
import io.joshatron.tak.server.database.SocialDAO;
import io.joshatron.tak.server.request.*;
import io.joshatron.tak.server.response.Message;
import io.joshatron.tak.server.response.SocialNotifications;
import io.joshatron.tak.server.response.User;

import java.util.Date;

public class SocialUtils {

    private SocialDAO socialDAO;
    private AccountDAO accountDAO;

    public SocialUtils(SocialDAO socialDAO, AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
        this.socialDAO = socialDAO;
    }

    public void createFriendRequest(Auth auth, String other) {
    }

    public void deleteFriendRequest(Auth auth, String other) {
    }

    public void respondToFriendRequest(Auth auth, String other, Text answer) {
    }

    public User[] listIncomingFriendRequests(Auth auth) {
        return null;
    }

    public User[] listOutgoingFriendRequests(Auth auth) {
        return null;
    }

    public void unfriend(Auth auth, String other) {
    }

    public void blockUser(Auth auth, String other) {
    }

    public void unblockUser(Auth auth, String other) {
    }

    public boolean isBlocked(Auth auth, String other) {
        return false;
    }

    public User[] listFriends(Auth auth) {
        return null;
    }

    public User[] listBlocked(Auth auth) {
        return null;
    }

    public void sendMessage(Auth auth, Text sendMessage) {
    }

    public Message[] listMessages(Auth auth, String senders, Date start, Date end, boolean read) {
        return null;
    }

    public void markMessagesRead(Auth auth, MarkRead markRead) {
    }

    public SocialNotifications getNotifications(Auth auth) {
        return null;
    }
}
