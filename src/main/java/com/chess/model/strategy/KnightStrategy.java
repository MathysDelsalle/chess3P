package com.chess.model.strategy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.chess.model.Board;
import com.chess.model.Direction;
import com.chess.model.Move;
import com.chess.model.Piece;
import com.chess.model.Position;
import com.chess.model.AttackInfo;

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

    public void addStandardKnightMoves(Position from, Board board, Piece piece, Map<Position, Move> uniqueMoves) {
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
     * on ajoute uniquement les destinations réellement autorisées
     */
    public void addCenterKnightMoves(Position from, Board board, Piece piece, Map<Position, Move> uniqueMoves) {
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

    public void addCenterMove(Board board, Piece piece, Map<Position, Move> uniqueMoves,  Position from, int tiers, int ligne, int colonne) {
        Position destination = board.findPosition(board.getPositions(), tiers, ligne, colonne);
        addMoveIfValid(uniqueMoves, from, destination, Direction.UP, board, piece);
    }

    public boolean isCenter(Position p) {
        return p.getLigne() == 4 && (p.getColonne() == 4 || p.getColonne() == 5);
    }

    public Position moveStepsKnight(Board board, Position start, Direction direction, int steps) {
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

    public boolean isValidKnightDestination(Position from, Position intermediate, Position destination) {
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

    public Direction[] getPerpendicularDirections(Direction direction) {
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

    public void addMoveIfValid(Map<Position, Move> uniqueMoves, Position from, Position destination, Direction moveDirection, Board board, Piece piece) {
        if (destination == null) {
            return;
        }

        Piece target = board.getPiece(destination);

        if (target == null || !target.getOwner().equals(piece.getOwner())) {
            uniqueMoves.putIfAbsent(destination, new Move(from, destination, moveDirection,piece));
        }
    }

    public void addStandardKnightAttacks(Position from, Board board, List<Position> attackedSquares) {
    for (Direction firstDir : KNIGHT_DIRECTIONS) {

        Position afterTwo = moveStepsKnight(board, from, firstDir, 2);
        if (afterTwo != null) {
            for (Direction turnDir : getPerpendicularDirections(firstDir)) {
                Position destination = moveStepsKnight(board, afterTwo, turnDir, 1);
                addIfExists(attackedSquares, destination);
            }
        }

        Position afterOne = moveStepsKnight(board, from, firstDir, 1);
        if (afterOne != null) {
            for (Direction turnDir : getPerpendicularDirections(firstDir)) {
                Position destination = moveStepsKnight(board, afterOne, turnDir, 2);
                addIfExists(attackedSquares, destination);
            }
        }
    }
}

    public void addCenterKnightAttacks(Position from, Board board, List<Position> attackedSquares) {
        int tiers = from.getTiers();
        int ligne = from.getLigne();
        int colonne = from.getColonne();

        // on réutilise exactement la logique existante plus haut
        if (ligne == 4 && colonne == 4) {
            if (tiers == 1) {
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 1, 2, 5));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 1, 2, 3));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 1, 3, 2));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 1, 3, 6));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 2, 4, 7));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 2, 3, 6));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 2, 3, 4));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 2, 4, 3));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 3, 3, 4));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 3, 4, 3));
            } else if (tiers == 2) {
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 2, 2, 5));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 2, 2, 3));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 2, 3, 2));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 2, 3, 6));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 3, 4, 7));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 3, 3, 6));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 3, 3, 4));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 3, 4, 3));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 1, 3, 4));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 1, 4, 3));
            } else if (tiers == 3) {
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 3, 2, 5));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 3, 2, 3));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 3, 3, 2));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 3, 3, 6));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 1, 4, 7));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 1, 3, 6));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 1, 3, 4));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 1, 4, 3));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 2, 3, 4));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 2, 4, 3));
            }
        }

        else if (ligne == 4 && colonne == 5) {
            if (tiers == 1) {
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 1, 3, 3));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 1, 3, 7));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 1, 2, 4));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 1, 2, 6));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 2, 4, 6));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 2, 3, 5));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 3, 3, 3));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 3, 4, 2));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 3, 3, 5));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 3, 4, 6));
            } else if (tiers == 2) {
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 2, 3, 3));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 2, 3, 7));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 2, 2, 4));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 2, 2, 6));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 3, 4, 6));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 3, 3, 5));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 1, 3, 3));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 1, 4, 2));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 1, 3, 5));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 1, 4, 6));
            } else if (tiers == 3) {
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 3, 3, 3));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 3, 3, 7));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 3, 2, 4));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 3, 2, 6));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 1, 4, 6));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 1, 3, 5));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 2, 3, 3));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 2, 4, 2));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 2, 3, 5));
                addIfExists(attackedSquares, board.findPosition(board.getPositions(), 2, 4, 6));
            }
        }
    }

    public void addIfExists(List<Position> list, Position p) {
        if (p != null && !list.contains(p)) {
            list.add(p);
        }
    }

    @Override
    public AttackInfo getAttackedAndProtectedSquares(Position from, Board board, Piece piece) {

        List<Position> attackedSquares = new ArrayList<>();
        List<Position> protectedSquares = new ArrayList<>();

        // On reprend les moves du knight
        List<Move> moves = getPossibleMoves(from, board, piece);

        // Les cases atteignables = cases attaquées
        for (Move m : moves) {
            attackedSquares.add(m.getTo());
        }

        // ajout des cases ou il y a des alliés
        if (isCenter(from)) {
            addCenterKnightAttacks(from, board, attackedSquares);
        } else {
            addStandardKnightAttacks(from, board, attackedSquares);
        }

        return new AttackInfo(attackedSquares, protectedSquares);
    }
}