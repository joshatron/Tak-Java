package io.joshatron.tak.server;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

//Suite B
//Current final test: 088
public class SocialTest {

    //Create Friend Request
    //Test 001
    @Test
    public void friendRequest_BasicRequest_204Listed() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //Create Users
        HttpResponse response = HttpUtils.createUser("B00101", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        response = HttpUtils.createUser("B00102", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Request friend
        response = HttpUtils.requestFriend("B00101", "password", "B00102", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Verify listed
        response = HttpUtils.checkIncomingFriendRequests("B00102", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertTrue(EntityUtils.toString(response.getEntity()).contains("B00101"));
    }

    //Test 002
    @Test
    public void friendRequest_InvalidUser_403() throws IOException {
        HttpClient client = HttpUtils.createHttpClient();
        //Create Users
        HttpResponse response = HttpUtils.createUser("B00201", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        response = HttpUtils.createUser("B00202", "password", client);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, response.getStatusLine().getStatusCode());
        //Request friend
        response = HttpUtils.requestFriend("B00203", "password", "B00202", client);
        Assert.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());
        //Verify listed
        response = HttpUtils.checkIncomingFriendRequests("B00203", "password", client);
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        Assert.assertFalse(EntityUtils.toString(response.getEntity()).contains("B0020"));
    }

    //Test 003
    @Test
    public void friendRequest_BlockedUser_403() {

    }

    //Test 004
    @Test
    public void friendRequest_InvalidCredentials_403() {

    }

    //Test 005
    @Test
    public void friendRequest_RequestAlreadyMade_403() {

    }

    //Test 006
    @Test
    public void friendRequest_AlreadyFriends_403() {

    }

    //Test 007
    @Test
    public void friendRequest_RequestYourself_403() {

    }

    //Test 088
    @Test
    public void friendRequest_RequestInvalidUser_403() {

    }

    //Cancel Friend Request
    //Test 008
    @Test
    public void cancelFriendRequest_BasicRequest_204RequestRemoved() {

    }

    //Test 009
    @Test
    public void cancelFriendRequest_InvalidUser_403() {

    }

    //Test 010
    @Test
    public void cancelFriendRequest_InvalidCredentials_403() {

    }

    //Test 011
    @Test
    public void cancelFriendRequest_RequestNotMade_403() {

    }

    //Respond to Friend Request
    //Test 012
    @Test
    public void respondToFriendRequest_BasicAccept_204FriendAddedRequestRemoved() {

    }

    //Test 013
    @Test
    public void respondToFriendRequest_BasicDeny_204FriendNotAddedRequestRemoved() {

    }

    //Test 087
    @Test
    public void respondToFriendRequest_BadFormattedAnswer_403() {

    }

    //Test 014
    @Test
    public void respondToFriendRequest_InvalidUser_403() {

    }

    //Test 015
    @Test
    public void respondToFriendRequest_InvalidCredentials_403() {

    }

    //Test 016
    @Test
    public void respondToFriendRequest_RequestNotMade_403() {

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
