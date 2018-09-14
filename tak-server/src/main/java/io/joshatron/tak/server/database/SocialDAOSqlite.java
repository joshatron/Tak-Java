package io.joshatron.tak.server.database;

import io.joshatron.tak.server.request.*;
import io.joshatron.tak.server.response.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public boolean createFriendRequest(FriendRequest request) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(request.getAuth())) {
            return false;
        }
        //if user blocked, return false
        if(isBlocked(request.getAuth().getUsername(), request.getFriend())) {
            return false;
        }

        //if friends already exists, return false
        String checkFriends = "SELECT requester, acceptor " +
                "FROM friends " +
                "WHERE (requester = ? AND acceptor = ?) OR (requester = ? AND acceptor = ?);";

        PreparedStatement checkFriendsStmt = conn.prepareStatement(checkFriends);
        checkFriendsStmt.setString(1, request.getAuth().getUsername());
        checkFriendsStmt.setString(2, request.getFriend());
        checkFriendsStmt.setString(3, request.getFriend());
        checkFriendsStmt.setString(4, request.getAuth().getUsername());
        ResultSet rs = checkFriendsStmt.executeQuery();

        if(rs.next()) {
            return false;
        }

        //if request already exists, return false
        String checkRequest = "SELECT requester, acceptor " +
                "FROM friend_requests " +
                "WHERE (requester = ? AND acceptor = ?) OR (requester = ? AND acceptor = ?);";

        PreparedStatement checkStmt = conn.prepareStatement(checkRequest);
        checkStmt.setString(1, request.getAuth().getUsername());
        checkStmt.setString(2, request.getFriend());
        checkStmt.setString(3, request.getFriend());
        checkStmt.setString(4, request.getAuth().getUsername());
        rs = checkStmt.executeQuery();

        if(rs.next()) {
            return false;
        }

        //add request
        String insertRequest = "INSERT INTO friend_requests (requester, acceptor) " +
                "VALUES (?,?);";

        PreparedStatement insertStmt = conn.prepareStatement(insertRequest);
        insertStmt.setString(1, request.getAuth().getUsername());
        insertStmt.setString(2, request.getFriend());
        insertStmt.executeUpdate();

        return true;
    }

    @Override
    public boolean deleteFriendRequest(CancelFriendRequest request) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(request.getAuth())) {
            return false;
        }

        //if request does not exist, return false
        String checkRequest = "SELECT requester, acceptor " +
                "FROM friend_requests " +
                "WHERE requester = ? AND acceptor = ?;";

        PreparedStatement checkStmt = conn.prepareStatement(checkRequest);
        checkStmt.setString(1, request.getAuth().getUsername());
        checkStmt.setString(2, request.getAcceptor());
        ResultSet rs = checkStmt.executeQuery();

        if(!rs.next()) {
            return false;
        }

        //delete request
        String deleteRequest = "DELETE FROM friend_requests " +
                "WHERE requester = ? AND acceptor = ?;";

        PreparedStatement deleteStmt = conn.prepareStatement(deleteRequest);
        deleteStmt.setString(1, request.getAuth().getUsername());
        deleteStmt.setString(2, request.getAcceptor());
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
        checkStmt.setString(1, response.getFriend());
        checkStmt.setString(2, response.getAuth().getUsername());
        ResultSet rs = checkStmt.executeQuery();

        if(!rs.next()) {
            return false;
        }

        //delete request and if accept add friends
        String deleteRequest = "DELETE FROM friend_requests " +
                "WHERE requester = ? AND acceptor = ?;";

        PreparedStatement deleteStmt = conn.prepareStatement(deleteRequest);
        deleteStmt.setString(1, response.getFriend());
        deleteStmt.setString(2, response.getAuth().getUsername());
        deleteStmt.executeUpdate();

        if(response.getResponse().toLowerCase().equals("accept")) {
            String insertFriend = "INSERT INTO friends (requester, acceptor) " +
                    "VALUES (?,?);";

            PreparedStatement insertStmt = conn.prepareStatement(insertFriend);
            insertStmt.setString(1, response.getFriend());
            insertStmt.setString(2, response.getAuth().getUsername());
            insertStmt.executeUpdate();
        }

        return true;
    }

    @Override
    public boolean blockUser(Block block) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(block.getAuth())) {
            return false;
        }

        //delete any requests and existing friendships
        String deleteRequest = "DELETE FROM friend_requests " +
                "WHERE (requester = ? AND acceptor = ?) OR (requester = ? AND acceptor = ?);";

        PreparedStatement deleteRequestStmt = conn.prepareStatement(deleteRequest);
        deleteRequestStmt.setString(1, block.getAuth().getUsername());
        deleteRequestStmt.setString(2, block.getBlock());
        deleteRequestStmt.setString(3, block.getBlock());
        deleteRequestStmt.setString(4, block.getAuth().getUsername());
        deleteRequestStmt.executeUpdate();

        String deleteFriend = "DELETE FROM friends " +
                "WHERE (requester = ? AND acceptor = ?) OR (requester = ? AND acceptor = ?);";

        PreparedStatement deleteStmt = conn.prepareStatement(deleteFriend);
        deleteStmt.setString(1, block.getAuth().getUsername());
        deleteStmt.setString(2, block.getBlock());
        deleteStmt.setString(3, block.getBlock());
        deleteStmt.setString(4, block.getAuth().getUsername());
        deleteStmt.executeUpdate();

        //add block
        String insertBlock = "INSERT INTO blocked (requester, blocked) " +
                "VALUES (?,?);";

        PreparedStatement insertStmt = conn.prepareStatement(insertBlock);
        insertStmt.setString(1, block.getAuth().getUsername());
        insertStmt.setString(2, block.getBlock());
        insertStmt.executeUpdate();

        return false;
    }

    @Override
    public boolean unblockUser(Unblock unblock) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(unblock.getAuth())) {
            return false;
        }
        return false;
    }

    @Override
    public boolean sendMessage(SendMessage sendMessage) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(sendMessage.getAuth())) {
            return false;
        }
        return false;
    }

    @Override
    public boolean isBlocked(String requester, String other) {
        return false;
    }

    @Override
    public String[] listFriends(Auth auth) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(auth)) {
            return null;
        }
        return new String[0];
    }

    @Override
    public String[] listBlocked(Auth auth) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(auth)) {
            return null;
        }
        return new String[0];
    }

    @Override
    public String[] listIncomingFriendRequests(Auth auth) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(auth)) {
            return null;
        }
        return new String[0];
    }

    @Override
    public String[] listOutgoingFriendRequests(Auth auth) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(auth)) {
            return null;
        }
        return new String[0];
    }

    @Override
    public Message[] listMessages(ReadMessages readMessages) throws SQLException {
        //if no auth, return false
        if(!accountDAO.isAuthenticated(readMessages.getAuth())) {
            return null;
        }
        return new Message[0];
    }
}
