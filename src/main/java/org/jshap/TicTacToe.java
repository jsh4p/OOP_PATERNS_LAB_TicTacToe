package org.jshap;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

import org.javatuples.Pair;

final public class TicTacToe {
    private final int GRID_DIMENSION;
    private char[][] field;
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;

    public TicTacToe(int GRID_DIMENSION) {
        if (GRID_DIMENSION < 3) {
            throw new IllegalArgumentException("GRID_DIMENSION must be greater or equal 3\n");
        }

        this.GRID_DIMENSION = GRID_DIMENSION;
        field = new char[GRID_DIMENSION][GRID_DIMENSION];
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        //refill();
    }

    public void play() {
        refill();

        char playerSymbol = 'X';
        boolean hasWinner = false;
        boolean hasEmptyCells = true;
        do {
            clearConsole();
            display();

            System.out.println("Your turn, " + playerSymbol);
            System.out.println("What do you wanna do?");
            System.out.println("1) Undo last move");
            System.out.println("2) Redo move");
            System.out.println("3) Make a move");
            System.out.print("Type here >> ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    if (!undoStack.isEmpty()) {
                        Command command = undoStack.pop();
                        command.undo();
                        redoStack.push(command);
                    } else {
                        continue;
                    }
                }
                case 2 -> {
                    if (!redoStack.isEmpty()) {
                        if (redoStack.peek() instanceof PlaceCrossCommand && playerSymbol == 'O'
                                || redoStack.peek() instanceof PlaceZeroCommand && playerSymbol == 'X') {
                            System.err.println("This command places another symbol, not " + playerSymbol + '\n');

                            continue;
                        }

                        Command command = redoStack.pop();
                        try {
                            command.execute();
                        } catch (IllegalArgumentException e) {
                            System.err.println(e.getMessage() + "\n");
                            redoStack.push(command);

                            continue;
                        }
                        undoStack.push(command);
                    } else {
                        continue;
                    }
                }
                case 3 -> {
                    Pair<Integer, Integer> pos = enterPos();

                    if (pos == null) {
                        continue;
                    }

                    Command command;
                    if (playerSymbol == 'X') {
                        command = new PlaceCrossCommand(field, pos);
                    } else {
                        command = new PlaceZeroCommand(field, pos);
                    }

                    command.execute();
                    undoStack.push(command);
                }
                default -> {
                    continue;
                }
            }

            hasWinner = isWinner(playerSymbol);
            if (!hasWinner) {
                playerSymbol = (playerSymbol == 'X')?'O':'X';
            }
            hasEmptyCells = !isFieldFull();
        } while(hasEmptyCells && !hasWinner);

        clearConsole();
        display();

        if (hasWinner) {
            System.out.println("The winner is " + playerSymbol + " team");
        } else {
            System.out.println("The game ended in a draw");
        }
    }

    private Pair<Integer, Integer> enterPos() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter position to make a move: ");

        int row = scanner.nextInt();
        int column = scanner.nextInt();

        if (row < 0 || row > GRID_DIMENSION - 1 || column < 0 || column > GRID_DIMENSION - 1) {
            System.err.println("row or column must be between 0 and GRID_DIMENSION - 1\n");

            return null;
            //throw new IllegalArgumentException("row or column must be between 0 and GRID_DIMENSION - 1\n");
        }

        if (field[row][column] != '_') {
            System.err.println("This pos is already occupied\n");

            return null;
        }

        return new Pair<>(row, column);
    }

    private void refill() {
        for (char[] row : field) {
            Arrays.fill(row, '_');
        }
    }

    private boolean isWinner(char playerSymbol) {
        boolean isGameOver = true;
        for (int i = 0; i < GRID_DIMENSION; ++i) {
            if (field[i][i] != playerSymbol) {
                isGameOver = false;
                break;
            }
        }

        if (isGameOver) {
            return true;
        }

        isGameOver = true;
        for (int i = 0; i < GRID_DIMENSION; ++i) {
            if (field[i][GRID_DIMENSION - 1 - i] != playerSymbol) {
                isGameOver = false;
                break;
            }
        }

        if (isGameOver) {
            return true;
        }

        isGameOver = true;
        for (int i = 0; i < GRID_DIMENSION; ++i) {
            for (int j = 0; j < GRID_DIMENSION; ++j) {
                if (field[i][j] != playerSymbol) {
                    isGameOver = false;
                    break;
                }
            }

            if (isGameOver) {
                return true;
            }

            isGameOver = true;
        }

        for (int i = 0; i < GRID_DIMENSION; ++i) {
            for (int j = 0; j < GRID_DIMENSION; ++j) {
                if (field[j][i] != playerSymbol) {
                    isGameOver = false;
                    break;
                }
            }

            if (isGameOver) {
                return true;
            }

            isGameOver = true;
        }

        return false;
    }

    private boolean isFieldFull() {
        for (int i = 0; i < GRID_DIMENSION; i++) {
            for (int j = 0; j < GRID_DIMENSION; j++) {
                if (field[i][j] == '_') {
                    return false;
                }
            }
        }

        return true;
    }

    private static void clearConsole() {
        // no idea
    }

    public void display() {
        for (char[] row : field) {
            for (char elem : row) {
                System.out.print(elem + " ");
            }
            System.out.println();
        }
    }
}
