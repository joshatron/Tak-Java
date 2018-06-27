package io.joshatron.engine;

import java.util.ArrayList;

public class GameState {

    //[x][y]
    private PieceStack[][] board;
    private int boardSize;

    private boolean whiteFirst;
    private boolean whiteTurn;

    private ArrayList<Turn> turns;

    private int whiteNormalPieces;
    private int whiteCapstones;
    private int blackNormalPieces;
    private int blackCapstones;

    public GameState(boolean whiteFirst, int boardSize) {
        this.whiteFirst = whiteFirst;
        this.boardSize = boardSize;
        this.turns = new ArrayList<>();

        if(whiteFirst) {
            this.whiteTurn = true;
        }
        else {
            this.whiteTurn = false;
        }

        switch(boardSize) {
            case 3:
                board = new PieceStack[3][3];
                whiteNormalPieces = 10;
                whiteCapstones = 0;
                blackNormalPieces = 10;
                blackCapstones = 0;
                break;
            case 4:
                board = new PieceStack[4][4];
                whiteNormalPieces = 15;
                whiteCapstones = 0;
                blackNormalPieces = 15;
                blackCapstones = 0;
                break;
            case 5:
                board = new PieceStack[5][5];
                whiteNormalPieces = 21;
                whiteCapstones = 1;
                blackNormalPieces = 21;
                blackCapstones = 1;
                break;
            case 6:
                board = new PieceStack[6][6];
                whiteNormalPieces = 30;
                whiteCapstones = 1;
                blackNormalPieces = 30;
                blackCapstones = 1;
                break;
            case 8:
                board = new PieceStack[8][8];
                whiteNormalPieces = 50;
                whiteCapstones = 2;
                blackNormalPieces = 50;
                blackCapstones = 2;
                break;
        }

        for(int x = 0; x < boardSize; x++) {
            for(int y = 0; y < boardSize; y++) {
                board[x][y] = new PieceStack();
            }
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
            if(place.getLocation().getX() < 0 || place.getLocation().getX() >= boardSize ||
               place.getLocation().getY() < 0 || place.getLocation().getY() >= boardSize) {
                return false;
            }

            // Check the location is empty
            if(board[place.getLocation().getX()][place.getLocation().getY()].getPieces().size() == 0) {
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
            if(move.getPickedUp() < 1 || move.getPickedUp() > boardSize) {
                return false;
            }

            // Check that stack has enough pieces
            if(board[move.getStartLocation().getX()][move.getStartLocation().getY()].getPieces().size() < move.getPickedUp()) {
                return false;
            }

            // Check that the player owns the stack
            if(whiteTurn) {
                if(board[move.getStartLocation().getX()][move.getStartLocation().getY()].getTopPiece().isBlack()) {
                    return false;
                }
            }
            else {
                if(board[move.getStartLocation().getX()][move.getStartLocation().getY()].getTopPiece().isWhite()) {
                    return false;
                }
            }

            // Check that each position of move is legal
            BoardLocation currentLocation = new BoardLocation(move.getStartLocation().getX(), move.getStartLocation().getY());
            int[] toPlace = move.getPlaced();
            ArrayList<Piece> pieces = board[currentLocation.getX()][currentLocation.getY()].getTopPieces(move.getPickedUp());
            for(int i = 0; i < toPlace.length; i++) {
                // Check that at least one piece was placed
                if(toPlace[i] < 1) {
                    return false;
                }

                currentLocation.move(move.getDirection());

                //Check that location is legal
                if(currentLocation.getX() < 0 || currentLocation.getX() >= boardSize ||
                   currentLocation.getY() < 0 || currentLocation.getY() >= boardSize) {
                    return false;
                }

                //Check that it is okay to place there
                if(board[currentLocation.getX()][currentLocation.getY()].getPieces().size() > 0) {
                    // If there is a capstone, fail
                    if(board[currentLocation.getX()][currentLocation.getY()].getTopPiece().getType() == PieceType.CAPSTONE) {
                        return false;
                    }
                    // If there is a wall and you don't have only a capstone, fail
                    if(board[currentLocation.getX()][currentLocation.getY()].getTopPiece().getType() == PieceType.WALL) {
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

    public boolean ExecuteTurn(Turn turn) {
        if(isLegalTurn(turn)) {
            if(turn.getType() == TurnType.PLACE) {
                PlaceTurn place = (PlaceTurn)turn;
                boolean white = whiteTurn;
                if(turns.size() < 2) {
                    white = !white;
                }
                board[place.getLocation().getX()][place.getLocation().getY()].addPiece(new Piece(white, place.getPieceType()));

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
                ArrayList<Piece> pieces = board[move.getStartLocation().getX()][move.getStartLocation().getY()].removePieces(move.getPickedUp());
                BoardLocation current = move.getStartLocation();
                for(int i = 0; i < move.getPlaced().length; i++) {
                    current.move(move.getDirection());
                    // If there is a wall, collapse it
                    if(board[current.getX()][current.getY()].getTopPiece().getType() == PieceType.WALL) {
                        board[current.getX()][current.getY()].collapseTopPiece();
                    }
                    // Place the right number of pieces in
                    for(int j = 0; j < move.getPlaced()[i]; j++) {
                        board[current.getX()][current.getY()].addPiece(pieces.remove(0));
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

    //TODO: implement undo feature
    public void undoTurn() {

    }

    //TODO: implement possible turns
    public ArrayList<Turn> getPossibleTurns() {
        ArrayList<Turn> turns = new ArrayList<>();

        return turns;
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
            for(int x = 0; x < boardSize; x++) {
                for(int y = 0; y < boardSize; y++) {
                    if(board[x][y].getTopPiece().getType() == PieceType.STONE) {
                        if(board[x][y].getTopPiece().isWhite()) {
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

        for(int i = 0; i < boardSize; i++) {
            if(board[0][i].getTopPiece() != null) {
                if (isWinPath(new BoardLocation(0, i), new boolean[boardSize][boardSize], true, board[0][i].getTopPiece().isWhite())) {
                    if (board[0][i].getTopPiece().isWhite()) {
                        whitePath = true;
                    } else {
                        blackPath = true;
                    }
                }
            }
            if(board[i][0].getTopPiece() != null) {
                if (isWinPath(new BoardLocation(i, 0), new boolean[boardSize][boardSize], false, board[i][0].getTopPiece().isWhite())) {
                    if (board[i][0].getTopPiece().isWhite()) {
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
        if((horizontal && current.getX() == boardSize - 1) ||
           (!horizontal && current.getY() == boardSize - 1)) {
            return true;
        }

        Direction[] dirs = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        Direction[] backDirs = {Direction.SOUTH, Direction.NORTH, Direction.WEST, Direction.EAST};

        for(int i = 0; i < dirs.length; i++) {
            current.move(dirs[i]);
            if (current.getY() >= 0 && current.getY() < boardSize &&
                current.getX() >= 0 && current.getX() < boardSize) {
                Piece topPiece = board[current.getX()][current.getY()].getTopPiece();
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
            current.move(backDirs[i]);
        }

        return false;
    }

    public void printBoard() {
        int maxSize = 0;
        for(int x = 0; x < boardSize; x++) {
            for(int y = 0; y < boardSize; y++) {
                if(board[x][y].getString().length() > maxSize) {
                    maxSize = board[x][y].getString().length();
                }
            }
        }

        System.out.print("    ");
        char[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        for(int i = 0; i < boardSize; i++) {
            System.out.print(chars[i] + "   ");
            for(int j = 0; j < maxSize - 1; j++) {
                System.out.print(" ");
            }
        }
        System.out.println();
        System.out.print("  ");
        for(int i = 0; i < ((maxSize + 3) * boardSize + 1); i++) {
            System.out.print("-");
        }
        System.out.println();
        for(int y = 0; y < boardSize; y++) {
            System.out.print((y + 1) + " ");
            for(int x = 0; x < boardSize; x++) {
                System.out.print("| ");
                System.out.print(board[x][y].getString() + " ");
                int len = board[x][y].getString().length();
                for(int i = 0; i < maxSize - len; i++) {
                    System.out.print(" ");
                }
            }
            System.out.println("|");
            System.out.print("  ");
            for(int i = 0; i < ((maxSize + 3) * boardSize + 1); i++) {
                System.out.print("-");
            }
            System.out.println();
        }
    }

    public PieceStack[][] getBoard() {
        return board;
    }

    public int getBoardSize() {
        return boardSize;
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
