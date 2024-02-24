package org.jshap;

    public interface Command {
        void execute();

        void undo();
    }