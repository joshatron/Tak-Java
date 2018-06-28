package io.joshatron.engine;

import java.util.ArrayList;

public class GameState {

    private GameBoard board;

    private boolean whiteFirst;
    private boolean whiteTurn;

    private ArrayList<Turn> turns;

    private int whiteNormalPieces;
    private int whiteCapstones;
    private int blackNormalPieces;
    private int blackCapstones;

    public GameState(boolean whiteFirst, int boardSize) {
        this.whiteFirst = whiteFirst;
        this.turns = new ArrayList<>();

        if(whiteFirst) {
            this.whiteTurn = true;
        }
        else {
            this.whiteTurn = false;
        }

        switch(boardSize) {
            case 3:
                board = new GameBoard(boardSize);
                whiteNormalPieces = 10;
                whiteCapstones = 0;
                blackNormalPieces = 10;
                blackCapstones = 0;
                break;
            case 4:
                board = new GameBoard(boardSize);
                whiteNormalPieces = 15;
                whiteCapstones = 0;
                blackNormalPieces = 15;
                blackCapstones = 0;
                break;
            case 5:
                board = new GameBoard(boardSize);
                whiteNormalPieces = 21;
                whiteCapstones = 1;
                blackNormalPieces = 21;
                blackCapstones = 1;
                break;
            case 6:
                board = new GameBoard(boardSize);
                whiteNormalPieces = 30;
                whiteCapstones = 1;
                blackNormalPieces = 30;
                blackCapstones = 1;
                break;
            case 8:
                board = new GameBoard(boardSize);
                whiteNormalPieces = 50;
                whiteCapstones = 2;
                blackNormalPieces = 50;
                blackCapstones = 2;
                break;
        }
    }

    public boolean isLegalTurn(Turn turn) {
        // Make sure game isn't already over
        if(checkForWinner() != 0) {
            return false;
        }

        // Check place
        if(turn.getType() == TurnType.PLACE) {
            PlaceTurn place = (PlaceTurn)turn;
            // Check if enough pieces.
            // Ignore first 2 turns because placing other's pieces and impossible to be out
            if(turns.size() >= 2) {
                if(whiteTurn) {
                    if((place.getPieceType() == PieceType.STONE || place.getPieceType() == PieceType.WALL) &&
                       whiteNormalPieces == 0) {
                        return false;
                    }
                    if(place.getPieceType() == PieceType.CAPSTONE && whiteCapstones == 0) {
                        return false;
                    }
                }
                else {
                    if((place.getPieceType() == PieceType.STONE || place.getPieceType() == PieceType.WALL) &&
                            blackNormalPieces == 0) {
                        return false;
                    }
                    if(place.getPieceType() == PieceType.CAPSTONE && blackCapstones == 0) {
                        return false;
                    }
                }
            }

            // Check if it is the first couple turns that only stones are placed
            if(turns.size() < 2 && place.getPieceType() != PieceType.STONE) {
                return false;
            }

            // Check the location is valid
            if(place.getLocation().getX() < 0 || place.getLocation().getX() >= board.getBoardSize() ||
               place.getLocation().getY() < 0 || place.getLocation().getY() >= board.getBoardSize()) {
                return false;
            }

            // Check the location is empty
            if(board.getPosition(place.getLocation()).getPieces().size() == 0) {
                return true;
            }
            else {
                return false;
            }
        }
        // Check move
        else {
            MoveTurn move = (MoveTurn)turn;

            // No moves can be done in the first 2 turns
            if(turns.size() < 2) {
                return false;
            }

            // Check that the picked up pieces is legal
            if(move.getPickedUp() < 1 || move.getPickedUp() > board.getBoardSize()) {
                return false;
            }

            // Check that stack has enough pieces
            if(board.getPosition(move.getStartLocation()).getPieces().size() < move.getPickedUp()) {
                return false;
            }

            // Check that the player owns the stack
            if(whiteTurn) {
                if(board.getPosition(move.getStartLocation()).getTopPiece().isBlack()) {
                    return false;
                }
            }
            else {
                if(board.getPosition(move.getStartLocation()).getTopPiece().isWhite()) {
                    return false;
                }
            }

            // Check that each position of move is legal
            BoardLocation currentLocation = new BoardLocation(move.getStartLocation().getX(), move.getStartLocation().getY());
            int[] toPlace = move.getPlaced();
            ArrayList<Piece> pieces = board.getPosition(currentLocation).getTopPieces(move.getPickedUp());
            for(int i = 0; i < toPlace.length; i++) {
                // Check that at least one piece was placed
                if(toPlace[i] < 1) {
                    return false;
                }

                currentLocation.move(move.getDirection());

                //Check that location is legal
                if(currentLocation.getX() < 0 || currentLocation.getX() >= board.getBoardSize() ||
                   currentLocation.getY() < 0 || currentLocation.getY() >= board.getBoardSize()) {
                    return false;
                }

                //Check that it is okay to place there
                if(board.getPosition(currentLocation).getPieces().size() > 0) {
                    // If there is a capstone, fail
                    if(board.getPosition(currentLocation).getTopPiece().getType() == PieceType.CAPSTONE) {
                        return false;
                    }
                    // If there is a wall and you don't have only a capstone, fail
                    if(board.getPosition(currentLocation).getTopPiece().getType() == PieceType.WALL) {
                        if(pieces.size() == 1 && pieces.get(0).getType() != PieceType.CAPSTONE) {
                            return false;
                        }
                    }
                }

                for(int j = 0; j < toPlace[i]; j++) {
                    pieces.remove(0);
                }
            }

            // Nothing found wrong with the move
            return true;
        }
    }

    /*
    0- no one
    1- white
    2- black
    3- tie
     */
    public int checkForWinner() {
        // Check if someone is out of pieces
        if((whiteNormalPieces == 0 && whiteCapstones == 0) ||
           (blackNormalPieces == 0 && blackCapstones == 0)) {
            int white = 0;
            int black = 0;
            for(int x = 0; x < board.getBoardSize(); x++) {
                for(int y = 0; y < board.getBoardSize(); y++) {
                    if(board.getPosition(x, y).getTopPiece().getType() == PieceType.STONE) {
                        if(board.getPosition(x, y).getTopPiece().isWhite()) {
                            white++;
                        }
                        else {
                            black++;
                        }
                    }
                }
            }

            if(white > black) {
                return 1;
            }
            else if(black > white) {
                return 2;
            }
            else if(whiteCapstones > blackCapstones) {
                return 1;
            }
            else if(blackCapstones > whiteCapstones) {
                return 2;
            }
            else {
                return 3;
            }
        }

        //Check for each possible path
        boolean whitePath = false;
        boolean blackPath = false;

        for(int i = 0; i < board.getBoardSize(); i++) {
            if(board.getPosition(0, i).getTopPiece() != null) {
                if (isWinPath(new BoardLocation(0, i), new boolean[board.getBoardSize()][board.getBoardSize()], true, board.getPosition(0, i).getTopPiece().isWhite())) {
                    if (board.getPosition(0, i).getTopPiece().isWhite()) {
                        whitePath = true;
                    } else {
                        blackPath = true;
                    }
                }
            }
            if(board.getPosition(i, 0).getTopPiece() != null) {
                if (isWinPath(new BoardLocation(i, 0), new boolean[board.getBoardSize()][board.getBoardSize()], false, board.getPosition(i, 0).getTopPiece().isWhite())) {
                    if (board.getPosition(i, 0).getTopPiece().isWhite()) {
                        whitePath = true;
                    } else {
                        blackPath = true;
                    }
                }
            }
        }

        if(whitePath && !blackPath) {
            return 1;
        }
        else if(!whitePath && blackPath) {
            return 2;
        }
        else if(whitePath && blackPath && whiteTurn) {
            return 1;
        }
        else if(whitePath && blackPath && !whiteTurn) {
            return 2;
        }

        return 0;
    }

    private boolean isWinPath(BoardLocation current, boolean[][] checked, boolean horizontal, boolean white) {
        if((horizontal && current.getX() == board.getBoardSize() - 1) ||
           (!horizontal && current.getY() == board.getBoardSize() - 1)) {
            return true;
        }

        Direction[] dirs = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

        for(int i = 0; i < dirs.length; i++) {
            current.move(dirs[i]);
            if (current.getY() >= 0 && current.getY() < board.getBoardSize() &&
                current.getX() >= 0 && current.getX() < board.getBoardSize()) {
                Piece topPiece = board.getPosition(current).getTopPiece();
                if(topPiece != null) {
                    if (!checked[current.getX()][current.getY()] && topPiece.isWhite() == white &&
                            (topPiece.getType() == PieceType.CAPSTONE || topPiece.getType() == PieceType.STONE)) {
                        checked[current.getX()][current.getY()] = true;
                        if (isWinPath(current, checked, horizontal, white)) {
                            return true;
                        }
                    }
                }
            }
            current.moveOpposite(dirs[i]);
        }

        return false;
    }

    public boolean ExecuteTurn(Turn turn) {
        if(isLegalTurn(turn)) {
            if(turn.getType() == TurnType.PLACE) {
                PlaceTurn place = (PlaceTurn)turn;
                boolean white = whiteTurn;
                if(turns.size() < 2) {
                    white = !white;
                }
                board.getPosition(place.getLocation()).addPiece(new Piece(white, place.getPieceType()));

                if(whiteTurn) {
                    if(place.getPieceType() == PieceType.CAPSTONE) {
                        whiteCapstones--;
                    }
                    else {
                        whiteNormalPieces--;
                    }
                }
                else {
                    if(place.getPieceType() == PieceType.CAPSTONE) {
                        blackCapstones--;
                    }
                    else {
                        blackNormalPieces--;
                    }
                }

            }
            else {
                MoveTurn move = (MoveTurn)turn;
                ArrayList<Piece> pieces = board.getPosition(move.getStartLocation()).removePieces(move.getPickedUp());
                BoardLocation current = move.getStartLocation();
                for(int i = 0; i < move.getPlaced().length; i++) {
                    current.move(move.getDirection());
                    // If there is a wall, collapse it
                    if(board.getPosition(current).getPieces().size() > 0 && board.getPosition(current).getTopPiece().getType() == PieceType.WALL) {
                        board.getPosition(current).collapseTopPiece();
                        move.flatten();
                    }
                    // Place the right number of pieces in
                    for(int j = 0; j < move.getPlaced()[i]; j++) {
                        board.getPosition(current).addPiece(pieces.remove(0));
                    }
                }
            }

            turns.add(turn);
            whiteTurn = !whiteTurn;
            return true;
        }
        else {
            return false;
        }
    }

    public void undoTurn() {
        Turn turn = turns.remove(turns.size() - 1);

        //Undo a place move
        if(turn.getType() == TurnType.PLACE) {
            PlaceTurn place = (PlaceTurn)turn;

            board.getPosition(place.getLocation()).removePieces(1);
        }
        //Undo a move turn
        else {
            MoveTurn move = (MoveTurn)turn;

            BoardLocation current = new BoardLocation(move.getStartLocation().getX(), move.getStartLocation().getY());
            for(int i = 0; i < move.getPlaced().length; i++) {
                current.move(move.getDirection());
            }
            ArrayList<Piece> pickedUp = new ArrayList<>();
            for(int i = move.getPlaced().length - 1; i >= 0; i--) {
                pickedUp.addAll(0, board.getPosition(current).removePieces(move.getPlaced()[i]));
                if(i == move.getPlaced().length - 1 && move.didFlatten()) {
                    board.getPosition(current).uncollapseTopPiece();
                }

                current.moveOpposite(move.getDirection());
            }

            board.getPosition(current).addPieces(pickedUp);
        }
    }

    //TODO: implement possible turns
    public ArrayList<Turn> getPossibleTurns() {
        ArrayList<Turn> turns = new ArrayList<>();

        return turns;
    }

    public GameBoard getBoard() {
        return board;
    }

    public void printBoard() {
        board.printBoard();
    }

    public int getBoardSize() {
        return board.getBoardSize();
    }

    public boolean isWhiteFirst() {
        return whiteFirst;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public ArrayList<Turn> getTurns() {
        return turns;
    }

    public int getWhiteNormalPiecesLeft() {
        return whiteNormalPieces;
    }

    public int getWhiteCapstonesLeft() {
        return whiteCapstones;
    }

    public int getBlackNormalPiecesLeft() {
        return blackNormalPieces;
    }

    public int getBlackCapstonesLeft() {
        return blackCapstones;
    }
}
