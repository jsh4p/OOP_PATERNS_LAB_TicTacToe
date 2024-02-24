package org.jshap;

import org.javatuples.Pair;

public class PlaceZeroCommand implements Command {
    private final Pair<Integer, Integer> POS;
    private char[][] field; // оч плоха

    PlaceZeroCommand(char[][] field, Pair<Integer, Integer> POS) {
        this.POS = POS;
        this.field = field;
    }

    @Override
    public void execute() {
        if (field[POS.getValue0()][POS.getValue1()] != '_') {
            //System.err.println("This pos is already occupied");
            throw new IllegalArgumentException("This pos is already occupied\n");

            //return;
        }

        field[POS.getValue0()][POS.getValue1()] = 'O';
    }

    @Override
    public void undo() {
        if (field[POS.getValue0()][POS.getValue1()] != 'O') {
            //System.err.println("This pos is empty/occupied by X");
            throw new IllegalArgumentException("This pos is empty/occupied by X\n");

            //return;
        }

        field[POS.getValue0()][POS.getValue1()] = '_';
    }
}
