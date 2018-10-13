package io.joshatron.tak.server;

import io.joshatron.tak.server.utils.AccountUtils;
import io.joshatron.tak.server.utils.HttpUtils;
import io.joshatron.tak.server.utils.SocialUtils;
import io.joshatron.tak.server.utils.User;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
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
    //Test 008
    @Test
    public void cancelFriendRequest_BasicRequest_204RequestRemoved() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //Create Users
        HttpResponse response = HttpUtils.createUser("B00801", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        response = HttpUtils.createUser("B00802", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Request friend
        response = HttpUtils.requestFriend("B00801", "password", "B00802", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Verify listed
        response = HttpUtils.checkIncomingFriendRequests("B00802", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("B00801"));
        //Cancel request
        response = HttpUtils.cancelFriendRequest("B00801", "password", "B00802", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Verify not listed
        response = HttpUtils.checkIncomingFriendRequests("B00802", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertFalse(EntityUtils.toString(response.getEntity()).contains("B00801"));
    }

    //Test 009
    @Test
    public void cancelFriendRequest_InvalidUser_403() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //Create Users
        HttpResponse response = HttpUtils.createUser("B00901", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        response = HttpUtils.createUser("B00902", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Request friend
        response = HttpUtils.requestFriend("B00901", "password", "B00902", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Verify listed
        response = HttpUtils.checkIncomingFriendRequests("B00902", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("B00901"));
        //Cancel request of invalid
        response = HttpUtils.cancelFriendRequest("B00903", "password", "B00902", client);
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());
        //Verify still listed
        response = HttpUtils.checkIncomingFriendRequests("B00902", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("B00901"));
    }

    //Test 089
    @Test
    public void cancelFriendRequest_CancelInvalidUser_403() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //Create Users
        HttpResponse response = HttpUtils.createUser("B08901", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        response = HttpUtils.createUser("B08902", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Request friend
        response = HttpUtils.requestFriend("B08901", "password", "B08902", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Verify listed
        response = HttpUtils.checkIncomingFriendRequests("B08902", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("B08901"));
        //Cancel request of invalid
        response = HttpUtils.cancelFriendRequest("B08901", "password", "B08903", client);
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());
        //Verify still listed
        response = HttpUtils.checkIncomingFriendRequests("B08902", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("B08901"));
    }

    //Test 010
    @Test
    public void cancelFriendRequest_InvalidCredentials_403() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //Create Users
        HttpResponse response = HttpUtils.createUser("B01001", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        response = HttpUtils.createUser("B01002", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Request friend
        response = HttpUtils.requestFriend("B01001", "password", "B01002", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Verify listed
        response = HttpUtils.checkIncomingFriendRequests("B01002", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("B01001"));
        //Cancel request of invalid
        response = HttpUtils.cancelFriendRequest("B01001", "drowssap", "B01002", client);
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());
        //Verify still listed
        response = HttpUtils.checkIncomingFriendRequests("B01002", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("B01001"));
    }

    //Test 011
    @Test
    public void cancelFriendRequest_RequestNotMade_403() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //Create Users
        HttpResponse response = HttpUtils.createUser("B01101", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        response = HttpUtils.createUser("B01102", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Cancel request of invalid
        response = HttpUtils.cancelFriendRequest("B01101", "password", "B01102", client);
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());
        //Verify not listed
        response = HttpUtils.checkIncomingFriendRequests("B01102", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertFalse(EntityUtils.toString(response.getEntity()).contains("B01101"));
    }

    //Respond to Friend Request
    //Test 012
    @Test
    public void respondToFriendRequest_BasicAccept_204FriendAddedRequestRemoved() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //Create Users
        HttpResponse response = HttpUtils.createUser("B01201", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        response = HttpUtils.createUser("B01202", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Request friend
        response = HttpUtils.requestFriend("B01201", "password", "B01202", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Verify listed
        response = HttpUtils.checkIncomingFriendRequests("B01202", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("B01201"));
        //Accept request
        response = HttpUtils.respondToRequest("B01202", "password", "B01201", "accept", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Verify friends
        response = HttpUtils.getFriends("B01201", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("B01202"));
        //Verify request gone
        response = HttpUtils.checkIncomingFriendRequests("B01202", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertFalse(EntityUtils.toString(response.getEntity()).contains("B01201"));
    }

    //Test 013
    @Test
    public void respondToFriendRequest_BasicDeny_204FriendNotAddedRequestRemoved() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //Create Users
        HttpResponse response = HttpUtils.createUser("B01301", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        response = HttpUtils.createUser("B01302", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Request friend
        response = HttpUtils.requestFriend("B01301", "password", "B01302", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Verify listed
        response = HttpUtils.checkIncomingFriendRequests("B01302", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("B01301"));
        //Deny request
        response = HttpUtils.respondToRequest("B01302", "password", "B01301", "deny", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Verify not friends
        response = HttpUtils.getFriends("B01301", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertFalse(EntityUtils.toString(response.getEntity()).contains("B01302"));
        //Verify request gone
        response = HttpUtils.checkIncomingFriendRequests("B01302", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertFalse(EntityUtils.toString(response.getEntity()).contains("B01301"));
    }

    //Test 087
    @Test
    public void respondToFriendRequest_BadFormattedAnswer_403() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //Create Users
        HttpResponse response = HttpUtils.createUser("B08701", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        response = HttpUtils.createUser("B08702", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Request friend
        response = HttpUtils.requestFriend("B08701", "password", "B08702", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Verify listed
        response = HttpUtils.checkIncomingFriendRequests("B08702", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("B08701"));
        //Invalid request
        response = HttpUtils.respondToRequest("B08702", "password", "B08701", "blah", client);
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());
        //Verify not friends
        response = HttpUtils.getFriends("B08701", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertFalse(EntityUtils.toString(response.getEntity()).contains("B08702"));
        //Verify request there
        response = HttpUtils.checkIncomingFriendRequests("B08702", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("B08701"));
    }

    //Test 014
    @Test
    public void respondToFriendRequest_InvalidUser_403() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //Create Users
        HttpResponse response = HttpUtils.createUser("B01401", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        response = HttpUtils.createUser("B01402", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Request friend
        response = HttpUtils.requestFriend("B01401", "password", "B01402", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Verify listed
        response = HttpUtils.checkIncomingFriendRequests("B01402", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("B01401"));
        //Invalid request
        response = HttpUtils.respondToRequest("B01403", "password", "B01401", "accept", client);
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());
        //Verify not friends
        response = HttpUtils.getFriends("B01401", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertFalse(EntityUtils.toString(response.getEntity()).contains("B01402"));
        //Verify request there
        response = HttpUtils.checkIncomingFriendRequests("B01402", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("B01401"));
    }

    //Test 015
    @Test
    public void respondToFriendRequest_InvalidCredentials_403() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //Create Users
        HttpResponse response = HttpUtils.createUser("B01501", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        response = HttpUtils.createUser("B01502", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Request friend
        response = HttpUtils.requestFriend("B01501", "password", "B01502", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Verify listed
        response = HttpUtils.checkIncomingFriendRequests("B01502", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("B01501"));
        //Invalid request
        response = HttpUtils.respondToRequest("B01502", "drowssap", "B01501", "accept", client);
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());
        //Verify not friends
        response = HttpUtils.getFriends("B01501", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertFalse(EntityUtils.toString(response.getEntity()).contains("B01502"));
        //Verify request there
        response = HttpUtils.checkIncomingFriendRequests("B01502", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("B01501"));
    }

    //Test 016
    @Test
    public void respondToFriendRequest_RequestNotMade_403() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //Create Users
        HttpResponse response = HttpUtils.createUser("B01601", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        response = HttpUtils.createUser("B01602", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Invalid request
        response = HttpUtils.respondToRequest("B01602", "password", "B01601", "accept", client);
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());
        //Verify not friends
        response = HttpUtils.getFriends("B01601", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertFalse(EntityUtils.toString(response.getEntity()).contains("B01602"));
    }

    //Check Incoming Requests
    //Test 017
    @Test
    public void checkIncomingRequests_NoIncoming_200BlankArray() {

    }

    //Test 018
    @Test
    public void checkIncomingRequest_OneIncoming_200ArrayWithOne() {

    }

    //Test 019
    @Test
    public void checkIncomingRequest_MultipleIncoming_200ArrayWithMultiple() {

    }

    //Test 020
    @Test
    public void checkIncomingRequest_OneOutgoing_200BlankArray() {

    }

    //Test 021
    @Test
    public void checkIncomingRequest_InvalidUser_403() {

    }

    //Test 022
    @Test
    public void checkIncomingRequest_InvalidCredentials_403() {

    }

    //Check Outgoing Requests
    //Test 023
    @Test
    public void checkOutgoingRequests_NoOutgoing_200BlankArray() {

    }

    //Test 024
    @Test
    public void checkOutgoingRequest_OneOutgoing_200ArrayWithOne() {

    }

    //Test 025
    @Test
    public void checkOutgoingRequest_MultipleOutgoing_200ArrayWithMultiple() {

    }

    //Test 026
    @Test
    public void checkOutgoingRequest_OneIncoming_200BlankArray() {

    }

    //Test 027
    @Test
    public void checkOutgoingRequest_InvalidUser_403() {

    }

    //Test 028
    @Test
    public void checkOutgoingRequest_InvalidCredentials_403() {

    }

    //Block User
    //Test 029
    @Test
    public void blockUser_BlockNonFriend_204BlockAdded() {

    }

    //Test 030
    @Test
    public void blockUser_BlockFriend_204FriendRemovedBlockAdded() {

    }

    //Test 031
    @Test
    public void blockUser_BlockWhileRequested_204RequestRemovedBlockAdded() {

    }

    //Test 032
    @Test
    public void blockUser_BlockWhileRequesting_204RequestRemovedBlockAdded() {

    }

    //Test 033
    @Test
    public void blockUser_BlockWhileBlocked_204BlockAdded() {

    }

    //Test 034
    @Test
    public void blockUser_InvalidUser_403() {

    }

    //Test 035
    @Test
    public void blockUser_InvalidCredentials_403() {

    }

    //Test 036
    @Test
    public void blockUser_BlockingInvalidUser_403() {

    }

    //Test 037
    @Test
    public void blockUser_BlockingAlreadyBlocked_403() {

    }

    //Test 038
    @Test
    public void blockUser_BlockingSelf_403() {

    }

    //Unblock User
    //Test 039
    @Test
    public void unblockUser_Normal_204BlockRemoved() {

    }

    //Test 040
    @Test
    public void unblockUser_UnblockedWhileBlocked_204OnlyYourBlockRemoved() {

    }

    //Test 041
    @Test
    public void unblockUser_UnblockWhileNoBlock_403() {

    }

    //Test 042
    @Test
    public void unblockUser_InvalidUser_403() {

    }

    //Test 043
    @Test
    public void unblockUser_InvalidCredentials_403() {

    }

    //Test 044
    @Test
    public void unblockUser_InvalidUserToUnblock_403() {

    }

    //List Friends
    //Test 045
    @Test
    public void listFriends_NoFriends_200EmptyArray() {

    }

    //Test 046
    @Test
    public void listFriends_OneFriend_200ArrayWithOne() {

    }

    //Test 047
    @Test
    public void listFriends_MultipleFriends_200ArrayWithMultiple() {

    }

    //Test 048
    @Test
    public void listFriends_CreatedByYou_200ArrayWithOne() {

    }

    //Test 049
    @Test
    public void listFriends_CreatedByOther_200ArrayWithOne() {

    }

    //Test 050
    @Test
    public void listFriends_IncomingRequest_200EmptyArray() {

    }

    //Test 051
    @Test
    public void listFriends_OutgoingRequest_200EmptyArray() {

    }

    //Test 052
    @Test
    public void listFriends_InvalidUser_403() {

    }

    //Test 053
    @Test
    public void listFriends_InvalidCredentials_403() {

    }

    //List Blocked Users
    //Test 054
    @Test
    public void listBlocked_NoBlocked_200EmptyArray() {

    }

    //Test 055
    @Test
    public void listBlocked_OneBlocked_200ArrayWithOne() {

    }

    //Test 056
    @Test
    public void listBlocked_MultipleBlocked_200ArrayWithMultiple() {

    }

    //Test 057
    @Test
    public void listBlocked_BeenBlocked_200EmptyArray() {

    }

    //Test 058
    @Test
    public void listBlocked_InvalidUser_403() {

    }

    //Test 059
    @Test
    public void listBlocked_InvalidCredentials_403() {

    }

    //Send a Message
    //Test 060
    @Test
    public void sendMessage_NormalMessage_204MessageSent() {

    }

    //Test 061
    @Test
    public void sendMessage_EmptyMessage_403NotSent() {

    }

    //Test 062
    @Test
    public void sendMessage_InvalidRecipient_403NotSent() {

    }

    //Test 063
    @Test
    public void sendMessage_InvalidUser_403NotSent() {

    }

    //Test 064
    @Test
    public void sendMessage_InvalidCredentials_403NotSent() {

    }

    //Test 065
    @Test
    public void sendMessage_SentToBlocked_403NotSent() {

    }

    //Test 066
    @Test
    public void sendMessage_SentToNonFriend_204Sent() {

    }

    //Test 067
    @Test
    public void sendMessage_SentToFriend_204Sent() {

    }

    //Test 068
    @Test
    public void sendMessage_SuperLongMessage_403NotSent() {

    }

    //Read Messages
    //Test 069
    @Test
    public void readMessages_NoParameters_200AllMessages() {

    }

    //Test 070
    @Test
    public void readMessages_InvalidUser_403() {

    }

    //Test 071
    @Test
    public void readMessages_InvalidCredentials_403() {

    }

    //Test 072
    @Test
    public void readMessages_OneSender_200MessagesFromSender() {

    }

    //Test 073
    @Test
    public void readMessages_MultipleSenders_200MessagesFromSenders() {

    }

    //Test 074
    @Test
    public void readMessages_NormalStart_200MessagesAfterStart() {

    }

    //Test 075
    @Test
    public void readMessages_StartAtCurrent_200NoMessages() {

    }

    //Test 076
    @Test
    public void readMessages_StartAtFuture_403() {

    }

    //Test 077
    @Test
    public void readMessages_OnlyUnread_200OnlyUnreadMessages() {

    }

    //Test 078
    @Test
    public void readMessages_OnlyRead_200OnlyReadMessages() {

    }

    //Test 079
    @Test
    public void readMessages_ReadBadFormat_403() {

    }

    //Test 080
    @Test
    public void readMessages_StartBadFormat_403() {

    }

    //Test 081
    @Test
    public void readMessages_InvalidSender_403() {

    }

    //Test 082
    @Test
    public void readMessages_SenderNoMessages_200NoMessages() {

    }

    //Test 083
    @Test
    public void readMessages_SenderAndStart_200MessagesFromSendersFromStart() {

    }

    //Test 084
    @Test
    public void readMessages_SenderAndRead_200MessagesFromSendersThatAreRead() {

    }

    //Test 085
    @Test
    public void readMessages_StartAndRead_200MessagesStartingAtStartThatAreRead() {

    }

    //Test 086
    @Test
    public void readMessages_SendersAndStartAndRead_200MessagesFittingQuery() {

    }
}
