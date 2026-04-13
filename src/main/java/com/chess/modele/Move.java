package modele;

public class Move {
    private Position from;
    private Position to;
    private Direction direction;
    private int distance;

    public Move(Position from, Position to, Direction direction, int distance) {
        this.from = from;
        this.to = to;
        this.direction = direction;
        this.distance = distance;
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
    public String toString() {
        return "Mouvement de "+  from + " vers " + to + " dans le sens : " + direction + " sur " + distance + " cases\n";
    }
}
