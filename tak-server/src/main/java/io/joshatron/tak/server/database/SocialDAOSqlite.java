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
        conn = DatabaseManager.getConnection();
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

        //if friends already exists, return false
        String checkFriends = "SELECT requester, acceptor " +
                "FROM friends " +
                "WHERE (requester = ? AND acceptor = ?) OR (requester = ? AND acceptor = ?);";

        PreparedStatement checkFriendsStmt = conn.prepareStatement(checkFriends);
        checkFriendsStmt.setInt(1, accountDAO.idFromUsername(request.getAuth().getUsername()));
        checkFriendsStmt.setInt(2, accountDAO.idFromUsername(request.getOther()));
        checkFriendsStmt.setInt(3, accountDAO.idFromUsername(request.getOther()));
        checkFriendsStmt.setInt(4, accountDAO.idFromUsername(request.getAuth().getUsername()));
        ResultSet rs = checkFriendsStmt.executeQuery();

        if(rs.next()) {
            return false;
        }

        //if request already exists, return false
        String checkRequest = "SELECT requester, acceptor " +
                "FROM friend_requests " +
                "WHERE (requester = ? AND acceptor = ?) OR (requester = ? AND acceptor = ?);";

        PreparedStatement checkStmt = conn.prepareStatement(checkRequest);
        checkStmt.setInt(1, accountDAO.idFromUsername(request.getAuth().getUsername()));
        checkStmt.setInt(2, accountDAO.idFromUsername(request.getOther()));
        checkStmt.setInt(3, accountDAO.idFromUsername(request.getOther()));
        checkStmt.setInt(4, accountDAO.idFromUsername(request.getAuth().getUsername()));
        rs = checkStmt.executeQuery();

        if(rs.next()) {
            return false;
        }

        //add request
        String insertRequest = "INSERT INTO friend_requests (requester, acceptor) " +
                "VALUES (?,?);";

        PreparedStatement insertStmt = conn.prepareStatement(insertRequest);
        insertStmt.setInt(1, accountDAO.idFromUsername(request.getAuth().getUsername()));
        insertStmt.setInt(2, accountDAO.idFromUsername(request.getOther()));
        insertStmt.executeUpdate();

        return true;
    }

    @Override
    public boolean deleteFriendRequest(UserInteraction request) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(request.getAuth())) {
            return false;
        }

        //if request does not exist, return false
        String checkRequest = "SELECT requester, acceptor " +
                "FROM friend_requests " +
                "WHERE requester = ? AND acceptor = ?;";

        PreparedStatement checkStmt = conn.prepareStatement(checkRequest);
        checkStmt.setInt(1, accountDAO.idFromUsername(request.getAuth().getUsername()));
        checkStmt.setInt(2, accountDAO.idFromUsername(request.getOther()));
        ResultSet rs = checkStmt.executeQuery();

        if(!rs.next()) {
            return false;
        }

        //delete request
        String deleteRequest = "DELETE FROM friend_requests " +
                "WHERE requester = ? AND acceptor = ?;";

        PreparedStatement deleteStmt = conn.prepareStatement(deleteRequest);
        deleteStmt.setInt(1, accountDAO.idFromUsername(request.getAuth().getUsername()));
        deleteStmt.setInt(2, accountDAO.idFromUsername(request.getOther()));
        deleteStmt.executeUpdate();

        return true;
    }

    @Override
    public boolean respondToFriendRequest(FriendResponse response) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(response.getAuth())) {
            return false;
        }

        //if request doesn't exists, return false
        String checkRequest = "SELECT requester, acceptor " +
                "FROM friend_requests " +
                "WHERE requester = ? AND acceptor = ?;";

        PreparedStatement checkStmt = conn.prepareStatement(checkRequest);
        checkStmt.setInt(1, accountDAO.idFromUsername(response.getFriend()));
        checkStmt.setInt(2, accountDAO.idFromUsername(response.getAuth().getUsername()));
        ResultSet rs = checkStmt.executeQuery();

        if(!rs.next()) {
            return false;
        }

        //delete request and if accept add friends
        String deleteRequest = "DELETE FROM friend_requests " +
                "WHERE requester = ? AND acceptor = ?;";

        PreparedStatement deleteStmt = conn.prepareStatement(deleteRequest);
        deleteStmt.setInt(1, accountDAO.idFromUsername(response.getFriend()));
        deleteStmt.setInt(2, accountDAO.idFromUsername(response.getAuth().getUsername()));
        deleteStmt.executeUpdate();

        if(response.getResponse().toLowerCase().equals("accept")) {
            String insertFriend = "INSERT INTO friends (requester, acceptor) " +
                    "VALUES (?,?);";

            PreparedStatement insertStmt = conn.prepareStatement(insertFriend);
            insertStmt.setInt(1, accountDAO.idFromUsername(response.getFriend()));
            insertStmt.setInt(2, accountDAO.idFromUsername(response.getAuth().getUsername()));
            insertStmt.executeUpdate();
        }

        return true;
    }

    @Override
    public boolean blockUser(UserInteraction block) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(block.getAuth())) {
            return false;
        }

        //delete any requests and existing friendships
        String deleteRequest = "DELETE FROM friend_requests " +
                "WHERE (requester = ? AND acceptor = ?) OR (requester = ? AND acceptor = ?);";

        PreparedStatement deleteRequestStmt = conn.prepareStatement(deleteRequest);
        deleteRequestStmt.setInt(1, accountDAO.idFromUsername(block.getAuth().getUsername()));
        deleteRequestStmt.setInt(2, accountDAO.idFromUsername(block.getOther()));
        deleteRequestStmt.setInt(3, accountDAO.idFromUsername(block.getOther()));
        deleteRequestStmt.setInt(4, accountDAO.idFromUsername(block.getAuth().getUsername()));
        deleteRequestStmt.executeUpdate();

        String deleteFriend = "DELETE FROM friends " +
                "WHERE (requester = ? AND acceptor = ?) OR (requester = ? AND acceptor = ?);";

        PreparedStatement deleteStmt = conn.prepareStatement(deleteFriend);
        deleteStmt.setInt(1, accountDAO.idFromUsername(block.getAuth().getUsername()));
        deleteStmt.setInt(2, accountDAO.idFromUsername(block.getOther()));
        deleteStmt.setInt(3, accountDAO.idFromUsername(block.getOther()));
        deleteStmt.setInt(4, accountDAO.idFromUsername(block.getAuth().getUsername()));
        deleteStmt.executeUpdate();

        //add block
        String insertBlock = "INSERT INTO blocked (requester, blocked) " +
                "VALUES (?,?);";

        PreparedStatement insertStmt = conn.prepareStatement(insertBlock);
        insertStmt.setInt(1, accountDAO.idFromUsername(block.getAuth().getUsername()));
        insertStmt.setInt(2, accountDAO.idFromUsername(block.getOther()));
        insertStmt.executeUpdate();

        return false;
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

        //remove block
        String deleteBlock = "DELETE FROM blocked " +
                "WHERE requester = ? AND blocked = ?;";

        PreparedStatement deleteStmt = conn.prepareStatement(deleteBlock);
        deleteStmt.setInt(1, accountDAO.idFromUsername(unblock.getAuth().getUsername()));
        deleteStmt.setInt(2, accountDAO.idFromUsername(unblock.getOther()));
        deleteStmt.executeUpdate();

        return true;
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

        //Add message
        LocalDate date = LocalDate.now();
        String insertRequest = "INSERT INTO messages (sender, recipient, message, time, opened) " +
                "VALUES (?,?,?,?,0);";

        PreparedStatement insertStmt = conn.prepareStatement(insertRequest);
        insertStmt.setInt(1, accountDAO.idFromUsername(sendMessage.getAuth().getUsername()));
        insertStmt.setInt(2, accountDAO.idFromUsername(sendMessage.getRecipient()));
        insertStmt.setString(3, sendMessage.getMessage());
        insertStmt.setString(4, date.toString());
        insertStmt.executeUpdate();

        return false;
    }

    @Override
    public boolean isBlocked(String requester, String other) throws SQLException {
        //if request already exists, return false
        String checkRequest = "SELECT requester, blocked " +
                "FROM blocked " +
                "WHERE requester = ? AND blocked = ?;";

        PreparedStatement checkStmt = conn.prepareStatement(checkRequest);
        checkStmt.setInt(1, accountDAO.idFromUsername(requester));
        checkStmt.setInt(2, accountDAO.idFromUsername(other));
        ResultSet rs = checkStmt.executeQuery();

        if(rs.next()) {
            return true;
        }

        return false;
    }

    @Override
    public String[] listFriends(Auth auth) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(auth)) {
            return null;
        }

        String getFriendsRequester = "SELECT users.username as username " +
                "FROM friends " +
                "LEFT OUTER JOIN users on friends.acceptor = users.id " +
                "WHERE requester = ?;";

        String getFriendsAcceptor = "SELECT users.username as username " +
                "FROM friends " +
                "LEFT OUTER JOIN users on friends.requester = users.id " +
                "WHERE acceptor = ?;";

        ArrayList<String> names = new ArrayList<>();

        PreparedStatement requesterStmt = conn.prepareStatement(getFriendsRequester);
        requesterStmt.setInt(1, accountDAO.idFromUsername(auth.getUsername()));
        ResultSet requestSet = requesterStmt.executeQuery();

        while(requestSet.next()) {
            names.add(requestSet.getString("username"));
        }

        PreparedStatement acceptorStmt = conn.prepareStatement(getFriendsAcceptor);
        acceptorStmt.setInt(1, accountDAO.idFromUsername(auth.getUsername()));
        ResultSet acceptSet = acceptorStmt.executeQuery();

        while(acceptSet.next()) {
            names.add(acceptSet.getString("username"));
        }

        return names.toArray(new String[names.size()]);
    }

    @Override
    public String[] listBlocked(Auth auth) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(auth)) {
            return null;
        }

        String getBlocked = "SELECT users.username as username " +
                "FROM blocked " +
                "LEFT OUTER JOIN users on blocked.blocked = users.id " +
                "WHERE requester = ?;";

        PreparedStatement blockedStmt = conn.prepareStatement(getBlocked);
        blockedStmt.setInt(1, accountDAO.idFromUsername(auth.getUsername()));
        ResultSet blockedSet = blockedStmt.executeQuery();

        ArrayList<String> names = new ArrayList<>();

        while(blockedSet.next()) {
            names.add(blockedSet.getString("username"));
        }

        return names.toArray(new String[names.size()]);
    }

    @Override
    public String[] listIncomingFriendRequests(Auth auth) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(auth)) {
            return null;
        }

        String getIncoming = "SELECT users.username as username " +
                "FROM friend_requests " +
                "LEFT OUTER JOIN users on friend_requests.requester = users.id " +
                "WHERE acceptor = ?;";

        PreparedStatement incomingStmt = conn.prepareStatement(getIncoming);
        incomingStmt.setInt(1, accountDAO.idFromUsername(auth.getUsername()));
        ResultSet incomingSet = incomingStmt.executeQuery();

        ArrayList<String> names = new ArrayList<>();

        while(incomingSet.next()) {
            names.add(incomingSet.getString("username"));
        }

        return names.toArray(new String[names.size()]);
    }

    @Override
    public String[] listOutgoingFriendRequests(Auth auth) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(auth)) {
            return null;
        }

        String getOutgoing = "SELECT users.username as username " +
                "FROM friend_requests " +
                "LEFT OUTER JOIN users on friend_requests.acceptor = users.id " +
                "WHERE requester = ?;";

        PreparedStatement outgoingStmt = conn.prepareStatement(getOutgoing);
        outgoingStmt.setInt(1, accountDAO.idFromUsername(auth.getUsername()));
        ResultSet outgoingSet = outgoingStmt.executeQuery();

        ArrayList<String> names = new ArrayList<>();

        while(outgoingSet.next()) {
            names.add(outgoingSet.getString("username"));
        }

        return names.toArray(new String[names.size()]);
    }

    @Override
    public Message[] listMessages(ReadMessages readMessages) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(readMessages.getAuth())) {
            return null;
        }

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

        PreparedStatement messageStmt = conn.prepareStatement(getMessages);
        messageStmt.setInt(1, accountDAO.idFromUsername(readMessages.getAuth().getUsername()));
        int i = 2;
        if(readMessages.getStart() != null) {
            messageStmt.setString(2, readMessages.getStart());
            i++;
        }
        for(String user : readMessages.getSenders()) {
            messageStmt.setInt(i, accountDAO.idFromUsername(user));
            i++;
        }
        ResultSet messageSet = messageStmt.executeQuery();

        ArrayList<Message> messages = new ArrayList<>();

        while(messageSet.next()) {
            messages.add(new Message(messageSet.getString("sender"), messageSet.getString("time"), messageSet.getString("message")));
        }


        String markRead = "UPDATE messages " +
                "SET read = 1 " +
                "WHERE recipient = ?;";

        PreparedStatement readStmt = conn.prepareStatement(markRead);
        readStmt.setInt(1, accountDAO.idFromUsername(readMessages.getAuth().getUsername()));
        readStmt.executeUpdate();

        return messages.toArray(new Message[messages.size()]);
    }
}
