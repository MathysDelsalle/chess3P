package model;

import java.util.Map;
import java.util.Objects;

import model.strategy.MovementStrategy;

public class Piece {

    private PieceType type;
    private People owner;
    private Color color;
    private MovementStrategy movementStrategy;
    private boolean hasMoved=false;
    private int StartTier;
    private final int id;
    private static int nextid=0;

    public Piece(PieceType type, People owner, Color color, int tier) {
        this.type = type;
        this.owner = owner;
        this.color = color;
        this.id=nextid++;
        this.StartTier=tier;
    }


    public PieceType getType() {
        return type;
    }

    public People getOwner() {
        return owner;
    }

    public Color getColor(){
        return color;
    }

    @Override
    public String toString() {

        return "This " + type + " belongs to " + owner.toString() + "\n";
    }

    public MovementStrategy getMovementStrategy() {
        return movementStrategy;
    }

    public void setMovementStrategy(PieceType P,Map<PieceType, MovementStrategy> strategies){
        this.movementStrategy = strategies.get(P);
    }


    public boolean getHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean b){
        this.hasMoved=b;
    }

    public int getStartTier() {
        return StartTier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Piece)) return false;
        Piece p = (Piece) o;
        return id==p.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

