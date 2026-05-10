package com.chess.model;

public interface Command {
    boolean execute();
    void undo();
}
