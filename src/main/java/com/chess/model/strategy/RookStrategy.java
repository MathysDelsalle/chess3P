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

//chercher comment aller de 2/4/8 vers 2/4/7
    Position suite = from;
    for (Direction direction : ROOK_DIRECTIONS) {
        Position to = board.getNeighborsDirection(suite,direction);
        Set<Position> visited = new HashSet<>();
        while (to != null && !visited.contains(to)) {
            Piece target = board.getPiece(to);
            visited.add(to);

            if (target == null) {
                moves.add(new Move(from, to, direction));
            } else {
                if (!target.getOwner().equals(piece.getOwner())) {
                    moves.add(new Move(from, to, direction));
                }
                break;
            }
            Position previous = board.getNeighborsDirection(suite, direction);
            if(piece.getStartTier()==suite.getTiers()){
                previous = board.getNeighborsDirection(suite, direction.switchDir());
            }

            if(suite.getIsJunction() && previous!=null && previous.getIsJunction() && suite.getTiers()!=previous.getTiers()){
                suite=to;
                to = board.getNeighborsDirection(to, direction.switchDir());
            }
            else{
                suite=to;
                to = board.getNeighborsDirection(to, direction);
            }
            
        }
        suite=from;
    }

    return moves;
}
}