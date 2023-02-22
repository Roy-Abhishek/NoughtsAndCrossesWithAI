package com.codewithabhishek;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class TicTacToeAI {
    public static final Scanner scanner = new Scanner(System.in);

    public static final String[][] board = new String[3][3];

    static {
        initiateBoard();
    }

    private static void initiateBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = "";
    }

    private static void waitForHalfASecond() {
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        mainPlayGame();
    }

    public static void mainPlayGame() {
        System.out.println("For the normal school assignment, type \"multiplayer\".");
        System.out.println("For a single player game where the computer chooses randomly, type \"random\".");
        System.out.println("To test out my AI, type \"AI\".");
        System.out.print("How do you want to play? ");
        String response = scanner.next();
        while (true) {
            if (response.equalsIgnoreCase("multiplayer")) {
                playGame(); break;
            } else if (response.equalsIgnoreCase("random")) {
                playGameWithRandomComputer(); break;
            } else if (response.equalsIgnoreCase("AI")) {
                playGameWithAI(); break;
            } else {
                System.out.println("Try again.");
                System.out.print("How do you want to play? ");
                response = scanner.next();
            }
        }
    }

    public static void playGameWithRandomComputer() {
        printIntroWithBoard();

        System.out.print("What do you want to be (X or O)? ");
        String player = scanner.next();
        boolean XisAI = !player.equalsIgnoreCase("X");
        System.out.print("Who goes first (X or O)? ");
        String player1 = scanner.next();
        boolean playerXTurn = !player1.equalsIgnoreCase("O");
        System.out.println("Let's play!");

        while (!checkForWins("O") && !checkForWins("X") && !checkForFullBoard()) {
            printBoard();
            if (playerXTurn) {
                if (!XisAI) {
                    System.out.print("Player X -- Row: ");
                    int row = scanner.nextInt();
                    if (row > 3 || row < 1) {
                        System.out.println("Invalid input, choose a number between 1 and 3 inclusive.");
                        continue;
                    }
                    System.out.print("Player X -- Column: ");
                    int column = scanner.nextInt();
                    if (column > 3 || column < 1) {
                        System.out.println("Invalid input, choose a number between 1 and 3 inclusive.");
                        continue;
                    }

                    if (!changeBoard(board, "X", row, column)) {
                        System.out.println("Invalid input, placing in a taken spot. Try again.");
                        continue;
                    }
                } else {
                    int row = (int) (Math.random() * 3) + 1;
                    System.out.println("Player X -- Row: " + row);
                    waitForHalfASecond();
                    int column = (int) (Math.random() * 3) + 1;
                    System.out.println("Player X -- Column: " + column);
                    waitForHalfASecond();
                    if (!changeBoard(board, "X", row, column)) {
                        System.out.println("Invalid input, placing in a taken spot. Try again.");
                        continue;
                    }
                }
            } else {
                if (!XisAI) {
                    int row = (int) (Math.random() * 3) + 1;
                    System.out.println("Player O -- Row: " + row);
                    waitForHalfASecond();
                    int column = (int) (Math.random() * 3) + 1;
                    System.out.println("Player O -- Column: " + column);
                    waitForHalfASecond();

                    if (!changeBoard(board, "O", row, column)) {
                        System.out.println("Invalid input, placing in a taken spot. Try again.");
                        continue;
                    }
                } else {
                    System.out.print("Player O -- Row: ");
                    int row = scanner.nextInt();
                    if (row > 3 || row < 1) {
                        System.out.println("Invalid input, choose a number between 1 and 3 inclusive.");
                        continue;
                    }
                    System.out.print("Player O -- Column: ");
                    int column = scanner.nextInt();
                    if (column > 3 || column < 1) {
                        System.out.println("Invalid input, choose a number between 1 and 3 inclusive.");
                        continue;
                    }

                    if (!changeBoard(board, "O", row, column)) {
                        System.out.println("Invalid input, placing in a taken spot. Try again.");
                        continue;
                    }
                }
            }
            playerXTurn = !playerXTurn;
        }

        finalStatement();
        playAgainOrNot();
    }

    public static void playGameWithAI() {
        printIntroWithBoard();

        System.out.println("The following AI program is not random and follows a minimax algorithm, which technically qualifies it as AI.");
        System.out.println("The difficulty mode on this AI is low (mostly cuz it's my first time making a minimax AI (in other words, my version of the");
        System.out.println("AI is kinda trash)), so don't expect much from it. But, there are times it completely destroys you, so be careful.");
        System.out.print("What do you want to be (X or O)? ");
        String player = scanner.next();
        boolean XisAI = !player.equalsIgnoreCase("X");
        System.out.print("Who goes first (X or O)? ");
        String playerOrAI = scanner.next();
        boolean playerXTurn = !playerOrAI.equalsIgnoreCase("O");
        System.out.println("Let's play!");

        while (!checkForWins("O") && !checkForWins("X") && !checkForFullBoard()) {
            printBoard();
            if (playerXTurn) {
                if (XisAI) {
                    int[] nodeNumberList = playForMe(board, 0, true, true);
                    int index = randomNodeNumListIndex(nodeNumberList, true);
                    int[] moveToPlay = movesThatCanBePlayed(board)[index];

                    if (isNextMoveAWin(board, "O"))
                        moveToPlay = whichMoveIsAWin(board, "O");

                    System.out.println("Player X -- Row: " + moveToPlay[0]);
                    waitForHalfASecond();
                    System.out.println("Player X -- Column: " + moveToPlay[1]);
                    waitForHalfASecond();

                    if (!changeBoard(board,"X", moveToPlay[0], moveToPlay[1])) {
                        System.out.println("Invalid input, placing in a taken spot. Try again.");
                        continue;
                    }
                } else {
                    System.out.print("Player X -- Row: ");
                    int row = scanner.nextInt();
                    if (row > 3 || row < 1) {
                        System.out.println("Invalid input, choose a number between 1 and 3 inclusive.");
                        continue;
                    }
                    System.out.print("Player X -- Column: ");
                    int column = scanner.nextInt();
                    if (column > 3 || column < 1) {
                        System.out.println("Invalid input, choose a number between 1 and 3 inclusive.");
                        continue;
                    }

                    if (!changeBoard(board,"X", row, column)) {
                        System.out.println("Invalid input, placing in a taken spot. Try again.");
                        continue;
                    }
                }
            } else {
                if (XisAI) {
                    System.out.print("Player O -- Row: ");
                    int row = scanner.nextInt();
                    if (row > 3 || row < 1) {
                        System.out.println("Invalid input, choose a number between 1 and 3 inclusive.");
                        continue;
                    }
                    System.out.print("Player O -- Column: ");
                    int column = scanner.nextInt();
                    if (column > 3 || column < 1) {
                        System.out.println("Invalid input, choose a number between 1 and 3 inclusive.");
                        continue;
                    }

                    if (!changeBoard(board, "O", row, column)) {
                        System.out.println("Invalid input, placing in a taken spot. Try again.");
                        continue;
                    }
                } else {
                    int[] nodeNumberList = playForMe(board, 0, false, false);
                    int index = randomNodeNumListIndex(nodeNumberList, false);
                    int[] moveToPlay = movesThatCanBePlayed(board)[index];

                    if (isNextMoveAWin(board, "X"))
                        moveToPlay = whichMoveIsAWin(board, "X");

                    System.out.println("Player O -- Row: " + moveToPlay[0]);
                    waitForHalfASecond();
                    System.out.println("Player O -- Column: " + moveToPlay[1]);
                    waitForHalfASecond();

                    if (!changeBoard(board,"O", moveToPlay[0], moveToPlay[1])) {
                        System.out.println("Invalid input, placing in a taken spot. Try again.");
                        continue;
                    }
                }
            }
            playerXTurn = !playerXTurn;
        }

        finalStatement();
        playAgainOrNotForAI();
    }

    public static int indexOf(int[] array, int target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) return i;
        } return -1;
    }

    public static int[] indexesOf(int[] array, int target) {
        int length = 0;
        for (int i : array) if (i == target) length++;

        int[] arrayOfIndexes = new int[length];
        int arrayOfIndexesIndex = 0;
        for (int i = 0; i < array.length; i++)
            if (array[i] == target) arrayOfIndexes[arrayOfIndexesIndex++] = i;

        return arrayOfIndexes;
    }

    public static int randomNodeNumListIndex(int[] nodeNumList, boolean max) {
        if (max) {
            int maxNum = maxNumberInArray(nodeNumList);
            int[] arrayOfIndexes = indexesOf(nodeNumList, maxNum);
            int randomIndex = (int) (Math.random() * arrayOfIndexes.length);
            return arrayOfIndexes[randomIndex];
        } else {
            int minNum = minNumberInArray(nodeNumList);
            int[] arrayOfIndexes = indexesOf(nodeNumList, minNum);
            int randomIndex = (int) (Math.random() * arrayOfIndexes.length);
            return arrayOfIndexes[randomIndex];
        }
    }

    public static int[] playForMe(String[][] boardState, int depth, boolean xPlayerTurn, boolean originalPlayerIsX) {
        if (checkForWinsForAI(boardState, "X")) return new int[] {10 - depth};
        else if (checkForWinsForAI(boardState, "O")) return new int[] {-10 + depth};
        else if (checkForFullBoardForAi(boardState)) return new int[] {0};

        int[][] moves = movesThatCanBePlayed(boardState);
        String[][][] boardCopies = new String[moves.length][3][3];
        int boardCopiesIndex = 0;
        for (int i = 0; i < moves.length; i++)
            boardCopies[i] = clone(boardState);

        int[] nodeNumList = new int[moves.length];
        int nodeNumListIndex = 0;
        if (xPlayerTurn) { // X's turn
            for (int[] move : moves) {
                String[][] boardCopy = boardCopies[boardCopiesIndex];

                changeBoard(boardCopy, "X", move[0], move[1]);
                int nodeNumber = maxNumberInArray(playForMe(boardCopy, depth + 1, false, originalPlayerIsX));
                nodeNumList[nodeNumListIndex++] = nodeNumber;
                boardCopiesIndex++;
            }
            return nodeNumList;
        } else { // O's turn
            for (int[] move : moves) {
                String[][] boardCopy = boardCopies[boardCopiesIndex];

                changeBoard(boardCopy, "O", move[0], move[1]);
                int nodeNumber = minNumberInArray(playForMe(boardCopy, depth + 1, true, originalPlayerIsX));
                nodeNumList[nodeNumListIndex++] = nodeNumber;
                boardCopiesIndex++;
            }
            return nodeNumList;
        }
    }

    public static boolean isNextMoveAWin(String[][] board, String player) {
        int[][] moves = movesThatCanBePlayed(board);
        String[][][] boardCopies = new String[moves.length][3][3];
        int boardCopiesIndex = 0;
        for (int i = 0; i < moves.length; i++)
            boardCopies[i] = clone(board);

        if (player.equalsIgnoreCase("X")) {
            for (int[] move: moves) {
                String[][] boardCopy = boardCopies[boardCopiesIndex];
                changeBoard(boardCopy, "X", move[0], move[1]);
                if (checkForWinsForAI(boardCopy, "X")) return true;
                boardCopiesIndex++;
            } return false;
        } else {
            for (int[] move: moves) {
                String[][] boardCopy = boardCopies[boardCopiesIndex];
                changeBoard(boardCopy, "O", move[0], move[1]);
                if (checkForWinsForAI(boardCopy, "O")) return true;
                boardCopiesIndex++;
            } return false;
        }
    }

    public static int[] whichMoveIsAWin(String[][] board, String player) {
        int[][] moves = movesThatCanBePlayed(board);
        String[][][] boardCopies = new String[moves.length][3][3];
        int boardCopiesIndex = 0;
        for (int i = 0; i < moves.length; i++)
            boardCopies[i] = clone(board);

        if (player.equalsIgnoreCase("X")) {
            for (int[] move: moves) {
                String[][] boardCopy = boardCopies[boardCopiesIndex];
                changeBoard(boardCopy, "X", move[0], move[1]);
                if (checkForWinsForAI(boardCopy, "X")) return move;
                boardCopiesIndex++;
            } return new int[0];
        } else {
            for (int[] move: moves) {
                String[][] boardCopy = boardCopies[boardCopiesIndex];
                changeBoard(boardCopy, "O", move[0], move[1]);
                if (checkForWinsForAI(boardCopy, "O")) return move;
                boardCopiesIndex++;
            } return new int[0];
        }
    }

    public static String[][] clone(String[][] board) {
        String[][] newBoard = new String[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                newBoard[i][j] = board[i][j];
        return newBoard;
    }

    public static int[][] movesThatCanBePlayed(String[][] board) {
        int amountOfMoves = 0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j].equalsIgnoreCase(""))
                    amountOfMoves++;
        int[][] moves = new int[amountOfMoves][2];

        int movesOuterIndex = 0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j].equalsIgnoreCase(""))
                    moves[movesOuterIndex++] = new int[]{i + 1, j + 1};
        return moves;
    }

    public static int maxNumberInArray(int[] array) {
        int max = array[0];
        for (int i = 1; i < array.length; i++)
            if (array[i] > max) max = array[i];
        return max;
    }

    public static int minNumberInArray(int[] array) {
        int min = array[0];
        for (int i = 1; i < array.length; i++)
            if (array[i] < min) min = array[i];
        return min;
    }

    public static boolean checkForFullBoardForAi(String[][] board) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].equalsIgnoreCase("")) return false;
            }
        } return true;
    }

    private static void playAgainOrNotForAI() {
        System.out.print("\nDo you want to play again (yes or no)? ");
        String option = scanner.next();
        System.out.println("\n");
        if (option.equalsIgnoreCase("yes")) {
            initiateBoard();
            mainPlayGame();
        } else if (option.equalsIgnoreCase("give")) {
            String whatToPrint = scanner.next();
            if (whatToPrint.equalsIgnoreCase("heart")) printFingerHeart();
            else if (whatToPrint.equalsIgnoreCase("girl")) printKPopGirl(); // Sorry, I don't know her name
            else if (whatToPrint.equalsIgnoreCase("group")) printKPopGroup();
        }
    }

    private static boolean checkForWinsForAI(String[][] board, String player) {
        return (
                (board[0][0].equalsIgnoreCase(player) && board[0][1].equalsIgnoreCase(player) && board[0][2].equalsIgnoreCase(player)) ||
                (board[1][0].equalsIgnoreCase(player) && board[1][1].equalsIgnoreCase(player) && board[1][2].equalsIgnoreCase(player)) ||
                (board[2][0].equalsIgnoreCase(player) && board[2][1].equalsIgnoreCase(player) && board[2][2].equalsIgnoreCase(player)) ||
                (board[0][0].equalsIgnoreCase(player) && board[1][0].equalsIgnoreCase(player) && board[2][0].equalsIgnoreCase(player)) ||
                (board[0][1].equalsIgnoreCase(player) && board[1][1].equalsIgnoreCase(player) && board[2][1].equalsIgnoreCase(player)) ||
                (board[0][2].equalsIgnoreCase(player) && board[1][2].equalsIgnoreCase(player) && board[2][2].equalsIgnoreCase(player)) ||
                (board[0][0].equalsIgnoreCase(player) && board[1][1].equalsIgnoreCase(player) && board[2][2].equalsIgnoreCase(player)) ||
                (board[0][2].equalsIgnoreCase(player) && board[1][1].equalsIgnoreCase(player) && board[2][0].equalsIgnoreCase(player))
        );
    }

    public static void playGame() {
        printIntroWithBoard();

        System.out.println("Decide between yourselves who plays first.");
        System.out.print("Who goes first (X or O)? ");
        String player1 = scanner.next();
        boolean playerXTurn = !player1.equalsIgnoreCase("O");
        System.out.println("Let's play!");

        while (!checkForWins("O") && !checkForWins("X") && !checkForFullBoard()) {
            printBoard();
            if (playerXTurn) {
                System.out.print("Player X -- Row: ");
                int row = scanner.nextInt();
                if (row > 3 || row < 1) {
                    System.out.println("Invalid input, choose a number between 1 and 3 inclusive.");
                    continue;
                }
                System.out.print("Player X -- Column: ");
                int column = scanner.nextInt();
                if (column > 3 || column < 1) {
                    System.out.println("Invalid input, choose a number between 1 and 3 inclusive.");
                    continue;
                }

                if (!changeBoard(board,"X", row, column)) {
                    System.out.println("Invalid input, placing in a taken spot. Try again.");
                    continue;
                }
            } else {
                System.out.print("Player O -- Row: ");
                int row = scanner.nextInt();
                if (row > 3 || row < 1) {
                    System.out.println("Invalid input, choose a number between 1 and 3 inclusive.");
                    continue;
                }
                System.out.print("Player O -- Column: ");
                int column = scanner.nextInt();
                if (column > 3 || column < 1) {
                    System.out.println("Invalid input, choose a number between 1 and 3 inclusive.");
                    continue;
                }

                if (!changeBoard(board, "O", row, column)) {
                    System.out.println("Invalid input, placing in a taken spot. Try again.");
                    continue;
                }
            }
            playerXTurn = !playerXTurn;
        }

        finalStatement();
        playAgainOrNot();
    }
    private static void playAgainOrNot() {
        System.out.print("\nDo you want to play again (yes or no)? ");
        String option = scanner.next();
        System.out.println("\n");
        if (option.equalsIgnoreCase("yes")) {
            initiateBoard();
            mainPlayGame();
        } else if (option.equalsIgnoreCase("give")) {
            String whatToPrint = scanner.next();
            if (whatToPrint.equalsIgnoreCase("heart")) printFingerHeart();
            else if (whatToPrint.equalsIgnoreCase("girl")) printKPopGirl(); // Sorry, I don't know her name
            else if (whatToPrint.equalsIgnoreCase("group")) printKPopGroup();
        }
    }

    private static void finalStatement() {
        printBoard();
        if (checkForWins("X"))
            System.out.println("Player X has won.");
        else if (checkForWins("O"))
            System.out.println("Player O has won.");
        else
            System.out.println("The game has ended in a tie.");
    }

    private static boolean checkForWins(String player) {
        return (
                (board[0][0].equalsIgnoreCase(player) && board[0][1].equalsIgnoreCase(player) && board[0][2].equalsIgnoreCase(player)) ||
                (board[1][0].equalsIgnoreCase(player) && board[1][1].equalsIgnoreCase(player) && board[1][2].equalsIgnoreCase(player)) ||
                (board[2][0].equalsIgnoreCase(player) && board[2][1].equalsIgnoreCase(player) && board[2][2].equalsIgnoreCase(player)) ||
                (board[0][0].equalsIgnoreCase(player) && board[1][0].equalsIgnoreCase(player) && board[2][0].equalsIgnoreCase(player)) ||
                (board[0][1].equalsIgnoreCase(player) && board[1][1].equalsIgnoreCase(player) && board[2][1].equalsIgnoreCase(player)) ||
                (board[0][2].equalsIgnoreCase(player) && board[1][2].equalsIgnoreCase(player) && board[2][2].equalsIgnoreCase(player)) ||
                (board[0][0].equalsIgnoreCase(player) && board[1][1].equalsIgnoreCase(player) && board[2][2].equalsIgnoreCase(player)) ||
                (board[0][2].equalsIgnoreCase(player) && board[1][1].equalsIgnoreCase(player) && board[2][0].equalsIgnoreCase(player))
        );
    }

    private static boolean checkForFullBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].equalsIgnoreCase("")) return false;
            }
        } return true;
    }

    private static boolean changeBoard(String[][] board,String player, int row, int column) {
        if (board[row - 1][column - 1].equalsIgnoreCase("")) {
            board[row - 1][column - 1] = player.toUpperCase();
            return true;
        } return false;
    }

    private static String printBoardFormattedReturns(int row, int column) {
        if (!board[row - 1][column - 1].equalsIgnoreCase("")) return (" " + board[row - 1][column - 1].toUpperCase() + " ");
        else return "   ";
    }

    public static void printBoard() {
        System.out.println();
        System.out.println(
                printBoardFormattedReturns(1, 1) + "|" + printBoardFormattedReturns(1, 2) + "|" + printBoardFormattedReturns(1, 3) + "\n" +
                        "___ ___ ___" + "\n" +
                        printBoardFormattedReturns(2, 1) + "|" + printBoardFormattedReturns(2, 2) + "|" + printBoardFormattedReturns(2, 3) + "\n" +
                        "___ ___ ___" + "\n" +
                        printBoardFormattedReturns(3, 1) + "|" + printBoardFormattedReturns(3, 2) + "|" + printBoardFormattedReturns(3, 3)
        );
    }

    public static void printIntroWithBoard() {
        System.out.println("Welcome to Tic Tac Toe.");
        System.out.println("The following is the game board.");
        System.out.println("(Remember, if the game is set to tie, just continue the game till the end)");
        System.out.println(
                """
                            1   2   3 <--- Columns
                        1    |   |  \s
                           ___ ___ ___
                        2    |   |  \s
                           ___ ___ ___
                        3    |   |  \s
                        ^
                        Rows
                        """
        );
    }













    public static void printFingerHeart() {
        System.out.println(
                """
                            ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                            ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠋⣉⡙⠛⣉⣉⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                            ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠸⣿⣿⣾⣿⡿⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                            ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⡘⠻⣿⠟⣡⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                            ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⢿⣿⣶⣤⣼⠟⠻⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                            ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢡⣶⣦⡌⠻⡿⢁⣾⣷⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿
                            ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡄⢻⣿⣿⣦⡀⢸⣿⡏⣸⣿⣿⣿⣿⣿⣿⣿⣿⣿
                            ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⠙⢿⣿⣿⣆⠙⢠⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                            ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⡌⠻⣿⣿⣷⣄⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿
                            ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢡⣴⣤⡘⢿⣿⣿⣧⡈⢿⣿⣿⣿⣿⣿⣿⣿
                            ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡟⢋⣁⠘⢿⣿⣿⣆⠙⣿⣿⣷⡀⢻⣿⣿⣿⣿⣿⣿
                            ⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠀⣿⣿⣷⣄⠻⣿⣿⣿⣿⣿⣿⣷⠈⣿⣿⣿⣿⣿⣿
                            ⣿⣿⣿⣿⣿⣿⣿⡟⢠⣶⣤⡈⠻⣿⣿⣷⣿⣿⣿⣿⣿⣿⠏⣰⣿⣿⣿⣿⣿⣿
                            ⣿⣿⣿⣿⣿⣿⣿⣿⡈⢿⣿⣿⣶⣼⣿⣿⣿⣿⣿⣿⣿⠋⣰⣿⣿⣿⣿⣿⣿⣿
                            ⣿⣿⣿⣿⣿⣿⣿⣿⣷⣄⠙⢿⣿⣿⣿⣿⣿⣿⡿⠟⣡⣾⣿⣿⣿⣿⣿⣿⣿⣿
                            ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣦⣉⠛⠛⠛⢛⣉⣴⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                            ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                            ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                """
        );
    }

    public static void printKPopGirl() {
        System.out.println(
                """
                                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣋⣷⣾⣷⣞⣽⣿⣾⣿⣿⣿⣻⣹⣿⣿⣿⣿⣿⣿⣟⣿⣿⣿⣟⣿⣞⣿⣷⣮⣻⣼⣝⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣾⠟⣷⣿⣿⣯⡿⣳⣟⣿⣟⣿⣿⣷⡟⣾⣿⢳⣻⣷⣿⣻⣾⣿⣿⣿⣏⣿⣧⢙⢾⡿⣌⣇⢆⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                                ⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣣⡿⣿⣫⢫⣧⣯⣾⣧⣾⠷⣾⣿⣿⣿⢹⣿⣿⣜⢻⡿⣿⢷⣿⠟⠿⣿⡟⣾⡟⣏⣏⢷⡹⡿⣐⢣⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                                ⠀⠀⠀⠀⠀⠀⠀⠀⢀⣼⡿⡿⣷⣡⣯⣯⣣⡟⡎⣯⢷⡿⣿⡿⢻⣿⡏⢹⡅⡈⠳⣿⢾⡝⣯⡄⠛⣿⡿⣿⣿⡯⣿⣗⢾⣷⣁⠉⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                                ⠀⠀⠀⠀⠀⠀⠀⠀⠸⣿⣿⡼⣯⢏⣿⡿⣾⣿⡝⡿⣬⢿⣻⡑⣙⣿⢁⠀⠀⢾⣺⣄⣥⡷⣽⣿⢧⢿⣿⣭⣷⣾⡷⣿⡎⣿⢿⢾⣆⢿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                                ⠀⠀⠀⠀⠀⠀⠀⠀⣼⣹⣽⠿⣿⢏⣿⣇⣷⣿⣿⣧⠟⣇⣯⡇⣿⢻⠀⠇⡹⢾⢹⣿⢿⣹⢈⣾⡘⡞⣿⢏⣿⢟⣾⣼⢳⡘⢇⢻⣿⣾⢾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
                                ⠀⠀⠀⠀⠀⠀⠀⠀⡿⣿⣿⣴⣽⡾⣿⠛⣈⣾⣿⢸⢾⣴⠿⢹⡻⠀⠀⠀⠡⠼⡸⡜⢿⣇⡾⢹⣧⣵⠸⡚⡼⡜⣾⡏⢻⣷⣿⣾⡛⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿
                                ⠀⠀⠀⠀⠀⠀⠀⣸⣱⣿⣿⣿⣟⡾⣯⡷⢷⣿⡧⡈⠀⠛⠇⢸⠀⠀⠀⠀⠀⠀⠃⠱⠛⠘⣇⠾⣿⣧⣃⢻⡾⢽⣆⢾⣻⣿⣯⣿⣿⡿⢿⣹⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠇
                                ⠀⠀⠀⠀⠀⠀⢠⣾⡿⣿⣿⣿⣿⡼⣿⣇⡏⣿⠃⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠀⠏⠋⢻⡞⡿⡿⣿⠘⣌⢿⡿⣽⢿⡟⢣⡏⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡟⠀
                                ⠀⠀⠀⠀⠀⠀⣿⣿⣹⣿⣿⣽⣿⣷⣟⣼⡿⡷⢀⣀⡆⣀⣸⡷⢠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠸⣶⣵⢸⣥⣷⣷⣥⢈⢻⡍⢯⣿⡌⣁⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠃⠀
                                ⠀⠀⠀⠀⠀⢸⣿⠋⣏⢘⣿⣿⣿⡿⣹⣿⠰⠃⣜⣼⣼⣷⣿⣴⣼⣼⣾⢣⢠⢀⠀⠀⠀⠀⠀⠀⠀⠐⡏⢻⣷⣿⡿⣾⡏⢺⢺⡟⢸⣿⠝⠁⣔⢀⠜⠋⣿⣿⠿⠛⠻⠿⢏⣀⣠
                                ⠀⠀⠀⠀⠀⠈⢻⣷⣽⣲⣿⣿⣿⣿⡿⠋⢠⢾⣿⣿⡿⠛⠛⠛⠻⠿⢟⡾⣾⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠃⣀⣘⣌⣷⣞⣞⣧⢸⡏⠀⠱⣎⣉⣀⣴⣿⠃⠀⠀⠀⠀⠀⠀⠉
                                ⠀⠀⠀⠀⠀⠀⠀⢻⣿⣿⣧⣄⡙⢛⣥⡶⠷⢟⡥⠚⠁⠀⣠⣤⣬⣤⣼⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⣾⣿⠛⢿⣿⠿⣿⣾⣜⣧⠠⠴⠋⠀⠻⢯⣿⣆⡀⡀⠀⠀⠀⠀⠀
                                ⠀⠀⠀⠀⠀⠀⠀⣉⢻⢹⣿⣟⣿⣿⣿⡇⠀⠀⠀⠠⣴⣾⡿⠛⣿⣿⣿⡿⢿⡄⠀⠀⠀⠀⠀⠀⠀⠀⣎⣿⣿⠟⠀⠘⡇⠀⢹⣿⣽⣿⣷⣿⡀⠀⠀⠀⠙⠛⠛⢛⠳⡀⠀⠀⠀
                                ⠀⠀⠀⠀⠀⠀⠰⣸⣠⣼⣿⣽⣍⠿⣿⣧⠀⠀⠀⠀⠀⠀⠀⠐⣸⣿⣟⠑⠛⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⠛⠓⠋⠀⠀⢸⣿⣿⣿⣿⣿⣷⡀⠀⠀⠀⠀⠈⡍⠁⡘⠆⠀⠀
                                ⠀⠀⠀⠀⠀⠀⢠⢃⣵⣿⢻⣽⣿⣷⡮⡿⡄⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣸⣿⣿⣿⣿⣿⣿⠇⠀⠀⠀⠀⠀⢻⣄⠠⢄⠀⠀
                                ⠀⠀⠀⠀⠀⠐⡾⣾⣿⣿⣿⣿⣿⣿⣷⣌⠹⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⣿⣿⡿⠿⠛⠋⠁⠀⠀⠀⠀⠀⠀⠈⠙⢶⡮⠃⠀
                                ⠀⠀⠀⠀⠀⠀⠁⢈⠛⣿⣿⣿⣿⣿⣿⣿⣿⣷⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣾⠟⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⡀⢀⢀⣴⠟⠃⠀⠀
                                ⠀⠀⠀⠀⠀⠀⣷⣜⣾⣿⣇⣿⣿⣿⣿⣿⣿⣿⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣸⣷⣄⠀⣀⣀⣤⣤⣤⣶⣶⣶⣌⡀⠁⠏⠀⠀⠀⠀⠀
                                ⠀⠀⠀⠀⠀⠀⡜⡽⣻⣿⣼⣿⣿⣿⣿⣿⣿⣿⣿⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠒⠚⠲⣤⠤⠀⠀⠀⠀⠀⠀⠀⢰⣿⣿⣿⣿⣿⣿⡹⣶⣭⡻⣷⣟⣭⡽⣿⣷⣦⣤⣤⡄⠦
                                ⠀⠀⠀⠀⢠⣿⣽⣿⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣀⠠⣀⣤⣀⣀⠀⠀⠀⢠⣿⣿⣿⣿⣯⣿⣿⣷⣽⣿⣿⠘⡈⠙⠻⠿⣿⠛⣿⡟⢁⣴
                                ⠀⠀⠀⠀⢠⣿⣿⣻⣿⣿⣿⣻⣿⣾⣿⣿⣿⣿⣿⣿⣿⣷⣄⠀⠀⠀⠀⠀⠀⢤⣶⣾⠿⠿⠿⣿⣛⢻⣿⠁⢀⣴⡿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⠏⡼⢫⠳⣄⠄⣿⣾⣿⠰⡿⠡
                                ⠀⠀⢀⣼⣾⣾⣿⡿⣿⣿⡿⣿⣫⣿⣿⣾⣿⣿⣟⣿⣿⣿⣿⣷⡀⠀⠀⠀⠀⠈⠙⠿⣿⣿⣚⣛⣿⣏⠀⣰⣿⣿⣵⣸⣿⣿⢿⣿⣟⣻⠿⠛⣡⠞⢀⡦⣰⣼⠓⣿⣿⡟⠧⠃⠀
                                ⠀⠀⠸⣿⣟⣧⣹⣽⣿⣿⣿⣿⣯⣿⣿⣿⣉⡗⣽⣿⣿⣿⣿⣿⣷⡔⢤⠀⠀⠀⠀⠀⠀⣰⣿⣿⡿⠿⠿⣿⣿⣿⣿⣿⣟⡿⠞⠛⣉⣤⢶⡿⣿⣦⠞⠐⣛⣿⣿⣿⣾⣳⡶⠀⠀
                                ⠀⠀⢘⣿⠿⠟⣛⡛⠛⣛⣟⣛⣉⣒⣿⣿⣿⣿⣧⣿⣿⣿⣿⣿⣿⣿⡄⠙⠲⣄⠀⠀⠀⣿⡿⢡⣴⣿⣟⣮⠛⠉⠉⢉⣤⣴⣾⣿⣿⣻⢿⣼⢟⠇⠀⠚⠈⠉⠋⣩⣭⣭⣥⡥⢀
                                ⠀⣀⣬⣷⣾⣿⣿⣿⣿⣟⢟⢛⡫⠭⣻⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣽⣿⣶⡤⣬⣥⣤⣤⣽⣇⣿⣿⣿⣿⣿⣦⣴⣾⣿⣿⣿⣿⣿⣿⣿⣻⡵⣮⡀⠾⣴⣤⣠⣿⡵⣿⡽⣗⣫⣥
                                ⢟⢿⣿⣿⣦⣤⣾⣿⣿⣿⢯⣽⣿⣿⢿⣿⣿⣿⣏⠩⣹⣿⣽⣽⣿⣽⣿⣿⡄⠀⠀⠀⣾⡿⠉⣿⣿⣿⠿⢿⢟⡟⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠷⣼⣟⣷⣼⣿⣭⣿⣿⣟⢿
                                ⠲⢶⣿⣽⣿⣿⣿⢏⢻⣿⣿⣿⢷⣧⣷⣾⣿⣿⣿⣿⣿⢿⣿⣿⣿⣿⣯⢿⢷⡀⠀⠀⠀⠀⠀⣿⠀⠻⣾⣞⡎⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⢿⣿⢧⡀⠫⠿⣿⣿⣿⡟⠋⠈⠈⠃
                                ⠀⠀⣿⣿⣿⣿⣿⣷⣶⣿⣿⣿⣿⣿⣿⣿⣾⣿⣿⣾⡵⠭⣿⣿⣿⣿⣿⡾⢷⣧⠀⠀⠀⠀⠀⡟⠀⠀⠉⠿⣏⠈⢿⣿⣿⣿⣿⣿⣿⣿⣿⣟⣿⠈⣿⣄⣶⣿⣿⣿⡷⣿⣷⡆⠈
                                ⠀⠀⠁⣘⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣽⣿⣷⢯⡖⢸⣿⣿⣿⣻⣣⣾⣿⡷⣄⠀⠀⣸⠃⠀⠀⠀⠱⠿⣷⣾⣿⣿⣿⣿⣿⣿⣻⣿⣿⢻⣽⣻⣿⣿⣿⡿⣿⣿⢹⡿⠁⠀
                                ⠀⣐⠀⡌⠝⣿⣿⣿⣿⣿⢿⣿⣿⣿⣿⣿⣿⡷⢿⢿⣿⡴⡝⣻⣿⣿⣿⣿⣾⣿⣿⠀⠛⣼⡇⠀⠀⠀⠀⠀⠀⢻⡿⣿⣿⣿⣯⣿⣿⣯⣿⣋⠀⢼⣽⣿⣻⣿⣷⣿⣿⣿⡷⠃⠀
                                ⢧⣛⣟⣿⠦⠞⢿⣿⣿⣿⣿⣿⣿⡿⣿⣿⣿⣿⣇⣬⣿⣿⣿⣿⣿⣿⣿⣿⡟⣿⣿⣧⠀⣿⣿⠀⠀⠀⠀⠀⠀⠸⣇⣿⣿⣿⣿⣿⣿⣿⣿⣆⠶⣜⣿⣿⣿⣿⣿⣿⣿⡿⠁⠀⠀
                                ⠈⠹⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣻⣿⣿⣷⣄⡿⣿⣿⣜⡏⠙⢛⡶⣶⣿⣿⡄⠈⠁⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣿⣿⣿⣿⣻⡿⣿⡿⣽⢽⠿⣿⣿⢿⣿⡿⠃⠀⠀⠀
                        """
        );
    }

    public static void printKPopGroup() {
        System.out.println(
                """
                                ⣿⣽⣿⣿⣿⣿⣿⢿⣟⣯⣻⡽⣯⣟⡼⡵⢆⠦⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣉⢚⡰⣸⡆⢍⢣⣑⠲⣶⣒⢌⡲⢄⠣⡌⢭⡙⠧⣩⢑⢻⠆⣻⠃⡕⢪⡱⢭⢣⣍⠻⣜⢧⣏⡼⣣⣯⡝⢮⡽⣟⢯⢯⡽⣭⣛⣾⣹⢏⡽⣻⣿⣿⣿⣿⣿⣿⣿⣿⡿⣿⣿⣿⣿⣿⣹⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢿⣿⣿⣿⣿⣿⣿⣿
                                ⣿⣿⣿⣿⣿⣿⣯⣿⣾⣿⡿⣽⡾⣏⠟⣾⣋⣖⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⢌⡻⡔⠰⣣⣎⢦⡿⣧⢿⣭⠢⡙⣌⢢⠱⣦⡹⣇⠦⡌⣜⡣⣍⠳⡜⣥⢓⣿⠲⣌⢯⡝⣞⡼⣻⢷⣭⢾⣿⣽⣯⣟⡾⣽⣻⣾⣵⣏⡾⣳⢹⣿⣿⣿⣿⣿⣿⣿⣳⢿⣽⢯⣟⣿⢿⣟⣿⣿⣿⣿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣾⣟⣾⡿⣽⣿⣯⣿⣾⣟⣯⣿⣿
                                ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣟⣛⣾⣛⣼⣯⣜⠆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢰⣎⡱⣍⡳⢴⣿⢎⣿⣿⡾⣽⣿⣷⣮⣖⣹⣦⣽⣹⣲⣽⡦⣝⣦⢹⣽⣬⣋⣾⣟⣼⣫⣿⣾⣽⣿⣿⣯⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣽⣷⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣫⣿⣿⣟⡿⣿⠼⣯⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠿⠿⢷⡿⠿⠿⢿⣟⣾⡷⣟⡾⣿⢿⣿
                                ⣿⣿⣿⣿⣿⣿⣿⢻⣷⣿⣻⣿⢿⣻⣾⣧⣻⡽⣳⢦⣀⠀⠀⠀⠐⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿⣾⣷⣿⣿⣿⣿⣿⣿⡟⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠙⣿⣿⣿⣿⣿⣿⣿⣿⣿⢼⣳⣿⣯⣿⢯⣿⢿⡻⣿⣿⣿⣿⣿⣿⣿⣿⠟⡉⢂⢉⣢⣿⡿⣙⡉⠛⠤⡀⠉⠛⢯⣿⣽⣻⣿
                                ⣿⣿⣿⣿⣿⢏⡾⣿⣽⣻⣷⣿⡿⣿⡷⢿⣻⣽⢷⡪⣍⡷⡀⠠⠀⠁⠀⠀⠀⠀⠀⠀⠀⠀⠂⠀⠀⢸⣿⣿⣿⣿⣿⣿⣿⣿⢿⡿⣀⣀⣀⣀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣠⣴⣾⣿⣿⣷⣾⣿⣿⣿⣿⣿⣟⡷⣻⣿⣿⢸⡿⣽⣷⣿⣿⣿⣧⣿⣿⣿⣿⣿⣿⣿⠟⡠⢂⡡⠄⢲⣿⡟⠬⠌⠈⠂⠀⠀⡐⠀⠀⢻⣾⣿⣿
                                ⣿⣿⣿⣿⡃⣾⣹⠿⣿⣻⡽⣿⣿⢷⣿⣏⢿⣞⣧⢻⣐⠻⣽⣆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣼⣿⣿⣿⡽⣷⣻⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣄⣤⣄⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣣⡟⡜⢯⣿⣯⣿⣿⣿⣿⣿⣿⡻⣿⣿⣿⣿⠃⢦⠑⢂⠀⠔⣫⡷⢍⠂⠀⠀⠀⠀⠳⣄⠀⡂⠈⢟⣿⣿
                                ⣿⣿⣿⠇⣰⠧⠁⠛⠽⠹⡯⢟⠸⡏⢧⠻⣄⠻⣎⢧⠧⣙⠄⣻⡆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠐⠆⣿⢿⡿⣯⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣝⣻⠸⣿⣯⣿⣿⣿⣿⣿⣿⣷⣿⣿⣿⢃⠌⠢⡉⠄⣠⠞⠁⠘⠀⠄⠀⠀⠀⠐⣂⠈⠳⣙⡄⠈⢿⣿
                                ⣿⣿⡿⢂⣿⢀⡆⣄⢠⣀⢀⡴⣆⣽⣞⣦⣽⣳⣿⣮⢗⢮⠘⡆⣿⡄⢀⠀⠀⠀⠀⠀⠀⠀⣈⠩⢐⣯⢯⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠛⣿⣿⣿⣿⣿⣿⣿⣿⣿⡜⢧⣿⣯⣻⣿⣿⣿⣿⣿⣿⣟⣿⡟⡠⠎⣵⠁⡼⠁⠀⠀⠀⠀⠀⠀⠀⠘⢥⣄⠣⢪⡙⣾⡄⠌⣿
                                ⣿⣿⣧⣿⣿⣻⣿⣯⣿⣿⣯⣿⣿⣿⣿⣿⣿⢿⣽⣿⢫⡞⣇⢳⡼⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⢸⣯⣾⣿⣿⣿⣿⣿⣿⡿⠟⠋⠜⠉⠻⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣿⣿⣿⣿⣿⣿⣿⣿⠟⠀⠀⠐⠀⠉⢻⣿⣻⣿⣿⣿⣿⣿⢺⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣳⡱⢨⡇⣼⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⢷⣣⠼⣼⣿⣆⢹
                                ⣿⣿⢣⣿⣿⣿⢻⣯⣿⣯⡍⠀⠈⢰⡟⣿⣿⠛⠉⢹⣷⠙⣯⠘⣷⢻⠀⠐⠀⢠⠀⡖⠀⠀⠀⠀⢠⢳⣿⣿⣿⣿⣿⣿⡏⠀⠀⠀⠀⠀⠀⠈⣿⣧⢹⣿⣿⣿⣿⣿⣿⣷⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣾⣿⣿⣿⣿⣿⣿⡟⡏⠀⠀⠀⠀⠀⠀⠀⢹⣷⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⣿⢰⣧⣤⠀⠀⠀⢠⣤⣶⡞⠓⠒⠂⠀⠀⢻⡌⠁⢻⣷⡌
                                ⣿⣿⢸⡗⣿⣿⠻⠭⠿⠛⠀⠀⠀⠀⠉⠉⠁⠀⠀⠈⣷⢙⢸⠣⣿⡞⢬⡡⠖⡈⠴⠁⠒⠀⢀⡠⠄⣿⣿⣿⣿⣿⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠘⣿⡆⠛⣿⣿⣿⣿⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⣿⣿⣿⣿⣿⣿⣿⣿⠃⠀⠀⠀⠀⠀⠀⠀⢀⣿⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣗⡿⢸⣿⣟⡛⣀⠀⠀⢩⡶⣶⣶⡶⠖⠀⠀⠈⠀⠃⢸⡟⡆
                                ⣿⣿⠰⢩⣿⣿⠀⠀⠀⠀⠠⠀⠀⠄⠀⠀⠀⠀⠀⠀⣿⣊⠔⣃⢿⡹⢦⡙⡜⢰⠋⠰⠡⠀⠀⠂⢤⣿⣿⣿⣿⣿⣿⡇⠀⠀⠀⠀⠀⠀⣀⣤⡤⠼⣷⡈⠘⣿⡿⢯⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣤⣄⣀⠀⠀⠴⠞⢋⣉⣹⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣯⡇⢻⣿⠿⠿⢩⡇⠀⠈⠛⠛⠉⠀⠀⠀⠀⠀⠐⠉⣸⡇⣏
                                ⣿⣿⣳⣻⣿⣿⣆⠀⠀⠈⠲⠓⠛⠓⠀⠀⠀⠀⠀⢘⣿⡇⠆⡣⢞⣛⡦⢹⡰⡂⠘⢂⡰⠁⢠⢈⢸⣿⣿⣿⣿⣿⣿⣟⣛⣻⡓⢦⠀⠈⢩⣶⣷⣾⣿⡇⠀⢻⡗⣆⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿⣿⣿⣿⣿⣿⣿⣷⣶⣬⣤⢠⠀⠀⢾⡿⠟⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡷⣿⣿⡁⢽⡀⠀⠀⣼⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⣦⣾⣿⢧⠘
                                ⣿⣿⣳⢿⣽⡿⣿⣆⠀⠀⣤⣾⣿⣷⣦⡤⠀⠀⠀⣾⣿⠃⡅⣑⣻⡜⣧⠁⠄⢱⣚⠂⡂⠀⠀⠠⢾⣿⣿⣿⣿⣿⣿⠿⣯⠿⠿⠲⡆⠀⠈⠉⠉⠀⠈⣿⢠⢹⠖⣩⣿⣿⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣾⣿⣿⣿⣿⣿⣿⣿⣷⡿⠛⠋⠁⢸⠀⠀⠀⠀⠀⠀⢸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡷⣿⣿⡄⢻⡄⠀⠀⢿⣤⡠⠄⠀⠀⠀⠀⠀⠀⠀⣼⣿⢫⣿⢬⢣
                                ⣿⣿⣏⡿⣞⣿⣻⣿⣦⡀⠘⠻⠿⠟⠋⠀⠀⢀⣴⡿⣾⠀⡔⠢⣽⠻⡼⡌⡝⡠⣍⠀⠄⡀⠡⢀⣿⣿⣿⣿⣿⣿⣿⡔⠀⠂⠀⢠⡇⠀⠀⠀⠀⠀⠀⢽⡇⢺⣥⣿⣿⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣿⣿⣿⣿⣿⣿⣿⣿⣷⡇⠀⠀⠀⣺⡀⢀⡀⠀⠀⠀⣸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣯⣿⡇⣿⣿⣧⠘⣿⡀⠀⣀⣦⣴⣄⣀⣀⠀⠀⠀⠀⣰⢃⡯⣼⣿⡞⡴
                                ⣿⡳⣞⡝⣻⢷⡿⣿⢿⣿⣦⣄⡀⢀⣀⣠⠴⣿⣿⣟⣿⠀⡆⡑⣯⡿⣱⢃⠼⡕⢺⣠⠥⠌⡡⢄⢿⣿⣿⣿⣿⣿⣿⣯⡄⠀⠀⠻⠵⡤⠖⠂⠀⠀⠀⢸⣟⣾⣷⣾⣿⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿⣿⣿⣿⣿⣿⣿⡷⣧⠀⠀⠀⠉⢉⠁⡀⠀⠀⣰⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡷⣿⢣⣿⣿⡙⡆⢹⣿⣄⠙⣿⣽⣽⠞⠁⠀⠀⢀⠴⠁⠀⣿⣞⣿⡿⣜
                                ⣯⣳⢣⡇⣟⣽⣿⣻⣯⢿⣷⠈⠉⠉⠉⠁⢠⣿⣽⣻⢿⡃⠄⡱⢳⣽⡒⣯⠘⡭⢏⡴⣂⢒⠱⣎⣿⣿⣿⣿⣿⣿⣿⣿⣷⡀⠀⢀⣠⣤⣤⣤⣄⠀⠀⣼⣷⣿⣿⣿⣿⣿⣿⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣽⣿⣿⣿⣿⣿⣿⣿⣿⣟⣿⣧⡀⠰⣾⡿⢯⣽⡟⣵⣿⣿⢛⡋⠙⣿⣿⣿⢿⣿⣿⣿⣿⣿⣿⣽⣿⢸⣿⣿⡀⣷⠸⣿⣿⣷⡄⠀⠀⠀⠀⣀⠴⠋⠀⠀⠀⢹⣿⣿⣿⣿
                                ⣷⣩⡷⢿⣣⢿⡳⢿⡇⣿⣿⣦⠀⠀⠀⠀⠈⢱⣿⣿⢹⣇⠂⡱⡙⣮⣷⢣⡃⢽⠦⣙⣤⣫⠴⣺⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⡠⠀⠙⠷⠼⠖⠉⠀⢠⣿⡧⣿⣿⣿⣿⣿⣿⣿⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⡟⣿⣿⣷⣄⠈⠉⠉⢁⣾⣿⠋⠋⣿⣇⣀⣹⣿⣿⢸⣿⣿⣿⣿⣿⣿⣿⣾⢸⣻⣿⢤⢸⠀⣿⣿⣿⣿⠷⠶⠖⠊⠁⠀⠀⠀⠀⠀⢸⡛⠿⣿⣿
                                ⠿⣷⣏⡿⣃⡞⣿⢺⣏⣽⣿⣿⡀⠀⠀⠀⠀⢘⣿⣿⢌⢿⡀⠣⣜⢹⠾⣭⣳⢎⣿⣟⡾⣽⣳⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠳⢦⣀⣀⣀⣠⠴⠫⢿⢃⣿⣿⣿⣿⣿⣿⣿⡟⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⢹⣿⣿⣿⣿⣿⣿⣿⣿⣯⣹⣿⣿⣿⢻⢶⣾⡿⣩⣴⣾⣿⣿⣿⣿⣿⣿⡿⢸⣿⣿⣿⣿⣿⣿⣿⣿⣼⣟⡯⣱⢸⠀⣹⣿⣿⣟⠣⣀⠀⠀⠀⠀⠀⠀⢀⡴⠃⠀⠀⢀⠞
                                ⣤⡿⣧⣏⠽⡅⣿⢹⡯⣼⢿⣿⣷⡀⠀⠀⠀⢸⣿⣿⡞⣸⢧⠱⣜⡎⢿⢳⡽⣾⣾⣿⣿⣷⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣇⠂⠈⠉⠉⠀⠀⠀⡟⠠⢾⣿⣿⣿⣿⣿⣿⣷⣌⣢⣤⡤⣤⣤⣄⡀⠀⠀⠈⢿⣿⣿⣿⣿⣿⣿⣿⣷⡦⢹⣿⡼⣿⣿⣿⣷⣿⣿⣿⣿⣿⣿⣿⣿⣿⠃⠈⣿⣿⣿⣿⣿⣿⣿⣿⣯⡷⡿⣸⡆⡇⢹⣿⣿⣿⣇⠠⢀⣀⣀⣠⢤⣖⡿⠥⠀⠀⠀⠈⠀
                                ⢻⡽⣷⣻⡆⣿⢸⣟⣇⢻⡞⢯⢹⣗⠀⠀⠀⣼⣿⣿⣿⣐⠻⣇⡹⡜⠸⡗⣎⣿⣿⣿⣿⣿⣿⣿⡿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠸⣿⣿⣿⣿⣿⣿⣽⣿⣻⣿⣻⣿⠟⠿⣿⣶⡶⠿⠿⣿⣿⣿⣿⣿⣿⣿⣿⡷⡄⠻⣿⡲⣜⣻⣿⠼⢻⣿⣿⣿⣿⣿⣿⣷⡖⠒⠿⣿⣿⣿⣿⣿⣿⣿⣿⣇⡿⢼⠃⠇⣸⠛⠟⠋⣹⣿⢿⣿⣷⡾⠋⠀⠀⠀⠀⠀⡘⠀⠀
                                ⣹⣟⣷⣹⣷⣹⡏⣾⣯⢼⣿⢪⢹⣿⡆⠀⢠⣟⠿⠮⣿⣎⠹⡝⢶⣏⠰⣙⢾⣽⣿⣿⡿⢫⣿⣿⢿⣷⣿⣿⣽⣿⣿⣿⣿⣿⡌⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣙⣽⣷⣿⣿⣶⣷⠀⠻⡷⠟⣭⡕⡀⠀⠈⠛⢿⣿⣿⣿⣿⣿⣿⣧⠀⢻⣷⣼⠏⠀⡆⣿⣿⣿⣿⣿⣿⣿⣿⠯⡛⡵⢬⣿⡍⣻⢿⣿⣿⣿⣯⡇⣿⠀⢂⣿⣋⠐⡀⠈⠋⠑⠏⠀⠀⠀⠀⠀⠀⠀⡰⠀⠄⠁
                                ⣿⣞⡷⣯⢿⣷⢻⣿⡷⣾⣩⣻⡿⣿⣿⣶⣾⣿⣷⣌⣀⣾⣆⡹⢸⣿⠠⣏⡿⣾⣿⣿⢷⣿⣻⣿⣿⡾⢲⢍⣿⣿⣿⣿⣿⣿⠄⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣭⡴⣾⣿⣟⡿⠳⡀⠀⠀⠀⠀⠀⠉⠉⠀⣦⣞⡡⠁⢻⡙⠉⠉⠉⢆⠈⡟⠁⠀⣠⡓⣏⣿⣿⣿⣿⣿⣿⣿⡿⠁⠜⣞⢡⣶⡟⠀⠆⣼⢛⣿⡇⣿⡃⡄⡛⠤⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠁⠂⠀⠀
                                ⣿⢾⡽⣯⣿⣾⣟⣾⡽⢯⣿⡽⣤⣀⣿⣿⣄⠉⠻⠿⢿⣿⣿⣳⢣⣿⠜⣽⣻⣿⣿⣯⣟⡏⡿⠀⣸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣊⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢿⣿⣟⣰⣟⣻⣿⣿⡟⠀⢈⣃⡀⢶⠨⣧⠠⡄⠀⢹⣿⣧⠶⠰⡇⠁⠀⠀⠨⠊⠀⠀⣴⠿⡴⢃⠿⡿⠟⠛⠉⠉⠉⠧⠀⡰⠁⠤⡁⠀⢀⠀⣬⢾⣿⣟⣿⣧⠄⡑⠂⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠤⣴⣆⠉⡀
                                ⣯⢿⡽⣿⣻⣽⣯⣷⣟⣷⣾⣯⢩⣽⡿⣿⣿⣶⣄⡤⣡⣿⣿⣿⠼⣿⡩⣾⣿⣿⣿⣧⢟⠁⣿⠃⢩⢿⣿⣿⣿⣿⣿⣿⣿⠏⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿⣼⢭⣿⣿⣿⡇⡤⠤⣉⠋⣦⠦⢿⡎⣣⠀⠪⠓⠁⡰⡶⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⡃⢈⡜⣹⡆⠀⠀⠀⢀⡜⢤⢣⠆⣠⣌⢁⠗⠀⠘⣿⡿⠋⠻⣿⠆⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⠀⠈⠙⠛⠻
                                ⣛⢾⣹⣿⣟⣿⣽⣟⣯⣿⣿⣿⣿⣿⡇⠸⠛⢿⣟⣿⣿⣿⣿⡿⡽⣾⡱⣿⣿⣿⣿⣽⡸⠀⡟⠀⣸⣾⣿⠟⢻⡙⠿⣿⡟⠈⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢻⣿⣿⣷⡿⣿⣿⡇⢤⣀⠨⢕⢄⠑⢄⠻⡟⠀⠀⣴⡞⡻⠁⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠁⠀⠀⠙⠣⠀⠀⢠⡾⠲⠁⠙⣄⢿⠇⠉⠀⠄⠒⡮⠒⠚⠙⡾⣎⣤⡤⠠⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠁⠀⠀⠀⠀⠀
                                ⡹⢾⣹⣯⣟⡿⣷⣿⣿⣿⣿⣏⢽⣿⣿⣶⡾⣿⣿⣿⣿⣿⣿⣏⣟⣿⢸⣿⣿⣿⣿⡟⡇⢸⡇⢠⡷⣿⡿⣿⡟⣻⣶⠻⣿⣷⣾⣿⣿⣿⠻⠿⣖⣶⣶⣲⣶⣶⣶⡶⣾⣿⣿⣿⣿⣿⣿⡃⢸⡿⣧⠐⠈⡮⡊⠫⣡⠀⠂⢉⡑⠢⡀⠈⢄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⡀⢠⡟⢒⡶⣾⣓⡂⠑⠄⠠⣔⣃⣾⣿⠴⣫⣶⣶⣼⡏⠀⠀⡀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                ⢹⡇⣾⢷⣯⢟⡯⣟⣾⣿⣿⣌⢺⣿⣟⣩⣿⣿⣿⣿⣿⣿⣿⣧⢿⣻⡽⣟⣿⣿⣿⢹⡇⣸⠁⢸⢸⣿⣯⠟⢯⣜⣀⣿⣿⣿⣻⣿⣛⣿⣿⣿⣾⣫⣿⣧⣖⣼⡿⢿⣿⣿⣿⣿⣿⣿⣿⠃⣼⣿⠟⠁⢀⡗⠃⠀⠈⢦⡀⠘⠆⣀⡕⠂⡊⠶⣤⣀⣀⠀⣸⡀⠀⠀⢀⣀⣴⣻⣧⢞⣠⣥⣐⠬⠙⣧⠀⠹⠦⣼⠿⠃⠀⣸⣿⣿⣾⡇⢻⠀⠀⠀⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                ⣱⢹⣟⣾⣻⢾⣹⢯⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⣻⣴⣻⢭⣿⣿⣿⣿⣿⡽⢺⡇⣿⠀⢼⢸⣿⣹⡶⠟⢓⣤⣾⣿⣿⣿⣦⣴⣯⣹⣿⣿⣷⣿⣿⣿⣿⣷⣾⣿⣿⣿⣿⣿⣿⣿⢀⣶⣀⣤⡼⠛⠤⡢⡴⡛⠀⠈⢢⡀⢀⠀⠀⠈⢦⣔⡛⢂⠀⠿⡏⠉⢽⠿⡟⢻⣹⢏⣿⣿⣿⣿⣷⡄⠸⣇⠐⢧⠹⢷⣄⢄⠙⠺⠿⣯⠀⣸⠀⠘⢶⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                ⣤⢟⣼⡧⣟⡿⣸⣼⣿⣿⣿⣿⣿⠘⠛⠻⠛⠻⢿⣿⣧⣻⣿⣿⣧⣼⣿⣿⣿⣧⡟⣿⡇⣿⠀⡿⢸⣿⣿⡿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⣿⣿⣻⢧⣿⣿⣜⣧⣿⣿⣿⣿⣿⣿⣿⣿⣿⢀⠿⠟⢃⢀⡄⠀⡿⠿⣿⠿⠇⣠⡟⠛⠘⢠⣀⠀⠿⣿⠃⢀⡇⢻⠀⢻⣤⣿⣿⠟⡘⡿⣿⣿⣿⡟⡇⠀⢛⡄⠀⣿⣤⢛⢻⡛⠄⠀⠧⢼⡿⡄⠀⠀⠣⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                ⡟⣼⡾⣽⣳⣳⡞⣮⣿⣿⣿⣿⡻⠆⠀⠀⠀⢹⠸⠿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢻⣿⠃⣿⠀⣻⣹⣿⣯⣤⣿⣿⣿⣿⣿⠻⢛⣿⣿⣿⣳⢛⡾⢟⣳⣾⣭⡟⣽⣿⣿⣆⡸⢿⣿⣿⣿⠀⢶⢞⣛⢿⣀⠀⠀⠀⣽⡶⣬⠁⠘⡆⠀⠻⠒⡆⠀⣀⠤⢸⣶⠌⡇⢀⢻⡿⢯⣄⠦⢉⡻⠭⠽⠚⠀⢀⣼⣿⠀⠈⠉⠈⠉⢻⣚⢣⡄⣤⠀⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                ⣹⢮⣟⡽⣷⣿⢻⣿⣿⣿⣿⣿⡏⠙⠒⠦⠜⠉⠀⠀⢸⣿⣿⣿⣿⣿⣿⣿⣿⣏⣾⡿⢸⡏⠃⣹⣼⣿⣿⣽⣿⣿⣿⢟⣅⡰⣾⣿⣿⣟⡻⣿⣷⣶⣶⣾⣷⣾⣶⣿⣿⣿⡗⢻⣿⣿⣿⡰⡞⠞⢿⣧⣉⠀⠀⠀⢿⢾⡿⣧⢀⣹⢴⣀⡴⠴⠎⡀⠀⣾⡏⢀⣷⠀⡷⢾⡹⡛⢾⢷⡆⠀⠀⠀⠐⠌⠙⠛⢇⠀⠀⠀⢀⣤⣻⣶⣩⠥⠐⡄⠀⠀⠀⠖⠋⠲⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
                                ⣯⢻⡾⣽⣻⣯⣹⣿⣿⣿⣿⣿⣧⠀⠀⠀⠀⠆⠀⠀⢸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⣿⣥⢦⡿⣿⣿⣿⣽⣿⣿⣟⠞⣄⠴⠿⣿⣿⣿⡧⣹⣿⣿⣿⣿⣿⣿⣿⣿⣿⡟⠋⠛⣿⣿⣿⣏⡃⠄⠀⡷⢡⡶⠀⠄⠈⠓⠻⠓⠋⠉⢺⣟⠀⢢⡀⠀⠀⣿⣓⣫⣤⠐⣰⠺⠡⠩⡍⠉⣀⡀⣄⠠⠀⠂⠔⠀⠘⠃⠘⣿⢆⣢⣭⡇⡻⢬⢹⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠
                                ⣧⡿⣽⡏⣿⣿⣿⣿⣿⣿⣿⣿⣧⡠⡄⠀⠀⠂⢀⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡃⣼⣇⣿⣿⠉⢹⣿⣿⣿⣶⡀⠀⠄⣿⣿⣿⣿⣞⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣢⣼⣿⣿⣿⡆⣵⣮⣡⡽⠳⢦⢀⢯⡷⢀⣀⡠⠀⠢⠌⠁⠘⠺⢿⣇⡖⢻⣵⢻⣿⠐⣯⣤⣻⢴⡇⠀⣙⡛⢀⡴⢒⠂⠀⢁⠀⠀⣰⣾⢸⣷⢤⡿⢦⢀⢿⢧⡀⠀⠀⠀⠀⠀⠀⠂⠀⠀⠀⠀⠀⠀⠀⠐⢽
                                ⠫⠔⡉⠄⣉⢹⢿⣿⣿⣿⣿⣿⣿⣛⣿⣾⣿⣾⣿⣿⣿⣿⣿⣿⣷⣾⣿⣿⣿⣿⣿⣿⣿⣶⣽⣿⣿⣿⠀⠈⣿⣿⣿⣿⣿⣾⣶⣿⣿⣿⣿⣿⣿⣿⣿⡿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⢀⣅⣨⠥⠶⠒⠒⠚⠀⠈⢁⠀⢀⣮⡤⣤⣄⠀⠀⠀⣿⣶⣧⣿⣿⣿⠀⠘⢯⣿⠑⢾⡌⢁⣐⣻⠏⠠⠀⠄⢡⠀⠁⠀⣹⣮⢛⣫⠵⣫⣾⣿⣶⣧⣤⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢸
                                ⡑⠌⡐⠄⠀⡐⢾⣿⣿⣿⣿⣿⠿⠿⠿⠿⠿⠿⠿⢿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠀⢸⣿⣿⣿⣿⡿⡍⢩⣿⣿⡿⢿⣿⢿⡿⣿⣿⠿⠶⣿⣿⣿⣿⣿⣿⣿⣿⢗⠩⢀⡀⣴⠆⣤⣄⠀⠀⠀⢿⣿⢾⣤⣥⡟⢿⡀⢸⠀⣴⣠⢿⡿⣿⣇⢠⢰⣾⣿⣦⣬⢿⣶⢌⣽⢃⡈⢤⠹⢷⠀⡀⠀⠉⠁⣈⣤⣶⣿⢿⡡⠃⠤⠐⡈⠄⠉⠉⠉⠙⠒⠲⠤⠄⣀⠀⠀⠀⣾
                                ⡔⡡⢂⠄⢠⣉⢾⣻⣿⣿⣿⣿⡃⠜⡀⠀⠀⡀⢂⠜⡢⣝⣿⣿⣿⣿⢿⠛⠛⠛⠛⠿⣿⣿⣿⣿⣿⣯⠀⠀⣿⣿⣿⢋⡷⠽⣿⣿⣿⣧⣼⡥⠾⢿⣼⡇⠀⠀⢻⣿⣿⣿⡿⣿⣿⣿⣿⡇⢸⣿⣴⣧⡝⢫⡄⠀⠀⢤⠀⢾⣕⣊⣿⡸⡟⠊⣧⠸⣯⣮⡛⣛⡿⢿⣿⡤⣍⠻⠍⡈⡁⢼⣈⡠⠥⠀⠀⠀⠐⠂⢡⣴⣿⣿⣿⣿⡟⢆⠡⡘⠄⡃⠔⡈⠠⠀⠀⠀⠀⠀⠄⠠⠀⡌⢰⣿⠏
                                ⡜⡡⢌⠀⠄⡘⣾⢧⣿⣿⣿⣿⡱⡘⠠⠐⠠⡐⢢⢊⠵⣩⢿⣿⣿⣿⣿⣷⣷⣶⣶⣶⣿⣿⣿⣿⣿⣿⣷⣶⣿⣿⣿⣋⡶⠺⢻⣿⣿⣇⣼⣉⡴⠋⠁⠘⣤⢼⣿⣿⣿⡿⣟⣿⣿⣿⣿⣿⢰⡛⣧⣎⣿⢐⡛⠀⠀⠈⣴⣬⠙⠛⡉⢿⣿⠀⢿⡆⠻⠛⢃⠛⣻⣿⣭⣥⠙⠷⣔⡀⡐⠈⠁⢀⡶⢤⣶⡶⢈⣤⣿⣀⣨⣿⣿⣿⡹⡌⢆⠱⡈⠔⡨⠐⠀⠀⠀⠀⣀⣈⣐⣤⣥⣴⣼⡏⠄
                                ⣳⡱⡎⢀⠂⡝⡾⣧⣿⣿⣿⣿⡥⡙⠤⢁⠂⡑⢢⢍⣲⣩⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣄⡒⢸⣿⣿⣉⣹⣾⠷⠀⡤⠞⢹⡋⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣆⣷⡏⣭⣍⡿⡿⠂⠐⡀⠈⣉⢀⠛⠋⢀⣀⣤⢚⡷⠿⠛⣛⡉⠛⡿⠿⢻⠿⣿⣮⣽⣧⣤⣤⣿⣳⣮⣽⣶⣿⣿⣿⢿⣿⣿⣿⣷⣣⣝⣬⣳⣬⣦⣵⣶⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠃⡌
                        """
        );
    }
}
