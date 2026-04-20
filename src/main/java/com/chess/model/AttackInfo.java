package com.chess.model;

import java.util.List;

public class AttackInfo{

    private List<Position> attackedSquares;
    private List<Position> protectedSquares;

    public AttackInfo(List<Position> attackedSquares, List<Position> protectedSquares){
        this.attackedSquares=attackedSquares;
        this.protectedSquares=protectedSquares;
    }

    public List<Position> getAttackedSquares() {
        return attackedSquares;
    }

    public List<Position> getProtectedSquares() {
        return protectedSquares;
    }
}