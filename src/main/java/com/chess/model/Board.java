package com.chess.model;
import java.util.*;

import com.chess.model.strategy.MovementStrategy;

public class Board {

    //map des position des pieces puis des voisin et des diags pour les cases
    private Map<Position, Piece> pieces = new HashMap<>();
    private Map<Position, Map<Direction,Position>> neighbors = new HashMap<>();
    private Map<Integer, Position> positions = new HashMap<>();
    private Map<Position, Set<Piece>> underAttack = new HashMap<>();
    private Map<Position, Set<Piece>> protectedRightNow = new HashMap<>();  

    //ajoute les cases dans neighbor et diag afin de dire ensuite les connexions entre elles
    public void addPosition(Position position) {
        neighbors.putIfAbsent(position, new HashMap<>());
    }

    //connecte les case qui sont voisines par les coté
    public void connectSide(Position from, Position to, Direction direction) {
        neighbors.get(from).put(direction,to);
        direction=direction.switchDir();
        neighbors.get(to).put(direction,from);
}

    public void connectOneWay(Position from, Position to, Direction direction) {
        neighbors.get(from).put(direction, to);
}


    public Map<Direction, Position> getNeighbors(Position position) {
        return neighbors.getOrDefault(position, new HashMap<>());
    }

    public Position getNeighborsDirection(Position position, Direction dir){
        return neighbors.getOrDefault(position, new HashMap<>()).get(dir);
    }

    public Piece getPiece(Position position) {

        return pieces.get(position);
    }

    public void setPiece(Position position, Piece piece) {

        pieces.put(position, piece);
    }

    public Map<Integer, Position> getPositions() {
        return positions;
    }

    public Position getPosition(int id) {
    return positions.get(id);
    }

    public Map<Position, Set<Piece>> getProtectedRightNow() {
        return protectedRightNow;
    }

    public Map<Position, Set<Piece>> getUnderAttack() {
        return underAttack;
    }

    public Position findPosition(Map<Integer, Position> positions, int tiers, int ligne, int colonne) {
    if (tiers < 1 || tiers > 3 || ligne < 1 || ligne > 4 || colonne < 1 || colonne > 8) {
        return null;
    }

    int id = (tiers - 1) * 32 + (ligne - 1) * 8 + (colonne - 1);
    return positions.get(id);
    }

    public void recomputeAttackMaps(){
        underAttack.clear();
        protectedRightNow.clear();

        for(Position pos : positions.values()){
            Piece p = getPiece(pos);
            if(p==null){
                continue;
            }
            MovementStrategy strategy = p.getMovementStrategy();
            AttackInfo info = strategy.getAttackedAndProtectedSquares(pos, this, p);

            for (Position position : info.getAttackedSquares()) {
                underAttack.computeIfAbsent(position, k -> new HashSet<>()).add(p);
            }

            for (Position position : info.getProtectedSquares()) {
                protectedRightNow.computeIfAbsent(position, k -> new HashSet<>()).add(p);
            }
        }

    }

    public boolean isSquareUnderAttack(Position pos, People attacker) {
        Set<Piece> attackers = underAttack.get(pos);

        if (attackers == null) {
            return false;
        }

        for (Piece p : attackers) {
            if (p.getOwner().equals(attacker)) {
                return true;
            }
        }

        return false;
    }

}



