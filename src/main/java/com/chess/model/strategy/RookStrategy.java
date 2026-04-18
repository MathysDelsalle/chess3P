package model.strategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.*;


public class RookStrategy implements MovementStrategy {

//helper
public Position getNextRookPosition(Position from, Position to, Direction direction, Board board, Piece piece) {
    Position previous = board.getNeighborsDirection(to, direction);

    if (from.getTiers() == to.getTiers()) {
        previous = board.getNeighborsDirection(to, direction.switchDir());
    }
    if(from.getTiers()!=to.getTiers() && previous!=null && previous.getIsJunction() && to.getIsJunction()){
        return board.getNeighborsDirection(to, direction.switchDir());
    }
    else if(from.getTiers()!=to.getTiers() && !to.getIsJunction()){
        return board.getNeighborsDirection(to, direction.switchDir());
    }

    return board.getNeighborsDirection(to, direction);
}

@Override
public List<Move> getPossibleMoves(Position from, Board board, Piece piece) {
    List<Move> moves = new ArrayList<>();

    Direction[] ROOK_DIRECTIONS = {
        Direction.UP,
        Direction.DOWN,
        Direction.LEFT,
        Direction.RIGHT
    };

    Position suite = from;
    for (Direction direction : ROOK_DIRECTIONS) {
        Position to = board.getNeighborsDirection(suite,direction);
        Set<Position> visited = new HashSet<>();
        while (to != null && !visited.contains(to)) {
            Piece target = board.getPiece(to);
            visited.add(to);

            if (target == null) {
                moves.add(new Move(from, to, direction,piece));
            } else {
                if (!target.getOwner().equals(piece.getOwner())) {
                    moves.add(new Move(from, to, direction,piece));
                }
                break;
            }
            suite=to;
            to= getNextRookPosition(from,suite, direction, board, piece);
            
        }
        suite=from;
    }

    return moves;
}

@Override
public MovementStrategy.AttackInfo getAttackedAndProtectedSquares(Position from, Board board, Piece piece) {
        List<Position> attackedSquares = new ArrayList<>();
        List<Position> protectedSquares = new ArrayList<>();

        Direction[] ROOK_DIRECTIONS = {
        Direction.UP,
        Direction.DOWN,
        Direction.LEFT,
        Direction.RIGHT
    };

        for (Direction dir : ROOK_DIRECTIONS) {
            Position to = board.getNeighborsDirection(from, dir);
            boolean pieceRencontree = false;

            while (to != null) {
                Piece target = board.getPiece(to);

                if (!pieceRencontree) {
                    attackedSquares.add(to);
                } else {
                    protectedSquares.add(to);
                }

                if (target != null) {
                    pieceRencontree = true;
                }

                to = getNextRookPosition(from,to, dir, board, piece);
            }
        }

        return new MovementStrategy.AttackInfo(attackedSquares, protectedSquares);
    }

}