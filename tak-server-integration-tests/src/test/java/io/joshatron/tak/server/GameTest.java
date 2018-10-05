package io.joshatron.tak.server;

import org.junit.Test;

//Suite C
//Current final test: 077
public class GameTest {

    //Request a Game
    //Test 001
    @Test
    public void requestGame_RequestFriend_204RequestMade() {

    }

    //Test 002
    @Test
    public void requestGame_RequestNonFriend_403RequestNotMade() {

    }

    //Test 003
    @Test
    public void requestGame_RequestPendingFriend_403RequestNotMade() {

    }

    //Test 004
    @Test
    public void requestGame_RequestBlocked_403RequestNotMade() {

    }

    //Test 005
    @Test
    public void requestGame_RequestNonexistent_403RequestNotMade() {

    }

    //Test 006
    @Test
    public void requestGame_RequestWithExisting_403RequestNotMade() {

    }

    //Test 007
    @Test
    public void requestGame_RequestYourself_403RequestNotMade() {

    }

    //Test 008
    @Test
    public void requestGame_RequestExistingGame_403RequestNotMade() {

    }

    //Test 009
    @Test
    public void requestGame_InvalidUser_403RequestNotMade() {

    }

    //Test 010
    @Test
    public void requestGame_InvalidCredentials_403RequestNotMade() {

    }

    //Respond to Game Request
    //Test 011
    @Test
    public void respondToGameRequest_RespondAccept_204GameStartedRequestRemoved() {

    }

    //Test 012
    @Test
    public void respondToGameRequest_RespondDeny_204GameNotStartedRequestRemoved() {

    }

    //Test 013
    @Test
    public void respondToGameRequest_RespondBadFormatting_403GameNotStartedRequestStillThere() {

    }

    //Test 014
    @Test
    public void respondToGameRequest_RespondNoRequest_403NoGameStarted() {

    }

    //Test 015
    @Test
    public void respondToGameRequest_InvalidUser_403GameNotStartedRequestStillThere() {

    }

    //Test 016
    @Test
    public void respondToGameRequest_InvalidCredentials_403GameNotStartedRequestStillThere() {

    }

    //Check Incoming Game Request
    //Test 017
    @Test
    public void checkIncomingGames_NoRequests_200EmptyArray() {

    }

    //Test 018
    @Test
    public void checkIncomingGames_OneRequest_200ArrayWithOne() {

    }

    //Test 019
    @Test
    public void checkIncomingGames_MultipleRequests_200ArrayWithMultiple() {

    }

    //Test 020
    @Test
    public void checkIncomingGames_OneOutgoing_200EmptyArray() {

    }

    //Test 021
    @Test
    public void checkIncomingGames_InvalidUser_403() {

    }

    //Test 022
    @Test
    public void checkIncomingGames_InvalidCredentials_403() {

    }

    //Check Outgoing Game Request
    //Test 023
    @Test
    public void checkOutgoingGames_NoRequests_200EmptyArray() {

    }

    //Test 024
    @Test
    public void checkOutgoingGames_200ArrayWithOne() {

    }

    //Test 025
    @Test
    public void checkOutgoingGames_200ArrayWithMultiple() {

    }

    //Test 026
    @Test
    public void checkOutgoingGames_OneIncoming_200EmptyArray() {

    }

    //Test 027
    @Test
    public void checkOutgoingGames_InvalidUser_403() {

    }

    //Test 028
    @Test
    public void checkOutgoingGames_InvalidCredentials_403() {

    }

    //Request a Random Game
    //Test 029
    @Test
    public void requestRandomGame_OneRequest_204NoGameCreated() {

    }

    //Test 030
    @Test
    public void requestRandomGame_TwoMatchingRequests_204GameCreated() {

    }

    //Test 031
    @Test
    public void requestRandomGame_TwoMismatchingRequests_204NoGameCreated() {

    }

    //Test 032
    @Test
    public void requestRandomGame_TwoMatchingAlreadyInGame_204NoGameCreated() {

    }

    //Test 033
    @Test
    public void requestRandomGame_TwoFriendsMatching_204GameCreated() {

    }

    //Test 034
    @Test
    public void requestRandomGame_TwoRandomMatching_204GameCreated() {

    }

    //Test 035
    @Test
    public void requestRandomGame_TwoMatchingBlocked_204NoGameCreated() {

    }

    //Test 036
    @Test
    public void requestRandomGame_TwoMismatchThenThirdMatch_204OneGameCreated() {

    }

    //Test 037
    @Test
    public void requestRandomGame_TwoBlockedMatchedThenNonBlockMatch_204OneGameCreated() {

    }

    //Test 038
    @Test
    public void requestRandomGame_InvalidUser_403() {

    }

    //Test 039
    @Test
    public void requestRandomGame_InvalidCredentials_403() {

    }

    //Test 040
    @Test
    public void requestRandomGame_GameSizeIllegalNumber_403() {

    }

    //Test 041
    @Test
    public void requestRandomGame_GameSizeString_403() {

    }

    //List Complete Games
    //Test 042
    @Test
    public void listCompletedGames_NoGames_200EmptyArray() {

    }

    //Test 043
    @Test
    public void listCompletedGames_OneGame_200ArrayWithOne() {

    }

    //Test 044
    @Test
    public void listCompletedGames_MultipleGames_200ArrayWithMultiple() {

    }

    //Test 045
    @Test
    public void listCompletedGames_GameInProgress_200EmptyArray() {

    }

    //Test 046
    @Test
    public void listCompletedGames_OneOpponent_200GamesFromOpponent() {

    }

    //Test 047
    @Test
    public void listCompletedGames_MultipleOpponents_200GamesFromMultipleOpponents() {

    }

    //Test 048
    @Test
    public void listCompletedGames_StartInPast_200GamesFromPastOn() {

    }

    //Test 049
    @Test
    public void listCompletedGames_StartInCurrent_200EmptyGames() {

    }

    //Test 050
    @Test
    public void listCompletedGames_StartInFuture_403() {

    }

    //Test 051
    @Test
    public void listCompletedGames_OneSize_200GamesWithSize() {

    }

    //Test 052
    @Test
    public void listCompletedGames_SizeBadNumber_403() {

    }

    //Test 053
    @Test
    public void listCompletedGames_InvalidUser_403() {

    }

    //Test 054
    @Test
    public void listCompletedGames_InvalidCredentials_403() {

    }

    //List Incomplete Games
    //Test 055
    @Test
    public void listIncompleteGames_NoGames_200EmptyArray() {

    }

    //Test 056
    @Test
    public void listIncompleteGames_OneGame_200ArrayWithOne() {

    }

    //Test 057
    @Test
    public void listIncompleteGames_MultipleGames_200ArrayWithMultiple() {

    }

    //Test 058
    @Test
    public void listIncompleteGames_CompleteGame_200EmptyArray() {

    }

    //Test 059
    @Test
    public void listIncompleteGames_Pending_200OnlyPending() {

    }

    //Test 060
    @Test
    public void listIncompleteGames_NotPending_200OnlyNotPending() {

    }

    //Test 061
    @Test
    public void listIncompleteGames_InvalidPending_403() {

    }

    //Test 062
    @Test
    public void listIncompleteGames_InvalidUser_403() {

    }

    //Test 063
    @Test
    public void listIncompleteGames_InvalidCredentials_403() {

    }

    //Get Info on a Game
    //Test 064
    @Test
    public void getGame_ValidGame_200GameInfo() {

    }

    //Test 065
    @Test
    public void getGame_NotYourGame_403() {

    }

    //Test 066
    @Test
    public void getGame_NotRealGame_403() {

    }

    //Test 067
    @Test
    public void getGame_InvalidUser_403() {

    }

    //Test 068
    @Test
    public void getGame_InvalidCredentials_403() {

    }

    //Play Turn
    //Test 069
    @Test
    public void playTurn_YourTurn_200TurnMadeConfirmationOfTurn() {

    }

    //Test 070
    @Test
    public void playTurn_NotYourTurn_403TurnNotMade() {

    }

    //Test 071
    @Test
    public void playTurn_NotYourGame_403TurnNotMade() {

    }

    //Test 072
    @Test
    public void playTurn_InvalidGame_403() {

    }

    //Test 073
    @Test
    public void playTurn_IllegalTurn_200TurnNotMadeWithReason() {

    }

    //Test 074
    @Test
    public void playTurn_IllFormattedTurn_403TurnNotMade() {

    }

    //Test 075
    @Test
    public void playTurn_WinGame_200WinMessage() {

    }

    //Test 076
    @Test
    public void playTurn_InvalidUser_403TurnNotMade() {

    }

    //Test 077
    @Test
    public void playTurn_InvalidCredentials_403TurnNotMade() {

    }
}
