package io.joshatron.tak.server;

import org.junit.Test;

//Suite B
public class SocialTest {

    //Create Friend Request
    //Test 001
    @Test
    public void friendRequest_BasicRequest_204Listed() {

    }

    //Test 002
    @Test
    public void friendRequest_InvalidUser_403() {

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

    //Cancel Friend Request
    //Test 008
    @Test
    public void cancelFriendRequest_BasicRequest_204() {

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
    public void respondToFriendRequest_BasicAccept_204() {

    }

    //Test 013
    @Test
    public void respondToFriendRequest_BasicDeny_204() {

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
    //Test 017
    @Test
    public void checkOutgoingRequests_NoOutgoing_200BlankArray() {

    }

    //Test 018
    @Test
    public void checkOutgoingRequest_OneOutgoing_200ArrayWithOne() {

    }

    //Test 019
    @Test
    public void checkOutgoingRequest_MultipleOutgoing_200ArrayWithMultiple() {

    }

    //Test 020
    @Test
    public void checkOutgoingRequest_OneIncoming_200BlankArray() {

    }

    //Test 021
    @Test
    public void checkOutgoingRequest_InvalidUser_403() {

    }

    //Test 022
    @Test
    public void checkOutgoingRequest_InvalidCredentials_403() {

    }

    //Block User

    //Unblock User

    //List Friends

    //List Blocked Users

    //Send a Message

    //Read Messages
}
