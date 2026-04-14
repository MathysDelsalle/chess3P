package model.strategy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.Board;
import model.Direction;
import model.Move;
import model.Piece;
import model.Position;

public class KnightStrategy implements MovementStrategy {

    private static final Direction[] KNIGHT_DIRECTIONS = {
        Direction.UP,
        Direction.DOWN,
        Direction.LEFT,
        Direction.RIGHT
    };

    @Override
    public List<Move> getPossibleMoves(Position from, Board board, Piece piece) {
        Map<Position, Move> uniqueMoves = new LinkedHashMap<>();

        if (isCenter(from)) {
            addCenterKnightMoves(from, board, piece, uniqueMoves);
        } else {
            addStandardKnightMoves(from, board, piece, uniqueMoves);
        }

        return new ArrayList<>(uniqueMoves.values());
    }

    private void addStandardKnightMoves(Position from,
                                        Board board,
                                        Piece piece,
                                        Map<Position, Move> uniqueMoves) {
        for (Direction firstDir : KNIGHT_DIRECTIONS) {

            Position afterTwo = moveStepsKnight(board, from, firstDir, 2);
            if (afterTwo != null) {
                for (Direction turnDir : getPerpendicularDirections(firstDir)) {
                    Position destination = moveStepsKnight(board, afterTwo, turnDir, 1);
                    if (isValidKnightDestination(from, afterTwo, destination)) {
                        addMoveIfValid(uniqueMoves, from, destination, firstDir, board, piece);
                    }
                }
            }

            Position afterOne = moveStepsKnight(board, from, firstDir, 1);
            if (afterOne != null) {
                for (Direction turnDir : getPerpendicularDirections(firstDir)) {
                    Position destination = moveStepsKnight(board, afterOne, turnDir, 2);
                    if (isValidKnightDestination(from, afterOne, destination)) {
                        addMoveIfValid(uniqueMoves, from, destination, firstDir, board, piece);
                    }
                }
            }
        }
    }

    /**
     * Cas spécial du centre :
     * on ajoute uniquement les destinations réellement autorisées.
     */
    private void addCenterKnightMoves(Position from,
                                      Board board,
                                      Piece piece,
                                      Map<Position, Move> uniqueMoves) {
        int tiers = from.getTiers();
        int ligne = from.getLigne();
        int colonne = from.getColonne();

        // Centre gauche : colonne 4
        if (ligne == 4 && colonne == 4) {
            if (tiers == 1) {
                addCenterMove(board, piece, uniqueMoves, from, 1, 2, 5);
                addCenterMove(board, piece, uniqueMoves, from, 1, 2, 3);
                addCenterMove(board, piece, uniqueMoves, from, 1, 3, 2);
                addCenterMove(board, piece, uniqueMoves, from, 1, 3, 6);
                addCenterMove(board, piece, uniqueMoves, from, 2, 4, 7);
                addCenterMove(board, piece, uniqueMoves, from, 2, 3, 6);
                addCenterMove(board, piece, uniqueMoves, from, 2, 3, 4);
                addCenterMove(board, piece, uniqueMoves, from, 2, 4, 3);
                addCenterMove(board, piece, uniqueMoves, from, 3, 3, 4);
                addCenterMove(board, piece, uniqueMoves, from, 3, 4, 3);
            } else if (tiers == 2) {
                addCenterMove(board, piece, uniqueMoves, from, 2, 2, 5);
                addCenterMove(board, piece, uniqueMoves, from, 2, 2, 3);
                addCenterMove(board, piece, uniqueMoves, from, 2, 3, 2);
                addCenterMove(board, piece, uniqueMoves, from, 2, 3, 6);
                addCenterMove(board, piece, uniqueMoves, from, 3, 4, 7);
                addCenterMove(board, piece, uniqueMoves, from, 3, 3, 6);
                addCenterMove(board, piece, uniqueMoves, from, 3, 3, 4);
                addCenterMove(board, piece, uniqueMoves, from, 3, 4, 3);
                addCenterMove(board, piece, uniqueMoves, from, 1, 3, 4);
                addCenterMove(board, piece, uniqueMoves, from, 1, 4, 3);
            } else if (tiers == 3) {
                addCenterMove(board, piece, uniqueMoves, from, 3, 2, 5);
                addCenterMove(board, piece, uniqueMoves, from, 3, 2, 3);
                addCenterMove(board, piece, uniqueMoves, from, 3, 3, 2);
                addCenterMove(board, piece, uniqueMoves, from, 3, 3, 6);
                addCenterMove(board, piece, uniqueMoves, from, 1, 4, 7);
                addCenterMove(board, piece, uniqueMoves, from, 1, 3, 6);
                addCenterMove(board, piece, uniqueMoves, from, 1, 3, 4);
                addCenterMove(board, piece, uniqueMoves, from, 1, 4, 3);
                addCenterMove(board, piece, uniqueMoves, from, 2, 3, 4);
                addCenterMove(board, piece, uniqueMoves, from, 2, 4, 3);
            }
        }

        // Centre droit : colonne 5
        else if (ligne == 4 && colonne == 5) {
            if (tiers == 1) {
                addCenterMove(board, piece, uniqueMoves, from, 1, 3, 3);
                addCenterMove(board, piece, uniqueMoves, from, 1, 3, 7);
                addCenterMove(board, piece, uniqueMoves, from, 1, 2, 4);
                addCenterMove(board, piece, uniqueMoves, from, 1, 2, 6);
                addCenterMove(board, piece, uniqueMoves, from, 2, 4, 6);
                addCenterMove(board, piece, uniqueMoves, from, 2, 3, 5);
                addCenterMove(board, piece, uniqueMoves, from, 3, 3, 3);
                addCenterMove(board, piece, uniqueMoves, from, 3, 4, 2);
                addCenterMove(board, piece, uniqueMoves, from, 3, 3, 5);
                addCenterMove(board, piece, uniqueMoves, from, 3, 4, 6);
            } else if (tiers == 2) {
                addCenterMove(board, piece, uniqueMoves, from, 2, 3, 3);
                addCenterMove(board, piece, uniqueMoves, from, 2, 3, 7);
                addCenterMove(board, piece, uniqueMoves, from, 2, 2, 4);
                addCenterMove(board, piece, uniqueMoves, from, 2, 2, 6);
                addCenterMove(board, piece, uniqueMoves, from, 3, 4, 6);
                addCenterMove(board, piece, uniqueMoves, from, 3, 3, 5);
                addCenterMove(board, piece, uniqueMoves, from, 1, 3, 3);
                addCenterMove(board, piece, uniqueMoves, from, 1, 4, 2);
                addCenterMove(board, piece, uniqueMoves, from, 1, 3, 5);
                addCenterMove(board, piece, uniqueMoves, from, 1, 4, 6);
            } else if (tiers == 3) {
                addCenterMove(board, piece, uniqueMoves, from, 3, 3, 3);
                addCenterMove(board, piece, uniqueMoves, from, 3, 3, 7);
                addCenterMove(board, piece, uniqueMoves, from, 3, 2, 4);
                addCenterMove(board, piece, uniqueMoves, from, 3, 2, 6);
                addCenterMove(board, piece, uniqueMoves, from, 1, 4, 6);
                addCenterMove(board, piece, uniqueMoves, from, 1, 3, 5);
                addCenterMove(board, piece, uniqueMoves, from, 2, 3, 3);
                addCenterMove(board, piece, uniqueMoves, from, 2, 4, 2);
                addCenterMove(board, piece, uniqueMoves, from, 2, 3, 5);
                addCenterMove(board, piece, uniqueMoves, from, 2, 4, 6);
            }
        }
    }

    private void addCenterMove(Board board,
                               Piece piece,
                               Map<Position, Move> uniqueMoves,
                               Position from,
                               int tiers,
                               int ligne,
                               int colonne) {
        Position destination = board.findPosition(board.getPositions(), tiers, ligne, colonne);
        addMoveIfValid(uniqueMoves, from, destination, Direction.UP, board, piece);
    }

    private boolean isCenter(Position p) {
        return p.getLigne() == 4 && (p.getColonne() == 4 || p.getColonne() == 5);
    }

    private Position moveStepsKnight(Board board, Position start, Direction direction, int steps) {
        Position current = start;

        for (int i = 0; i < steps; i++) {
            Position next = board.getNeighborsDirection(current, direction);

            if (next == null) {
                return null;
            }

            current = next;
        }

        return current;
    }

    private boolean isValidKnightDestination(Position from,
                                             Position intermediate,
                                             Position destination) {
        if (destination == null) {
            return false;
        }

        if (destination.equals(from)) {
            return false;
        }

        if (intermediate != null && destination.equals(intermediate)) {
            return false;
        }

        return true;
    }

    private Direction[] getPerpendicularDirections(Direction direction) {
        switch (direction) {
            case UP:
            case DOWN:
                return new Direction[]{Direction.LEFT, Direction.RIGHT};
            case LEFT:
            case RIGHT:
                return new Direction[]{Direction.UP, Direction.DOWN};
            default:
                return new Direction[0];
        }
    }

    private void addMoveIfValid(Map<Position, Move> uniqueMoves,
                                Position from,
                                Position destination,
                                Direction moveDirection,
                                Board board,
                                Piece piece) {
        if (destination == null) {
            return;
        }

        Piece target = board.getPiece(destination);

        if (target == null || !target.getOwner().equals(piece.getOwner())) {
            uniqueMoves.putIfAbsent(destination, new Move(from, destination, moveDirection));
        }
    }
}