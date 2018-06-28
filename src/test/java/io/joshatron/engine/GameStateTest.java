package io.joshatron.engine;

import org.junit.Assert;
import org.junit.Test;

public class GameStateTest {

    //Initialize state and get first 2 moves out of the way
    private GameState initializeState(int size) {
        GameState state = new GameState(true, size);
        PlaceTurn turn = new PlaceTurn(new BoardLocation(0,0), PieceType.STONE);
        state.executeTurn(turn);
        turn = new PlaceTurn(new BoardLocation(1,0), PieceType.STONE);
        state.executeTurn(turn);

        return state;
    }

    @Test
    public void isLegalTurnPlaceNormal() {
        GameState state = initializeState(8);

        //Test stone placement for each color
        PlaceTurn turn = new PlaceTurn(new BoardLocation(1,1), PieceType.STONE);
        Assert.assertTrue(state.isLegalTurn(turn));
        state.executeTurn(turn);
        turn = new PlaceTurn(new BoardLocation(2,1), PieceType.STONE);
        Assert.assertTrue(state.isLegalTurn(turn));
        state.executeTurn(turn);

        //Test wall placement for each color
        turn = new PlaceTurn(new BoardLocation(2,2), PieceType.WALL);
        Assert.assertTrue(state.isLegalTurn(turn));
        state.executeTurn(turn);
        turn = new PlaceTurn(new BoardLocation(3,2), PieceType.WALL);
        Assert.assertTrue(state.isLegalTurn(turn));
        state.executeTurn(turn);

        //Test capstone placement for each color
        turn = new PlaceTurn(new BoardLocation(3,3), PieceType.CAPSTONE);
        Assert.assertTrue(state.isLegalTurn(turn));
        state.executeTurn(turn);
        turn = new PlaceTurn(new BoardLocation(4,3), PieceType.CAPSTONE);
        Assert.assertTrue(state.isLegalTurn(turn));
        state.executeTurn(turn);
    }

    @Test
    public void isLegalTurnPlaceOutOfPieces() {

        //Capstones
        GameState state = initializeState(8);
        //Place legal
        PlaceTurn turn = new PlaceTurn(new BoardLocation(2,0), PieceType.CAPSTONE);
        Assert.assertTrue(state.isLegalTurn(turn));
        state.executeTurn(turn);
        turn = new PlaceTurn(new BoardLocation(3,0), PieceType.CAPSTONE);
        Assert.assertTrue(state.isLegalTurn(turn));
        state.executeTurn(turn);
        turn = new PlaceTurn(new BoardLocation(4,0), PieceType.CAPSTONE);
        Assert.assertTrue(state.isLegalTurn(turn));
        state.executeTurn(turn);
        turn = new PlaceTurn(new BoardLocation(5,0), PieceType.CAPSTONE);
        Assert.assertTrue(state.isLegalTurn(turn));
        state.executeTurn(turn);
        //Place illegal capstone white
        turn = new PlaceTurn(new BoardLocation(6,0), PieceType.CAPSTONE);
        Assert.assertFalse(state.isLegalTurn(turn));
        //Place legal stone white
        turn = new PlaceTurn(new BoardLocation(6,0), PieceType.STONE);
        Assert.assertTrue(state.isLegalTurn(turn));
        state.executeTurn(turn);
        //Place illegal capstone black
        turn = new PlaceTurn(new BoardLocation(7,0), PieceType.CAPSTONE);
        Assert.assertFalse(state.isLegalTurn(turn));

        //Stones
        state = initializeState(8);
        //Fill up board to get to no stones
        for(int i = 0; i < 2; i++) {
            for (int y = 1; y < 7; y++) {
                turn = new PlaceTurn(new BoardLocation(0, y), PieceType.STONE);
                state.executeTurn(turn);
                for (int x = 1; x < 8 - i; x++) {
                    turn = new PlaceTurn(new BoardLocation(x, y), PieceType.STONE);
                    state.executeTurn(turn);
                    MoveTurn move = new MoveTurn(new BoardLocation(x - 1, y), x, Direction.EAST, new int[]{x});
                    state.executeTurn(move);
                }
            }
        }
        for(int i = 0; i < 4; i++) {
            turn = new PlaceTurn(new BoardLocation(i * 2,7), PieceType.STONE);
            state.executeTurn(turn);
            turn = new PlaceTurn(new BoardLocation(i * 2 + 1,7), PieceType.STONE);
            state.executeTurn(turn);
        }
        //Illegal white move, out of pieces
        turn = new PlaceTurn(new BoardLocation(0, 1), PieceType.STONE);
        Assert.assertFalse(state.isLegalTurn(turn));
        turn = new PlaceTurn(new BoardLocation(0, 1), PieceType.WALL);
        Assert.assertFalse(state.isLegalTurn(turn));
        //Legal capstone placement to make black turn
        turn = new PlaceTurn(new BoardLocation(0, 1), PieceType.CAPSTONE);
        state.executeTurn(turn);
        //Illegal black move
        turn = new PlaceTurn(new BoardLocation(0, 2), PieceType.STONE);
        Assert.assertFalse(state.isLegalTurn(turn));
        turn = new PlaceTurn(new BoardLocation(0, 2), PieceType.WALL);
        Assert.assertFalse(state.isLegalTurn(turn));
    }

    @Test
    public void isLegalTurnPlaceOffBoard() {
    }

    @Test
    public void isLegalTurnPlaceOnOtherPieces() {
    }

    @Test
    public void isLegalTurnPlaceBadFirstMoves() {
    }

    @Test
    public void isLegalTurnMoveNormal() {
    }

    @Test
    public void isLegalTurnMoveOffBoard() {
    }

    @Test
    public void isLegalTurnMoveIllegalPickup() {
    }

    @Test
    public void isLegalTurnMoveIllegalCover() {
    }

    @Test
    public void isLegalTurnMoveTooManyPickup() {
    }

    @Test
    public void isLegalTurnMoveEmptySpots() {
    }

    @Test
    public void isLegalTurnMoveBadFirstMoves() {
    }

    @Test
    public void checkForWinnerStraightHorizontal() {
    }

    @Test
    public void checkForWinnerStraightVertical() {
    }

    @Test
    public void checkForWinnerCurvyHorizontal() {
    }

    @Test
    public void checkForWinnerCurvyVertical() {
    }

    @Test
    public void checkForWinnerWallInPath() {
    }

    @Test
    public void checkForWinnerCapstoneInPath() {
    }

    @Test
    public void checkForWinnerStacks() {
    }

    @Test
    public void checkForWinnerFullBoard() {
    }

    @Test
    public void checkForWinnerOutOfPieces() {
    }

    @Test
    public void checkForWinnerDoubleRoad() {
    }

    @Test
    public void executeTurnPlace() {
    }

    @Test
    public void executeTurnMove() {
    }

    @Test
    public void undoTurnPlace() {
    }

    @Test
    public void undoTurnMove() {
    }
}