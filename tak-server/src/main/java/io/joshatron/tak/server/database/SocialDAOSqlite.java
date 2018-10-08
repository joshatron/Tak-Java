package io.joshatron.tak.server.database;

import io.joshatron.tak.server.request.*;
import io.joshatron.tak.server.response.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class SocialDAOSqlite implements SocialDAO {

    private AccountDAOSqlite accountDAO;
    private Connection conn;

    public SocialDAOSqlite() {
        accountDAO = new AccountDAOSqlite();
        conn = accountDAO.getConnection();
    }

    public SocialDAOSqlite(AccountDAOSqlite accountDAO, Connection conn) {
        this.accountDAO = accountDAO;
        this.conn = conn;
    }

    @Override
    public boolean createFriendRequest(UserInteraction request) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(request.getAuth())) {
            return false;
        }
        //if user blocked, return false
        if(isBlocked(request.getAuth().getUsername(), request.getOther())) {
            return false;
        }

        PreparedStatement checkFriendsStmt = null;
        PreparedStatement checkStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        //if friends already exists, return false
        String checkFriends = "SELECT requester, acceptor " +
                "FROM friends " +
                "WHERE (requester = ? AND acceptor = ?) OR (requester = ? AND acceptor = ?);";

        //if request already exists, return false
        String checkRequest = "SELECT requester, acceptor " +
                "FROM friend_requests " +
                "WHERE (requester = ? AND acceptor = ?) OR (requester = ? AND acceptor = ?);";

        //add request
        String insertRequest = "INSERT INTO friend_requests (requester, acceptor) " +
                "VALUES (?,?);";

        try {
            checkFriendsStmt = conn.prepareStatement(checkFriends);
            checkFriendsStmt.setInt(1, accountDAO.idFromUsername(request.getAuth().getUsername()));
            checkFriendsStmt.setInt(2, accountDAO.idFromUsername(request.getOther()));
            checkFriendsStmt.setInt(3, accountDAO.idFromUsername(request.getOther()));
            checkFriendsStmt.setInt(4, accountDAO.idFromUsername(request.getAuth().getUsername()));
            rs = checkFriendsStmt.executeQuery();

            if (rs.next()) {
                return false;
            }
            rs.close();

            checkStmt = conn.prepareStatement(checkRequest);
            checkStmt.setInt(1, accountDAO.idFromUsername(request.getAuth().getUsername()));
            checkStmt.setInt(2, accountDAO.idFromUsername(request.getOther()));
            checkStmt.setInt(3, accountDAO.idFromUsername(request.getOther()));
            checkStmt.setInt(4, accountDAO.idFromUsername(request.getAuth().getUsername()));
            rs = checkStmt.executeQuery();

            if (rs.next()) {
                return false;
            }

            insertStmt = conn.prepareStatement(insertRequest);
            insertStmt.setInt(1, accountDAO.idFromUsername(request.getAuth().getUsername()));
            insertStmt.setInt(2, accountDAO.idFromUsername(request.getOther()));
            insertStmt.executeUpdate();

            return true;
        }
        finally {
            if(checkFriendsStmt != null) {
                checkFriendsStmt.close();
            }
            if(checkStmt != null) {
                checkStmt.close();
            }
            if(insertStmt != null) {
                insertStmt.close();
            }
            if(rs != null) {
                rs.close();
            }
        }
    }

    @Override
    public boolean deleteFriendRequest(UserInteraction request) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(request.getAuth())) {
            return false;
        }

        PreparedStatement checkStmt = null;
        PreparedStatement deleteStmt = null;
        ResultSet rs = null;

        //if request does not exist, return false
        String checkRequest = "SELECT requester, acceptor " +
                "FROM friend_requests " +
                "WHERE requester = ? AND acceptor = ?;";

        //delete request
        String deleteRequest = "DELETE FROM friend_requests " +
                "WHERE requester = ? AND acceptor = ?;";

        try {
            checkStmt = conn.prepareStatement(checkRequest);
            checkStmt.setInt(1, accountDAO.idFromUsername(request.getAuth().getUsername()));
            checkStmt.setInt(2, accountDAO.idFromUsername(request.getOther()));
            rs = checkStmt.executeQuery();

            if (!rs.next()) {
                return false;
            }

            deleteStmt = conn.prepareStatement(deleteRequest);
            deleteStmt.setInt(1, accountDAO.idFromUsername(request.getAuth().getUsername()));
            deleteStmt.setInt(2, accountDAO.idFromUsername(request.getOther()));
            deleteStmt.executeUpdate();

            return true;
        }
        finally {
            if(checkStmt != null) {
                checkStmt.close();
            }
            if(deleteStmt != null) {
                deleteStmt.close();
            }
            if(rs != null) {
                rs.close();
            }
        }
    }

    @Override
    public boolean respondToFriendRequest(FriendResponse response) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(response.getAuth())) {
            return false;
        }

        PreparedStatement checkStmt = null;
        PreparedStatement deleteStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        //if request doesn't exists, return false
        String checkRequest = "SELECT requester, acceptor " +
                "FROM friend_requests " +
                "WHERE requester = ? AND acceptor = ?;";

        //delete request and if accept add friends
        String deleteRequest = "DELETE FROM friend_requests " +
                "WHERE requester = ? AND acceptor = ?;";

        String insertFriend = "INSERT INTO friends (requester, acceptor) " +
                "VALUES (?,?);";

        try {
            checkStmt = conn.prepareStatement(checkRequest);
            checkStmt.setInt(1, accountDAO.idFromUsername(response.getFriend()));
            checkStmt.setInt(2, accountDAO.idFromUsername(response.getAuth().getUsername()));
            rs = checkStmt.executeQuery();

            if (!rs.next()) {
                return false;
            }

            deleteStmt = conn.prepareStatement(deleteRequest);
            deleteStmt.setInt(1, accountDAO.idFromUsername(response.getFriend()));
            deleteStmt.setInt(2, accountDAO.idFromUsername(response.getAuth().getUsername()));
            deleteStmt.executeUpdate();

            if (response.getResponse().toLowerCase().equals("accept")) {
                insertStmt = conn.prepareStatement(insertFriend);
                insertStmt.setInt(1, accountDAO.idFromUsername(response.getFriend()));
                insertStmt.setInt(2, accountDAO.idFromUsername(response.getAuth().getUsername()));
                insertStmt.executeUpdate();
            }

            return true;
        }
        finally {
            if(checkStmt != null) {
                checkStmt.close();
            }
            if(deleteStmt != null) {
                deleteStmt.close();
            }
            if(insertStmt != null) {
                insertStmt.close();
            }
            if(rs != null) {
                rs.close();
            }
        }
    }

    @Override
    public boolean blockUser(UserInteraction block) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(block.getAuth())) {
            return false;
        }

        PreparedStatement deleteRequestStmt = null;
        PreparedStatement deleteStmt = null;
        PreparedStatement insertStmt = null;

        //delete any requests and existing friendships
        String deleteRequest = "DELETE FROM friend_requests " +
                "WHERE (requester = ? AND acceptor = ?) OR (requester = ? AND acceptor = ?);";

        String deleteFriend = "DELETE FROM friends " +
                "WHERE (requester = ? AND acceptor = ?) OR (requester = ? AND acceptor = ?);";

        //add block
        String insertBlock = "INSERT INTO blocked (requester, blocked) " +
                "VALUES (?,?);";

        try {
            deleteRequestStmt = conn.prepareStatement(deleteRequest);
            deleteRequestStmt.setInt(1, accountDAO.idFromUsername(block.getAuth().getUsername()));
            deleteRequestStmt.setInt(2, accountDAO.idFromUsername(block.getOther()));
            deleteRequestStmt.setInt(3, accountDAO.idFromUsername(block.getOther()));
            deleteRequestStmt.setInt(4, accountDAO.idFromUsername(block.getAuth().getUsername()));
            deleteRequestStmt.executeUpdate();

            deleteStmt = conn.prepareStatement(deleteFriend);
            deleteStmt.setInt(1, accountDAO.idFromUsername(block.getAuth().getUsername()));
            deleteStmt.setInt(2, accountDAO.idFromUsername(block.getOther()));
            deleteStmt.setInt(3, accountDAO.idFromUsername(block.getOther()));
            deleteStmt.setInt(4, accountDAO.idFromUsername(block.getAuth().getUsername()));
            deleteStmt.executeUpdate();

            insertStmt = conn.prepareStatement(insertBlock);
            insertStmt.setInt(1, accountDAO.idFromUsername(block.getAuth().getUsername()));
            insertStmt.setInt(2, accountDAO.idFromUsername(block.getOther()));
            insertStmt.executeUpdate();

            return false;
        }
        finally {
            if(deleteRequestStmt != null) {
                deleteRequestStmt.close();
            }
            if(deleteStmt != null) {
                deleteStmt.close();
            }
            if(insertStmt != null) {
                insertStmt.close();
            }
        }
    }

    @Override
    public boolean unblockUser(UserInteraction unblock) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(unblock.getAuth())) {
            return false;
        }

        //if block doesn't exist, return false
        if(!isBlocked(unblock.getAuth().getUsername(), unblock.getOther())) {
            return false;
        }

        PreparedStatement deleteStmt = null;

        //remove block
        String deleteBlock = "DELETE FROM blocked " +
                "WHERE requester = ? AND blocked = ?;";

        try {
            deleteStmt = conn.prepareStatement(deleteBlock);
            deleteStmt.setInt(1, accountDAO.idFromUsername(unblock.getAuth().getUsername()));
            deleteStmt.setInt(2, accountDAO.idFromUsername(unblock.getOther()));
            deleteStmt.executeUpdate();

            return true;
        }
        finally {
            if(deleteStmt != null) {
                deleteStmt.close();
            }
        }
    }

    @Override
    public boolean sendMessage(SendMessage sendMessage) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(sendMessage.getAuth())) {
            return false;
        }
        //if blocked, return false
        if(isBlocked(sendMessage.getAuth().getUsername(), sendMessage.getRecipient())) {
            return false;
        }

        PreparedStatement insertStmt = null;

        //Add message
        LocalDate date = LocalDate.now();
        String insertRequest = "INSERT INTO messages (sender, recipient, message, time, opened) " +
                "VALUES (?,?,?,?,0);";

        try {
            insertStmt = conn.prepareStatement(insertRequest);
            insertStmt.setInt(1, accountDAO.idFromUsername(sendMessage.getAuth().getUsername()));
            insertStmt.setInt(2, accountDAO.idFromUsername(sendMessage.getRecipient()));
            insertStmt.setString(3, sendMessage.getMessage());
            insertStmt.setString(4, date.toString());
            insertStmt.executeUpdate();

            return false;
        }
        finally {
            if(insertStmt != null) {
                insertStmt.close();
            }
        }
    }

    @Override
    public boolean isBlocked(String requester, String other) throws SQLException {
        PreparedStatement checkStmt = null;
        ResultSet rs = null;

        //if request already exists, return false
        String checkRequest = "SELECT requester, blocked " +
                "FROM blocked " +
                "WHERE requester = ? AND blocked = ?;";

        try {
            checkStmt = conn.prepareStatement(checkRequest);
            checkStmt.setInt(1, accountDAO.idFromUsername(requester));
            checkStmt.setInt(2, accountDAO.idFromUsername(other));
            rs = checkStmt.executeQuery();

            if (rs.next()) {
                return true;
            }

            return false;
        }
        finally {
            if(checkStmt != null) {
                checkStmt.close();
            }
            if(rs != null) {
                rs.close();
            }
        }
    }

    @Override
    public String[] listFriends(Auth auth) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(auth)) {
            return null;
        }

        PreparedStatement requesterStmt = null;
        PreparedStatement acceptorStmt = null;
        ResultSet requestSet = null;
        ResultSet acceptSet = null;

        String getFriendsRequester = "SELECT users.username as username " +
                "FROM friends " +
                "LEFT OUTER JOIN users on friends.acceptor = users.id " +
                "WHERE requester = ?;";

        String getFriendsAcceptor = "SELECT users.username as username " +
                "FROM friends " +
                "LEFT OUTER JOIN users on friends.requester = users.id " +
                "WHERE acceptor = ?;";

        try {
            ArrayList<String> names = new ArrayList<>();

            requesterStmt = conn.prepareStatement(getFriendsRequester);
            requesterStmt.setInt(1, accountDAO.idFromUsername(auth.getUsername()));
            requestSet = requesterStmt.executeQuery();

            while (requestSet.next()) {
                names.add(requestSet.getString("username"));
            }

            acceptorStmt = conn.prepareStatement(getFriendsAcceptor);
            acceptorStmt.setInt(1, accountDAO.idFromUsername(auth.getUsername()));
            acceptSet = acceptorStmt.executeQuery();

            while (acceptSet.next()) {
                names.add(acceptSet.getString("username"));
            }

            return names.toArray(new String[names.size()]);
        }
        finally {
            if(requesterStmt != null) {
                requesterStmt.close();
            }
            if(acceptorStmt != null) {
                acceptorStmt.close();
            }
            if(requestSet != null) {
                requestSet.close();
            }
            if(acceptSet != null) {
                acceptSet.close();
            }
        }
    }

    @Override
    public String[] listBlocked(Auth auth) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(auth)) {
            return null;
        }

        PreparedStatement blockedStmt = null;
        ResultSet blockedSet = null;

        String getBlocked = "SELECT users.username as username " +
                "FROM blocked " +
                "LEFT OUTER JOIN users on blocked.blocked = users.id " +
                "WHERE requester = ?;";

        try {
            blockedStmt = conn.prepareStatement(getBlocked);
            blockedStmt.setInt(1, accountDAO.idFromUsername(auth.getUsername()));
            blockedSet = blockedStmt.executeQuery();

            ArrayList<String> names = new ArrayList<>();

            while (blockedSet.next()) {
                names.add(blockedSet.getString("username"));
            }

            return names.toArray(new String[names.size()]);
        }
        finally {
            if(blockedStmt != null) {
                blockedStmt.close();
            }
            if(blockedSet != null) {
                blockedSet.close();
            }
        }
    }

    @Override
    public String[] listIncomingFriendRequests(Auth auth) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(auth)) {
            return null;
        }

        PreparedStatement incomingStmt = null;
        ResultSet incomingSet = null;

        String getIncoming = "SELECT users.username as username " +
                "FROM friend_requests " +
                "LEFT OUTER JOIN users on friend_requests.requester = users.id " +
                "WHERE acceptor = ?;";

        try {
            incomingStmt = conn.prepareStatement(getIncoming);
            incomingStmt.setInt(1, accountDAO.idFromUsername(auth.getUsername()));
            incomingSet = incomingStmt.executeQuery();

            ArrayList<String> names = new ArrayList<>();

            while (incomingSet.next()) {
                names.add(incomingSet.getString("username"));
            }

            return names.toArray(new String[names.size()]);
        }
        finally {
            if(incomingStmt != null) {
                incomingStmt.close();
            }
            if(incomingSet != null) {
                incomingSet.close();
            }
        }
    }

    @Override
    public String[] listOutgoingFriendRequests(Auth auth) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(auth)) {
            return null;
        }

        PreparedStatement outgoingStmt = null;
        ResultSet outgoingSet = null;

        String getOutgoing = "SELECT users.username as username " +
                "FROM friend_requests " +
                "LEFT OUTER JOIN users on friend_requests.acceptor = users.id " +
                "WHERE requester = ?;";

        try {
            outgoingStmt = conn.prepareStatement(getOutgoing);
            outgoingStmt.setInt(1, accountDAO.idFromUsername(auth.getUsername()));
            outgoingSet = outgoingStmt.executeQuery();

            ArrayList<String> names = new ArrayList<>();

            while (outgoingSet.next()) {
                names.add(outgoingSet.getString("username"));
            }

            return names.toArray(new String[names.size()]);
        }
        finally {
            if(outgoingStmt != null) {
                outgoingStmt.close();
            }
            if(outgoingSet != null) {
                outgoingSet.close();
            }
        }
    }

    @Override
    public Message[] listMessages(ReadMessages readMessages) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(readMessages.getAuth())) {
            return null;
        }

        PreparedStatement messageStmt = null;
        PreparedStatement readStmt = null;
        ResultSet messageSet = null;

        String getMessages = "SELECT sender, message, time " +
                "FROM messages " +
                "WHERE recipient = ?";

        if(readMessages.getRead() != null) {
            if(readMessages.getRead().toLowerCase().equals("true")) {
                getMessages += " AND read = 1";
            }
            else {
                getMessages += " AND read = 0";
            }
        }
        if(readMessages.getStart() != null) {
            getMessages += " AND time > ?";
        }
        if(readMessages.getSenders() != null && readMessages.getSenders().length > 0) {
            getMessages += " AND (sender = ?";
            for(int i = 1; i < readMessages.getSenders().length; i++) {
                getMessages += " OR sender = ?";
            }
            getMessages += ")";
        }
        getMessages += ";";

        String markRead = "UPDATE messages " +
                "SET read = 1 " +
                "WHERE recipient = ?;";

        try {
            messageStmt = conn.prepareStatement(getMessages);
            messageStmt.setInt(1, accountDAO.idFromUsername(readMessages.getAuth().getUsername()));
            int i = 2;
            if (readMessages.getStart() != null) {
                messageStmt.setString(2, readMessages.getStart());
                i++;
            }
            for (String user : readMessages.getSenders()) {
                messageStmt.setInt(i, accountDAO.idFromUsername(user));
                i++;
            }
            messageSet = messageStmt.executeQuery();

            ArrayList<Message> messages = new ArrayList<>();

            while (messageSet.next()) {
                messages.add(new Message(messageSet.getString("sender"), messageSet.getString("time"), messageSet.getString("message")));
            }


            readStmt = conn.prepareStatement(markRead);
            readStmt.setInt(1, accountDAO.idFromUsername(readMessages.getAuth().getUsername()));
            readStmt.executeUpdate();

            return messages.toArray(new Message[messages.size()]);
        }
        finally {
            if(messageStmt != null) {
                messageStmt.close();
            }
            if(readStmt != null) {
                readStmt.close();
            }
            if(messageSet != null) {
                messageSet.close();
            }
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public AccountDAOSqlite getAccountDAO() {
        return accountDAO;
    }
}
