package strategy;

import java.util.ArrayList;
import java.util.List;

import modele.*;


public class RookStrategy implements MovementStrategy {

    private static final Direction[] ROOK_DIRECTIONS = {
            Direction.UP,
            Direction.DOWN,
            Direction.LEFT,
            Direction.RIGHT
    };

    @Override
    public List<Move> getPossibleMoves(Position from, Board board, Piece piece) {
        List<Move> moves = new ArrayList<>();

        for (Direction direction : ROOK_DIRECTIONS) {
            Position current = board.getNeighborsDirection(from, direction);
            int distance = 1;

            while (current != null) {
                Piece target = board.getPiece(current);

                if (target == null) {
                    moves.add(new Move(from, current, direction, distance));
                } else {
                    if (!target.getOwner().equals(piece.getOwner())) {
                        moves.add(new Move(from, current, direction, distance));
                    }
                    break;
                }

                current = board.getNeighborsDirection(current, direction);
                distance++;
            }
        }

        return moves;
    }
}