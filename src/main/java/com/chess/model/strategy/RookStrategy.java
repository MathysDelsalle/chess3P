package model.strategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.*;


public class RookStrategy implements MovementStrategy {

@Override
public List<Move> getPossibleMoves(Position from, Board board, Piece piece) {
    List<Move> moves = new ArrayList<>();

    Direction[] ROOK_DIRECTIONS = {
        Direction.UP,
        Direction.DOWN,
        Direction.LEFT,
        Direction.RIGHT
    };

    for (Direction direction : ROOK_DIRECTIONS) {
        Position current = board.getNeighborsDirection(from, direction);
        Set<Position> visited = new HashSet<>();

        while (current != null && !visited.contains(current)) {
            Piece target = board.getPiece(current);
            visited.add(current);

            if (target == null) {
                moves.add(new Move(from, current, direction));
            } else {
                if (!target.getOwner().equals(piece.getOwner())) {
                    moves.add(new Move(from, current, direction));
                }
                break;
            }
            if(current.getIsJunction()){
                current = board.getNeighborsDirection(current, direction.switchDir());
            }
            else{
                current = board.getNeighborsDirection(current, direction);
            }
            
        }
    }

    return moves;
}
}