package model;

import java.util.Objects;

public class Move {
    private final Position from;
    private final Position to;
    private final Direction direction;
    private final int distance;

    public Move(Position from, Position to, Direction direction, int distance) {
        this.from = from;
        this.to = to;
        this.direction = direction;
        this.distance = distance;
    }

    public Move(Position from, Position to, Direction direction) {
        this(from, to, direction, 1);
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
        return distance == move.distance
                && Objects.equals(from, move.from)
                && Objects.equals(to, move.to)
                && direction == move.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, direction, distance);
    }

    @Override
    public String toString() {
        return "Mouvement de " + from +
                "\n vers " + to +
                "\n dans le sens : " + direction +
                " sur " + distance + " cases\n";
    }
}