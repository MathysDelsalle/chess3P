package com.chess.model;

public class ScoredMove {
    private final Move move;
    private final int score;

    public ScoredMove(Move move, int score) {
        this.move = move;
        this.score = score;
    }

    public Move move() {
        return move;
    }

    public int score() {
        return score;
    }
}
