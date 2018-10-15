package io.joshatron.tak.server;

import io.joshatron.tak.server.utils.AccountUtils;
import io.joshatron.tak.server.utils.HttpUtils;
import io.joshatron.tak.server.utils.SocialUtils;
import io.joshatron.tak.server.utils.User;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.junit.Test;

import java.io.IOException;

//Current final test: 089
public class SocialTest {

    private final String suite = "B";
    private HttpClient client;

    public SocialTest() {
        client = HttpUtils.createHttpClient();
    }

    //Create Friend Request
    @Test
    public void friendRequest_BasicRequest_204Listed() throws IOException {
        String test = "001";
        User user1 = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        User user2 = AccountUtils.addUser(suite, test, "02", "password", client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.requestFriend(user1, user2, client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, new User[]{user1}, null);
    }

    @Test
    public void friendRequest_InvalidUser_403() throws IOException {
        String test = "002";
        User user1 = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        User user2 = AccountUtils.addUser(suite, test, "02", "password", client, HttpStatus.SC_NO_CONTENT);
        User user3 = new User(suite + test + "03", "password");
        SocialUtils.requestFriend(user3, user2, client, HttpStatus.SC_FORBIDDEN);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, null, new User[]{user3});
    }

    @Test
    public void friendRequest_BlockedUser_403() throws IOException {
        String test = "003";
        User user1 = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        User user2 = AccountUtils.addUser(suite, test, "02", "password", client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.blockUser(user2, user1, client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.requestFriend(user1, user2, client, HttpStatus.SC_FORBIDDEN);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, null, new User[]{user1});
    }

    @Test
    public void friendRequest_InvalidCredentials_403() throws IOException {
        String test = "004";
        User user1 = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        User user2 = AccountUtils.addUser(suite, test, "02", "password", client, HttpStatus.SC_NO_CONTENT);
        user1.setPassword("drowssap");
        SocialUtils.requestFriend(user1, user2, client, HttpStatus.SC_FORBIDDEN);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, null, new User[]{user1});
    }

    @Test
    public void friendRequest_RequestAlreadyMade_403() throws IOException {
        String test = "005";
        User user1 = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        User user2 = AccountUtils.addUser(suite, test, "02", "password", client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.requestFriend(user1, user2, client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, new User[]{user1}, null);
        SocialUtils.requestFriend(user1, user2, client, HttpStatus.SC_FORBIDDEN);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, new User[]{user1}, null);
    }

    @Test
    public void friendRequest_AlreadyFriends_403() throws IOException {
        String test = "006";
        User user1 = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        User user2 = AccountUtils.addUser(suite, test, "02", "password", client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.requestFriend(user1, user2, client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, new User[]{user1}, null);
        SocialUtils.respondToRequest(user2, user1, "accept", client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.requestFriend(user1, user2, client, HttpStatus.SC_FORBIDDEN);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, null, new User[]{user1});
    }

    @Test
    public void friendRequest_RequestYourself_403() throws IOException {
        String test = "007";
        User user = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.requestFriend(user, user, client, HttpStatus.SC_FORBIDDEN);
        SocialUtils.checkIncoming(user, client, HttpStatus.SC_OK, null, new User[]{user});
    }

    @Test
    public void friendRequest_RequestInvalidUser_403() throws IOException {
        String test = "088";
        User user1 = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        User user2 = new User(suite + test + "02", "password");
        SocialUtils.requestFriend(user1, user2, client, HttpStatus.SC_FORBIDDEN);
        SocialUtils.checkOutgoing(user1, client, HttpStatus.SC_OK, null, new User[]{user2});
    }

    //Cancel Friend Request
    @Test
    public void cancelFriendRequest_BasicRequest_204RequestRemoved() throws IOException {
        String test = "008";
        User user1 = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        User user2 = AccountUtils.addUser(suite, test, "02", "password", client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.requestFriend(user1, user2, client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, new User[]{user1}, null);
        SocialUtils.cancelRequest(user1, user2, client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, null, new User[]{user1});
    }

    @Test
    public void cancelFriendRequest_InvalidUser_403() throws IOException {
        String test = "009";
        User user1 = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        User user2 = AccountUtils.addUser(suite, test, "02", "password", client, HttpStatus.SC_NO_CONTENT);
        User user3 = new User(suite + test + "03", "password");
        SocialUtils.requestFriend(user1, user2, client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, new User[]{user1}, null);
        SocialUtils.cancelRequest(user3, user2, client, HttpStatus.SC_FORBIDDEN);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, new User[]{user1}, null);
    }

    @Test
    public void cancelFriendRequest_CancelInvalidUser_403() throws IOException {
        String test = "089";
        User user1 = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        User user2 = AccountUtils.addUser(suite, test, "02", "password", client, HttpStatus.SC_NO_CONTENT);
        User user3 = new User(suite + test + "03", "password");
        SocialUtils.requestFriend(user1, user2, client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, new User[]{user1}, null);
        SocialUtils.cancelRequest(user1, user3, client, HttpStatus.SC_FORBIDDEN);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, new User[]{user1}, null);
    }

    @Test
    public void cancelFriendRequest_InvalidCredentials_403() throws IOException {
        String test = "010";
        User user1 = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        User user2 = AccountUtils.addUser(suite, test, "02", "password", client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.requestFriend(user1, user2, client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, new User[]{user1}, null);
        user1.setPassword("drowssap");
        SocialUtils.cancelRequest(user1, user2, client, HttpStatus.SC_FORBIDDEN);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, new User[]{user1}, null);
    }

    @Test
    public void cancelFriendRequest_RequestNotMade_403() throws IOException {
        String test = "011";
        User user1 = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        User user2 = AccountUtils.addUser(suite, test, "02", "password", client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.cancelRequest(user1, user2, client, HttpStatus.SC_FORBIDDEN);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, null, new User[]{user1});
    }

    //Respond to Friend Request
    @Test
    public void respondToFriendRequest_BasicAccept_204FriendAddedRequestRemoved() throws IOException {
        String test = "012";
        User user1 = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        User user2 = AccountUtils.addUser(suite, test, "02", "password", client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.requestFriend(user1, user2, client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, new User[]{user1}, null);
        SocialUtils.respondToRequest(user2, user1, "accept", client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.checkFriends(user2, client, HttpStatus.SC_OK, new User[]{user1}, null);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, null, new User[]{user1});
    }

    @Test
    public void respondToFriendRequest_BasicDeny_204FriendNotAddedRequestRemoved() throws IOException {
        String test = "013";
        User user1 = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        User user2 = AccountUtils.addUser(suite, test, "02", "password", client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.requestFriend(user1, user2, client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, new User[]{user1}, null);
        SocialUtils.respondToRequest(user2, user1, "deny", client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.checkFriends(user2, client, HttpStatus.SC_OK, null, new User[]{user1});
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, null, new User[]{user1});
    }

    @Test
    public void respondToFriendRequest_BadFormattedAnswer_403() throws IOException {
        String test = "087";
        User user1 = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        User user2 = AccountUtils.addUser(suite, test, "02", "password", client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.requestFriend(user1, user2, client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, new User[]{user1}, null);
        SocialUtils.respondToRequest(user2, user1, "blah", client, HttpStatus.SC_FORBIDDEN);
        SocialUtils.checkFriends(user2, client, HttpStatus.SC_OK, null, new User[]{user1});
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, new User[]{user1}, null);
    }

    @Test
    public void respondToFriendRequest_InvalidUser_403() throws IOException {
        String test = "014";
        User user1 = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        User user2 = AccountUtils.addUser(suite, test, "02", "password", client, HttpStatus.SC_NO_CONTENT);
        User user3 = new User(suite + test + "03", "password");
        SocialUtils.requestFriend(user1, user2, client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, new User[]{user1}, null);
        SocialUtils.respondToRequest(user3, user1, "accept", client, HttpStatus.SC_FORBIDDEN);
        SocialUtils.checkFriends(user2, client, HttpStatus.SC_OK, null, new User[]{user1});
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, new User[]{user1}, null);
    }

    @Test
    public void respondToFriendRequest_InvalidCredentials_403() throws IOException {
        String test = "015";
        User user1 = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        User user2 = AccountUtils.addUser(suite, test, "02", "password", client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.requestFriend(user1, user2, client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, new User[]{user1}, null);
        user2.setPassword("drowssap");
        SocialUtils.respondToRequest(user2, user1, "accept", client, HttpStatus.SC_FORBIDDEN);
        user2.setPassword("password");
        SocialUtils.checkFriends(user2, client, HttpStatus.SC_OK, null, new User[]{user1});
        SocialUtils.checkIncoming(user2, client, HttpStatus.SC_OK, new User[]{user1}, null);
    }

    @Test
    public void respondToFriendRequest_RequestNotMade_403() throws IOException {
        String test = "016";
        User user1 = AccountUtils.addUser(suite, test, "01", "password", client, HttpStatus.SC_NO_CONTENT);
        User user2 = AccountUtils.addUser(suite, test, "02", "password", client, HttpStatus.SC_NO_CONTENT);
        SocialUtils.respondToRequest(user2, user1, "accept", client, HttpStatus.SC_FORBIDDEN);
        SocialUtils.checkFriends(user2, client, HttpStatus.SC_OK, null, new User[]{user1});
    }

    //Check Incoming Requests
    @Test
    public void checkIncomingRequests_NoIncoming_200BlankArray() {
        String test = "017";
    }

    @Test
    public void checkIncomingRequest_OneIncoming_200ArrayWithOne() {
        String test = "018";
    }

    @Test
    public void checkIncomingRequest_MultipleIncoming_200ArrayWithMultiple() {
        String test = "019";
    }

    @Test
    public void checkIncomingRequest_OneOutgoing_200BlankArray() {
        String test = "020";
    }

    @Test
    public void checkIncomingRequest_InvalidUser_403() {
        String test = "021";
    }

    @Test
    public void checkIncomingRequest_InvalidCredentials_403() {
        String test = "022";
    }

    //Check Outgoing Requests
    @Test
    public void checkOutgoingRequests_NoOutgoing_200BlankArray() {
        String test = "023";
    }

    @Test
    public void checkOutgoingRequest_OneOutgoing_200ArrayWithOne() {
        String test = "024";
    }

    @Test
    public void checkOutgoingRequest_MultipleOutgoing_200ArrayWithMultiple() {
        String test = "025";
    }

    @Test
    public void checkOutgoingRequest_OneIncoming_200BlankArray() {
        String test = "026";
    }

    @Test
    public void checkOutgoingRequest_InvalidUser_403() {
        String test = "027";
    }

    @Test
    public void checkOutgoingRequest_InvalidCredentials_403() {
        String test = "028";
    }

    //Block User
    @Test
    public void blockUser_BlockNonFriend_204BlockAdded() {
        String test = "029";
    }

    @Test
    public void blockUser_BlockFriend_204FriendRemovedBlockAdded() {
        String test = "030";
    }

    @Test
    public void blockUser_BlockWhileRequested_204RequestRemovedBlockAdded() {
        String test = "031";
    }

    @Test
    public void blockUser_BlockWhileRequesting_204RequestRemovedBlockAdded() {
        String test = "032";
    }

    @Test
    public void blockUser_BlockWhileBlocked_204BlockAdded() {
        String test = "033";
    }

    @Test
    public void blockUser_InvalidUser_403() {
        String test = "034";
    }

    @Test
    public void blockUser_InvalidCredentials_403() {
        String test = "035";
    }

    @Test
    public void blockUser_BlockingInvalidUser_403() {
        String test = "036";
    }

    @Test
    public void blockUser_BlockingAlreadyBlocked_403() {
        String test = "037";
    }

    @Test
    public void blockUser_BlockingSelf_403() {
        String test = "038";
    }

    //Unblock User
    @Test
    public void unblockUser_Normal_204BlockRemoved() {
        String test = "039";
    }

    @Test
    public void unblockUser_UnblockedWhileBlocked_204OnlyYourBlockRemoved() {
        String test = "040";
    }

    @Test
    public void unblockUser_UnblockWhileNoBlock_403() {
        String test = "041";
    }

    @Test
    public void unblockUser_InvalidUser_403() {
        String test = "042";
    }

    @Test
    public void unblockUser_InvalidCredentials_403() {
        String test = "043";
    }

    @Test
    public void unblockUser_InvalidUserToUnblock_403() {
        String test = "044";
    }

    //List Friends
    @Test
    public void listFriends_NoFriends_200EmptyArray() {
        String test = "045";
    }

    @Test
    public void listFriends_OneFriend_200ArrayWithOne() {
        String test = "046";
    }

    @Test
    public void listFriends_MultipleFriends_200ArrayWithMultiple() {
        String test = "047";
    }

    @Test
    public void listFriends_CreatedByYou_200ArrayWithOne() {
        String test = "048";
    }

    @Test
    public void listFriends_CreatedByOther_200ArrayWithOne() {
        String test = "049";
    }

    @Test
    public void listFriends_IncomingRequest_200EmptyArray() {
        String test = "050";
    }

    @Test
    public void listFriends_OutgoingRequest_200EmptyArray() {
        String test = "051";
    }

    @Test
    public void listFriends_InvalidUser_403() {
        String test = "052";
    }

    @Test
    public void listFriends_InvalidCredentials_403() {
        String test = "053";
    }

    //List Blocked Users
    @Test
    public void listBlocked_NoBlocked_200EmptyArray() {
        String test = "054";
    }

    @Test
    public void listBlocked_OneBlocked_200ArrayWithOne() {
        String test = "055";
    }

    @Test
    public void listBlocked_MultipleBlocked_200ArrayWithMultiple() {
        String test = "056";
    }

    @Test
    public void listBlocked_BeenBlocked_200EmptyArray() {
        String test = "057";
    }

    @Test
    public void listBlocked_InvalidUser_403() {
        String test = "058";
    }

    @Test
    public void listBlocked_InvalidCredentials_403() {
        String test = "059";
    }

    //Send a Message
    @Test
    public void sendMessage_NormalMessage_204MessageSent() {
        String test = "060";
    }

    @Test
    public void sendMessage_EmptyMessage_403NotSent() {
        String test = "061";
    }

    @Test
    public void sendMessage_InvalidRecipient_403NotSent() {
        String test = "062";
    }

    @Test
    public void sendMessage_InvalidUser_403NotSent() {
        String test = "063";
    }

    @Test
    public void sendMessage_InvalidCredentials_403NotSent() {
        String test = "064";
    }

    @Test
    public void sendMessage_SentToBlocked_403NotSent() {
        String test = "065";
    }

    @Test
    public void sendMessage_SentToNonFriend_204Sent() {
        String test = "066";
    }

    @Test
    public void sendMessage_SentToFriend_204Sent() {
        String test = "067";
    }

    @Test
    public void sendMessage_SuperLongMessage_403NotSent() {
        String test = "068";
    }

    //Read Messages
    @Test
    public void readMessages_NoParameters_200AllMessages() {
        String test = "069";
    }

    @Test
    public void readMessages_InvalidUser_403() {
        String test = "070";
    }

    @Test
    public void readMessages_InvalidCredentials_403() {
        String test = "071";
    }

    @Test
    public void readMessages_OneSender_200MessagesFromSender() {
        String test = "072";
    }

    @Test
    public void readMessages_MultipleSenders_200MessagesFromSenders() {
        String test = "073";
    }

    @Test
    public void readMessages_NormalStart_200MessagesAfterStart() {
        String test = "074";
    }

    @Test
    public void readMessages_StartAtCurrent_200NoMessages() {
        String test = "075";
    }

    @Test
    public void readMessages_StartAtFuture_403() {
        String test = "076";
    }

    @Test
    public void readMessages_OnlyUnread_200OnlyUnreadMessages() {
        String test = "077";
    }

    @Test
    public void readMessages_OnlyRead_200OnlyReadMessages() {
        String test = "078";
    }

    @Test
    public void readMessages_ReadBadFormat_403() {
        String test = "079";
    }

    @Test
    public void readMessages_StartBadFormat_403() {
        String test = "080";
    }

    @Test
    public void readMessages_InvalidSender_403() {
        String test = "081";
    }

    @Test
    public void readMessages_SenderNoMessages_200NoMessages() {
        String test = "082";
    }

    @Test
    public void readMessages_SenderAndStart_200MessagesFromSendersFromStart() {
        String test = "083";
    }

    @Test
    public void readMessages_SenderAndRead_200MessagesFromSendersThatAreRead() {
        String test = "084";
    }

    @Test
    public void readMessages_StartAndRead_200MessagesStartingAtStartThatAreRead() {
        String test = "085";
    }

    @Test
    public void readMessages_SendersAndStartAndRead_200MessagesFittingQuery() {
        String test = "086";
    }
}
