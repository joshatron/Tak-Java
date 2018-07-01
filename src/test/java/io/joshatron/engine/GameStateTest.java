package io.joshatron.engine;

import org.junit.Assert;
import org.junit.Test;

public class GameStateTest {
    //These tests are set up to be closer to black box testing.
    //This is done to make sure no rules can be broken instead of focusing on line coverage.
    //Assert.assertTrue is around every execute turn to make sure it happened
    // so the next tests happen in the correct state.

    //Initialize state and get first 2 moves out of the way
    private GameState initializeState(int size) {
        GameState state = new GameState(true, size);
        PlaceTurn turn = new PlaceTurn(new BoardLocation(0,0), PieceType.STONE);
        Assert.assertTrue(state.executeTurn(turn));
        turn = new PlaceTurn(new BoardLocation(1,0), PieceType.STONE);
        Assert.assertTrue(state.executeTurn(turn));

        return state;
    }

    //Tests placing each type of piece
    @Test
    public void isLegalTurnPlaceNormal() {
        GameState state = initializeState(8);

        //Test stone placement for each color
        PlaceTurn turn = new PlaceTurn(1,1, PieceType.STONE);
        Assert.assertTrue(state.isLegalTurn(turn));
        Assert.assertTrue(state.executeTurn(turn));
        turn = new PlaceTurn(2,1, PieceType.STONE);
        Assert.assertTrue(state.isLegalTurn(turn));
        Assert.assertTrue(state.executeTurn(turn));

        //Test wall placement for each color
        turn = new PlaceTurn(2,2, PieceType.WALL);
        Assert.assertTrue(state.isLegalTurn(turn));
        Assert.assertTrue(state.executeTurn(turn));
        turn = new PlaceTurn(3,2, PieceType.WALL);
        Assert.assertTrue(state.isLegalTurn(turn));
        Assert.assertTrue(state.executeTurn(turn));

        //Test capstone placement for each color
        turn = new PlaceTurn(3,3, PieceType.CAPSTONE);
        Assert.assertTrue(state.isLegalTurn(turn));
        Assert.assertTrue(state.executeTurn(turn));
        turn = new PlaceTurn(4,3, PieceType.CAPSTONE);
        Assert.assertTrue(state.isLegalTurn(turn));
        Assert.assertTrue(state.executeTurn(turn));
    }

    //Test that when a type of piece is out it can't play
    @Test
    public void isLegalTurnPlaceOutOfPieces() {

        //Capstones
        GameState state = initializeState(8);
        //Place legal
        PlaceTurn turn = new PlaceTurn(2,0, PieceType.CAPSTONE);
        Assert.assertTrue(state.isLegalTurn(turn));
        Assert.assertTrue(state.executeTurn(turn));
        turn = new PlaceTurn(3,0, PieceType.CAPSTONE);
        Assert.assertTrue(state.isLegalTurn(turn));
        Assert.assertTrue(state.executeTurn(turn));
        turn = new PlaceTurn(4,0, PieceType.CAPSTONE);
        Assert.assertTrue(state.isLegalTurn(turn));
        Assert.assertTrue(state.executeTurn(turn));
        turn = new PlaceTurn(5,0, PieceType.CAPSTONE);
        Assert.assertTrue(state.isLegalTurn(turn));
        Assert.assertTrue(state.executeTurn(turn));
        //Place illegal capstone white
        turn = new PlaceTurn(6,0, PieceType.CAPSTONE);
        Assert.assertFalse(state.isLegalTurn(turn));
        //Place legal stone white
        turn = new PlaceTurn(6,0, PieceType.STONE);
        Assert.assertTrue(state.isLegalTurn(turn));
        Assert.assertTrue(state.executeTurn(turn));
        //Place illegal capstone black
        turn = new PlaceTurn(7,0, PieceType.CAPSTONE);
        Assert.assertFalse(state.isLegalTurn(turn));

        //Stones
        state = initializeState(8);
        //Fill up board to get to no stones
        for(int i = 0; i < 2; i++) {
            for (int y = 1; y < 7; y++) {
                turn = new PlaceTurn(0, y, PieceType.STONE);
                state.executeTurn(turn);
                for (int x = 1; x < 8 - i; x++) {
                    turn = new PlaceTurn(x, y, PieceType.STONE);
                    Assert.assertTrue(state.executeTurn(turn));
                    MoveTurn move = new MoveTurn(x - 1, y, x, Direction.EAST, new int[]{x});
                    Assert.assertTrue(state.executeTurn(move));
                }
            }
        }
        for(int i = 0; i < 4; i++) {
            turn = new PlaceTurn(i * 2,7, PieceType.STONE);
            Assert.assertTrue(state.executeTurn(turn));
            turn = new PlaceTurn(i * 2 + 1,7, PieceType.STONE);
            Assert.assertTrue(state.executeTurn(turn));
        }
        //Illegal white move, out of pieces
        turn = new PlaceTurn(0, 1, PieceType.STONE);
        Assert.assertFalse(state.isLegalTurn(turn));
        turn = new PlaceTurn(0, 1, PieceType.WALL);
        Assert.assertFalse(state.isLegalTurn(turn));
        //Legal capstone placement to make black turn
        turn = new PlaceTurn(0, 1, PieceType.CAPSTONE);
        Assert.assertTrue(state.executeTurn(turn));
        //Illegal black move
        turn = new PlaceTurn(0, 2, PieceType.STONE);
        Assert.assertFalse(state.isLegalTurn(turn));
        turn = new PlaceTurn(0, 2, PieceType.WALL);
        Assert.assertFalse(state.isLegalTurn(turn));
    }

    //Tests that you can't place a piece off the board
    @Test
    public void isLegalTurnPlaceOffBoard() {
        GameState state = new GameState(false, 3);
        //Black
        PlaceTurn turn = new PlaceTurn(new BoardLocation(-1,-1), PieceType.STONE);
        Assert.assertFalse(state.isLegalTurn(turn));
        turn = new PlaceTurn(1,3, PieceType.STONE);
        Assert.assertFalse(state.isLegalTurn(turn));
        turn = new PlaceTurn(3,1, PieceType.STONE);
        Assert.assertFalse(state.isLegalTurn(turn));
        turn = new PlaceTurn(3,3, PieceType.STONE);
        Assert.assertFalse(state.isLegalTurn(turn));
        turn = new PlaceTurn(1,1, PieceType.STONE);
        Assert.assertTrue(state.executeTurn(turn));
        //White
        turn = new PlaceTurn(-1,-1, PieceType.STONE);
        Assert.assertFalse(state.isLegalTurn(turn));
        turn = new PlaceTurn(1,3, PieceType.STONE);
        Assert.assertFalse(state.isLegalTurn(turn));
        turn = new PlaceTurn(3,1, PieceType.STONE);
        Assert.assertFalse(state.isLegalTurn(turn));
        turn = new PlaceTurn(3,3, PieceType.STONE);
        Assert.assertFalse(state.isLegalTurn(turn));
        turn = new PlaceTurn(1,2, PieceType.STONE);
        Assert.assertTrue(state.isLegalTurn(turn));
    }

    //Tests that you can't place pieces on other ones
    @Test
    public void isLegalTurnPlaceOnOtherPieces() {
        //Initalize with every type of piece
        GameState state = initializeState(8);
        PlaceTurn turn = new PlaceTurn(1,1,PieceType.STONE);
        Assert.assertTrue(state.executeTurn(turn));
        turn = new PlaceTurn(2,1,PieceType.STONE);
        Assert.assertTrue(state.executeTurn(turn));
        turn = new PlaceTurn(1,2,PieceType.WALL);
        Assert.assertTrue(state.executeTurn(turn));
        turn = new PlaceTurn(2,2,PieceType.WALL);
        Assert.assertTrue(state.executeTurn(turn));
        turn = new PlaceTurn(1,3,PieceType.CAPSTONE);
        Assert.assertTrue(state.executeTurn(turn));
        turn = new PlaceTurn(2,3,PieceType.CAPSTONE);
        Assert.assertTrue(state.executeTurn(turn));

        //Test white placing
        for(int x = 1; x < 3; x++) {
            for(int y = 1; y < 4; y++) {
                turn = new PlaceTurn(x,y, PieceType.STONE);
                Assert.assertFalse(state.isLegalTurn(turn));
                turn = new PlaceTurn(x,y, PieceType.WALL);
                Assert.assertFalse(state.isLegalTurn(turn));
                turn = new PlaceTurn(x,y, PieceType.CAPSTONE);
                Assert.assertFalse(state.isLegalTurn(turn));
            }
        }

        turn = new PlaceTurn(5,5, PieceType.STONE);
        Assert.assertTrue(state.executeTurn(turn));

        //Test black placing
        for(int x = 1; x < 3; x++) {
            for(int y = 1; y < 4; y++) {
                turn = new PlaceTurn(x,y, PieceType.STONE);
                Assert.assertFalse(state.isLegalTurn(turn));
                turn = new PlaceTurn(x,y, PieceType.WALL);
                Assert.assertFalse(state.isLegalTurn(turn));
                turn = new PlaceTurn(x,y, PieceType.CAPSTONE);
                Assert.assertFalse(state.isLegalTurn(turn));
            }
        }
    }

    //Makes sure you can't place anything besides stones for the first 2 turns
    @Test
    public void isLegalTurnPlaceBadFirstMoves() {
        GameState state = new GameState(true, 5);
        //white illegal turns
        PlaceTurn turn = new PlaceTurn(0,0,PieceType.CAPSTONE);
        Assert.assertFalse(state.isLegalTurn(turn));
        turn = new PlaceTurn(0,0,PieceType.WALL);
        Assert.assertFalse(state.isLegalTurn(turn));
        turn = new PlaceTurn(0,0,PieceType.STONE);
        Assert.assertTrue(state.executeTurn(turn));
        //black illegal turns
        turn = new PlaceTurn(1,1,PieceType.CAPSTONE);
        Assert.assertFalse(state.isLegalTurn(turn));
        turn = new PlaceTurn(1,1,PieceType.WALL);
        Assert.assertFalse(state.isLegalTurn(turn));
        turn = new PlaceTurn(1,1,PieceType.STONE);
        Assert.assertTrue(state.executeTurn(turn));
    }

    //Tests that some legal moves are legal
    @Test
    public void isLegalTurnMoveNormal() {
        GameState state = initializeState(5);

        MoveTurn move = new MoveTurn(1,0,1,Direction.WEST,new int[]{1});
        Assert.assertTrue(state.executeTurn(move));
        PlaceTurn place = new PlaceTurn(0,2,PieceType.WALL);
        Assert.assertTrue(state.executeTurn(place));
        place = new PlaceTurn(1,0,PieceType.CAPSTONE);
        Assert.assertTrue(state.executeTurn(place));
        place = new PlaceTurn(0,1,PieceType.STONE);
        Assert.assertTrue(state.executeTurn(place));
        move = new MoveTurn(1,0,1,Direction.WEST,new int[]{1});
        Assert.assertTrue(state.executeTurn(move));
        place = new PlaceTurn(4,4,PieceType.STONE);
        Assert.assertTrue(state.executeTurn(place));
        move = new MoveTurn(0,0,2,Direction.SOUTH,new int[]{1,1});
        Assert.assertTrue(state.isLegalTurn(move));
    }

    //Tests if you try to make a move off board
    @Test
    public void isLegalTurnMoveOffBoard() {
        GameState state = initializeState(5);

        PlaceTurn place = new PlaceTurn(1,1,PieceType.STONE);
        Assert.assertTrue(state.executeTurn(place));
        MoveTurn move = new MoveTurn(0,0,1,Direction.EAST,new int[]{1});
        Assert.assertTrue(state.executeTurn(move));
        place = new PlaceTurn(2,2,PieceType.STONE);
        Assert.assertTrue(state.executeTurn(place));
        move = new MoveTurn(1,0,2,Direction.NORTH,new int[]{2});
        Assert.assertFalse(state.isLegalTurn(move));
        move = new MoveTurn(1,0,2,Direction.WEST,new int[]{1,1});
        Assert.assertFalse(state.isLegalTurn(move));
    }

    //Tests if you try to grab a pile you don't own
    @Test
    public void isLegalTurnMoveIllegalPickup() {
        GameState state = initializeState(5);

        //Entire stack is owned by other player
        MoveTurn move = new MoveTurn(0,0,1,Direction.EAST,new int[]{1});
        Assert.assertFalse(state.isLegalTurn(move));
        move = new MoveTurn(1,0,1,Direction.WEST,new int[]{1});
        Assert.assertTrue(state.executeTurn(move));
        //Only top of stack is owned by other player
        move = new MoveTurn(0,0,2,Direction.EAST,new int[]{2});
        Assert.assertFalse(state.isLegalTurn(move));
    }

    //Tests if you try to cover a wall or capstone illegally
    @Test
    public void isLegalTurnMoveIllegalCover() {
    }

    //Tests if you try to pick up more than the max height
    @Test
    public void isLegalTurnMoveTooManyPickup() {
    }

    //Tests if you try to not leave at least 1 piece in each spot in the path
    @Test
    public void isLegalTurnMoveEmptySpots() {
    }

    //Tests if you try to do a move in the first 2 turns
    @Test
    public void isLegalTurnMoveBadFirstMoves() {
    }

    //Tests a straight horizontal win path
    @Test
    public void checkForWinnerStraightHorizontal() {
    }

    //Tests a straight vertical win path
    @Test
    public void checkForWinnerStraightVertical() {
    }

    //Tests a non-straight horizontal win path
    @Test
    public void checkForWinnerCurvyHorizontal() {
    }

    //Tests a non-straight vertical win path
    @Test
    public void checkForWinnerCurvyVertical() {
    }

    //Tests that walls can't be in win paths
    @Test
    public void checkForWinnerWallInPath() {
    }

    //Tests that capstones can be in win paths
    @Test
    public void checkForWinnerCapstoneInPath() {
    }

    //Tests that the top spot in a stack is the one counted
    @Test
    public void checkForWinnerStacks() {
    }

    //Tests the win condition that happens with a full board
    @Test
    public void checkForWinnerFullBoard() {
    }

    //Tests the win condition that happens when a player runs out of pieces
    @Test
    public void checkForWinnerOutOfPieces() {
    }

    //Makes sure the right player wins when both players get a road in the final move
    @Test
    public void checkForWinnerDoubleRoad() {
    }

    //Tests that places end up with the right state
    @Test
    public void executeTurnPlace() {
    }

    //Tests that moves end up with the right state
    @Test
    public void executeTurnMove() {
    }

    //Tests that place undos end up with the right state
    @Test
    public void undoTurnPlace() {
    }

    //Tests that move undos end up with the right state
    @Test
    public void undoTurnMove() {
    }

    //Makes sure the initial number of pieces are correct to the rules
    @Test
    public void initializeTest() {
        //3x3
        GameState state = new GameState(true, 3);
        Assert.assertEquals(10, state.getWhiteNormalPiecesLeft());
        Assert.assertEquals(0, state.getWhiteCapstonesLeft());
        Assert.assertEquals(10, state.getBlackNormalPiecesLeft());
        Assert.assertEquals(0, state.getBlackCapstonesLeft());
        Assert.assertEquals(3, state.getBoardSize());
        //4x4
        state = new GameState(true, 4);
        Assert.assertEquals(15, state.getWhiteNormalPiecesLeft());
        Assert.assertEquals(0, state.getWhiteCapstonesLeft());
        Assert.assertEquals(15, state.getBlackNormalPiecesLeft());
        Assert.assertEquals(0, state.getBlackCapstonesLeft());
        Assert.assertEquals(4, state.getBoardSize());
        //5x5
        state = new GameState(true, 5);
        Assert.assertEquals(21, state.getWhiteNormalPiecesLeft());
        Assert.assertEquals(1, state.getWhiteCapstonesLeft());
        Assert.assertEquals(21, state.getBlackNormalPiecesLeft());
        Assert.assertEquals(1, state.getBlackCapstonesLeft());
        Assert.assertEquals(5, state.getBoardSize());
        //6x6
        state = new GameState(true, 6);
        Assert.assertEquals(30, state.getWhiteNormalPiecesLeft());
        Assert.assertEquals(1, state.getWhiteCapstonesLeft());
        Assert.assertEquals(30, state.getBlackNormalPiecesLeft());
        Assert.assertEquals(1, state.getBlackCapstonesLeft());
        Assert.assertEquals(6, state.getBoardSize());
        //8x8
        state = new GameState(true, 8);
        Assert.assertEquals(50, state.getWhiteNormalPiecesLeft());
        Assert.assertEquals(2, state.getWhiteCapstonesLeft());
        Assert.assertEquals(50, state.getBlackNormalPiecesLeft());
        Assert.assertEquals(2, state.getBlackCapstonesLeft());
        Assert.assertEquals(8, state.getBoardSize());
    }
}