package model;

import java.util.Map;

import model.strategy.MovementStrategy;

public class Piece {

    private PieceType type;
    private People owner;
    private Color color;
    private MovementStrategy movementStrategy;
    private boolean hasMoved=false;

    public Piece(PieceType type, People owner, Color color) {
        this.type = type;
        this.owner = owner;
        this.color = color;
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
}