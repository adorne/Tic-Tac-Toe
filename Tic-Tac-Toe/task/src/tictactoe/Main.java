package tictactoe;

import java.util.Scanner;

public class Main {

    private static final char playerX = 'X';

    private static final char playerO = 'O';

    private static final char emptyCell = ' ';

    private static final int lineSize = 3;

    public static void main(String[] args) {
        // create game board
        char[][] board = initBoard(lineSize);

        game(board);
    }

    private static void game(char[][] board) {
        printBoard(board);
        char currentPlayer = playerX;
        Scanner scanner = new Scanner(System.in);
        String gameState = gameState(board);

        while (gameState.equals("Game not finished")) {
            // get coordinates of next move
            System.out.print("Enter the coordinates (" + currentPlayer + " goes): ");

            String[] coordinates = scanner.nextLine().trim().split(" ");

            if (validCoordinates(board, coordinates)) {
                setFigure(board, currentPlayer, coordinates);

                switch (currentPlayer) {
                    case playerO:
                        currentPlayer = playerX;
                        break;

                    case playerX:
                        currentPlayer = playerO;
                        break;
                }

                printBoard(board);

                gameState = gameState(board);

                switch (gameState) {
                    case "X wins":

                    case "Draw":

                    case "O wins":
                        System.out.println(gameState);
                        scanner.close();
                        break;

                    case "Impossible":
                        System.out.println(gameState);
                        initBoard(lineSize);
                        break;
                }
            }
        }
    }

//
//    START BOARD INTERACTION
//
    // accepts line of symbols and makes a game board from them
    private static char[][] initBoard(int lineSize) {
        char[][] board = new char[lineSize][lineSize];

        for (int i = 0; i < lineSize; i++) {
            for (int j = 0; j < lineSize; j++) {
                board[i][j] = emptyCell;
            }
        }

        return board;
    }

    // prints given game board
    private static void printBoard(char[][] board) {
        int lineSize = board.length;
        System.out.print("---------\n| ");

        for (int i = 0; i < lineSize; i++) {
            for (int j = 0; j < lineSize; j++) {
                System.out.print(board[i][j] + " ");
            }

            if (i != lineSize - 1) {
                System.out.print("|\n| ");
            }
        }

        System.out.print("|\n---------\n");
    }

    // validates given coordinates
    private static boolean validCoordinates(char[][] board, String[] coordinates) {
        if (coordinates.length == 0) {
            System.out.println("Enter something!");
        } else if (coordinates.length < 2) {
            System.out.println("Must be 2 coordinates!");
            return false;
        } else {
            for (String coordinate : coordinates) {
                try {
                    int coordinateInt = Integer.parseInt(coordinate);

                    if ((coordinateInt < 1 || coordinateInt > 3)) {
                        System.out.println("Coordinates should be from 1 to 3!");
                        return false;
                    }
                } catch (Exception e) {
                    System.out.println("You should enter numbers!");
                    return false;
                }
            }
        }

        char figureToCheck = getFigure(board, coordinates);

        if (figureToCheck == playerO || figureToCheck == playerX) {
            System.out.println("This cell is occupied! Choose another one!");
            return false;
        }

        return true;
    }

    // sets given figure on given coordinates
    private static void setFigure(char[][] board, char figure, String[] coordinates) {
        // invert coordinates through Math.abs() method: Math.abs(board.length - y)
        // 1 1 => 2 0
        // 1 3 => 0 0
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);

        int boardX = Math.abs(board.length - y);
        int boardY = x - 1;

        board[boardX][boardY] = figure;
    }

    // gets given figure from given coordinates
    private static char getFigure(char[][] board, String[] coordinates) {
        // invert coordinates through Math.abs() method: Math.abs(board.length - y)
        // 1 1 => 2 0
        // 1 3 => 0 0
        int x = Integer.parseInt(coordinates[0]);
        int y = Integer.parseInt(coordinates[1]);
        int boardX = Math.abs(board.length - y);
        int boardY = x - 1;

        return board[boardX][boardY];
    }

//
//    END BOARD INTERACTION
//

//
//    START GAME STATE CHECK
//

    // prints out current game state
    private static String gameState(char[][] board) {
        /*
         * check filled lines for both players
         * check empty cells
         * if both X and O fill lines or | X - O | > 1 -> Impossible
         * if X/O fill line -> X/O wins
         * if board has no filled lines but has empty cells -> Game not finished
         * if lines isn't filled and no empty cells -> Draw
         */

        String gameState;

        if (isImpossible(board)) {
            gameState = "Impossible";
        } else if (checkLine(board, playerX)) {
            gameState = "X wins";
        } else if (checkLine(board, playerO)) {
            gameState = "O wins";
        } else if (hasEmptyCells(board)) {
            gameState = "Game not finished";
        } else {
            gameState = "Draw";
        }

        return gameState;
    }

    /*
     checks conditions of impossible board:
        when the field has three X in a row as well as three O in a row
        or
        when the field has a lot more X's than O's or vice versa
    */
    private static boolean isImpossible(char[][] board) {
        boolean bothPlayersFilledLines = checkLine(board, Main.playerX) && checkLine(board, Main.playerO);
        boolean incorrectDiff = Math.abs(countSymbols(board, Main.playerX) - countSymbols(board, Main.playerO)) > 1;

        return bothPlayersFilledLines || incorrectDiff;
    }

    // counts sum of player's symbols on given board
    private static int countSymbols(char[][] board, char player) {
        int counter = 0;

        for (char[] line : board) {
            for (char element : line) {
                if (element == player) {
                    counter++;
                }
            }
        }

        return counter;
    }

    // checks if one of lines is filled with player's symbol
    private static boolean checkLine(char[][] board, char player) {
        return checkH(board, player) || checkV(board, player) || checkD(board, player);
    }

    // checks horizontal lines
    private static boolean checkH(char[][] board, char player) {

        for (char[] hLine : board) {
            int counter = 0;

            for (char element : hLine) {
                if (element == player) {
                    counter++;

                    if (counter == 3) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // checks vertical lines
    private static boolean checkV(char[][] board, char player) {
        int lineSize = board.length;

        for (int i = 0; i < lineSize; i++) {
            int counter = 0;

            for (char[] vLine : board) {
                if (vLine[i] == player) {
                    counter++;
                }
            }

            if (counter == lineSize) {
                return true;
            }
        }

        return false;
    }

    // checks diagonal lines
    private static boolean checkD(char[][] board, char player) {
        int lineSize = board.length;
        int counter1 = 0, counter2 = 0;

        for (int i = 0; i < lineSize; i++) {
            if (board[i][i] == player) {
                counter1++;
            }

            if (board[i][lineSize - 1 - i] == player) {
                counter2++;
            }
        }

        return counter1 == lineSize || counter2 == lineSize;
    }

    // checks if board has empty cells
    private static boolean hasEmptyCells(char[][] board) {
        for (char[] line : board) {
            for (char element : line) {
                if (element == emptyCell) {
                    return true;
                }
            }
        }

        return false;
    }

//
//    END GAME STATE CHECK
//

}