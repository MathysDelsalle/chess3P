package model;

import java.util.Objects;

public class Move {
    private final Position from;
    private final Position to;
    private final Direction direction;
    private final int distance;
    private final Piece piece;

    public Move(Position from, Position to, Direction direction, int distance, Piece piece) {
        this.from = from;
        this.to = to;
        this.direction = direction;
        this.distance = distance;
        this.piece=piece;
    }

    public Move(Position from, Position to, Direction direction,Piece piece) {
        this(from, to, direction, 1,piece);
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move)) return false;
        Move move = (Move) o;
        return Objects.equals(from, move.from)
                && Objects.equals(to, move.to)
                && Objects.equals(piece, move.piece);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, piece);
    }

    @Override
    public String toString() {
        return "Mouvement de " + from +
                "\n vers " + to + "\n";
    }

    public Piece getPiece() {
        return piece;
    }
}