package io.joshatron.cli;

import io.joshatron.engine.*;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println("Initializing game...");
        GameState state = new GameState(true, 5);
        Scanner reader = new Scanner(System.in);


        while(state.checkForWinner() == 0) {
            System.out.println();
            state.printBoard();
            if(state.isWhiteTurn()) {
                System.out.print("White move: ");
            }
            else {
                System.out.print("Black move: ");
            }
            String input = reader.nextLine();

            if(input.equals("exit") || input.equals("quit")) {
                break;
            }
            else if(input.equals("help")) {
                System.out.println("Type exit to quit the game.");
            }
            else {
                Turn turn = turnFromString(input);
                if(turn != null && state.isLegalTurn(turn)) {
                    state.ExecuteTurn(turn);
                }
                else {
                    System.out.println("Invalid move. If you need help, type help. To quit, type exit.");
                }
            }
        }
    }

    private static Turn turnFromString(String str) {
        str = str.toLowerCase();
        if(str.charAt(0) == 'p') {
            PieceType type = null;
            switch(str.charAt(1)) {
                case 's':
                    type = PieceType.STONE;
                    break;
                case 'w':
                    type = PieceType.WALL;
                    break;
                case 'c':
                    type = PieceType.CAPSTONE;
                    break;
            }

            int x = xToNum(str.charAt(3));
            int y = charToNum(str.charAt(4)) - 1;

            if(type != null && x != -1 && y != -1) {
                PlaceTurn turn = new PlaceTurn(TurnType.PLACE, new BoardLocation(x, y), type);
                return turn;
            }
        }
        else if(str.charAt(0) == 'm') {
            Direction dir = null;
            switch(str.charAt(1)) {
                case 'n':
                    dir = Direction.NORTH;
                    break;
                case 's':
                    dir = Direction.SOUTH;
                    break;
                case 'e':
                    dir = Direction.EAST;
                    break;
                case 'w':
                    dir = Direction.WEST;
                    break;
            }

            int x = xToNum(str.charAt(3));
            int y = charToNum(str.charAt(4)) - 1;

            int pickUp = charToNum(str.charAt(6));

            int spots = (str.length() - 7) / 2;
            int[] drop = new int[spots];
            for(int i = 0; i < spots; i++) {
                drop[i] = charToNum(str.charAt(7 + (2 * i) + 1));
            }

            if(dir != null && x != -1 && y != -1 && pickUp != -1) {
                MoveTurn turn = new MoveTurn(TurnType.MOVE, new BoardLocation(x, y), pickUp, dir, drop);
                return turn;
            }
        }

        return null;
    }

    private static int xToNum(char c) {
        switch(c) {
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
        }

        return -1;
    }

    private static int charToNum(char c) {
        switch(c) {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
        }

        return -1;
    }
}
