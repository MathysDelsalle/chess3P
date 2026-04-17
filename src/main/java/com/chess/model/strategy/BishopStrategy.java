package model.strategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import model.Board;
import model.Direction;
import model.Move;
import model.NextStep;
import model.Piece;
import model.Position;

public class BishopStrategy implements MovementStrategy {

    private static final Direction[] BISHOP_DIRECTIONS = {
        Direction.DIAG_UP_LEFT,
        Direction.DIAG_UP_RIGHT,
        Direction.DIAG_DOWN_LEFT,
        Direction.DIAG_DOWN_RIGHT,
        Direction.DIAG_CENTRE_LEFT,
        Direction.DIAG_CENTRE_RIGHT
    };

    @Override
    public List<Move> getPossibleMoves(Position from, Board board, Piece piece) {
        Set<Move> moves = new LinkedHashSet<>();
        for (Direction startDirection : BISHOP_DIRECTIONS) {
            Position first = board.getNeighborsDirection(from, startDirection);

            if (first != null) {
                exploreDirection(from,from,first,startDirection,board,piece,moves,new HashSet<>());
            }
        }

        return new ArrayList<>(moves);
    }

    public void exploreDirection(Position from, Position previous, Position current, Direction currentDirection, Board board, Piece piece, Set<Move> moves, Set<Position> visited) 
    {

        if (current == null || visited.contains(current)) {
            return;
        }

        visited.add(current);

        Piece target = board.getPiece(current);

        if (target == null) {
            moves.add(new Move(from, current, currentDirection));
        } else {
            if (!target.getOwner().equals(piece.getOwner())) {
                moves.add(new Move(from, current, currentDirection));
            }
            return;
        }

        List<NextStep> nextSteps = getNextSteps(previous, current, currentDirection, board);

        for (NextStep step : nextSteps) {exploreDirection(from, current, step.getPosition(), step.getDirection(), board, piece, moves, new HashSet<>(visited));
        }
    }

    public List<NextStep> getNextSteps(Position previous, Position current, Direction direction, Board board) 
    {
        List<NextStep> nextSteps = new ArrayList<>();

        Position next = board.getNeighborsDirection(current, direction);

        // Cas centre :
        // si current est un centre ET que next est aussi un centre,
        // on est dans un embranchement
        if (isCenter(current) && next != null && isCenter(next)) {
            List<NextStep> centerSteps = getCenterSteps(previous, current, direction, board);
            if (!centerSteps.isEmpty()) {
                return centerSteps;
            }
        }

        // Cas normal / jointure simple / centre simple
        if (next != null) {
            Direction nextDirection = direction;

            // Si on traverse deux jonctions à la suite,
            // on inverse la direction pour l'étape suivante
            if (current.getIsJunction() && next.getIsJunction()) {
                nextDirection = direction.switchDir();
            }

            nextSteps.add(new NextStep(next, nextDirection));
        }

        return nextSteps;
    }

    public boolean isCenter(Position position) {
        return position.getLigne() == 4 && (position.getColonne() == 4 || position.getColonne() == 5);
    }

    public List<NextStep> getCenterSteps(Position previous, Position current, Direction direction, Board board) 
    {
        List<NextStep> nextSteps = new ArrayList<>();

        int tier = current.getTiers();
        int line = current.getLigne();
        int col = current.getColonne();

        //Bloc pour colonne = 4
        // 1/1/1 -> 1/2/2 -> 1/3/3 -> 1/4/4
        // puis deux choix :
        // 2/4/4 via DIAG_CENTRE_LEFT
        // 3/4/4 via DIAG_CENTRE_RIGHT
        if (tier == 1 && line == 4 && col == 4) {
            if (previous.getTiers() == 1 && previous.getLigne() == 3 && previous.getColonne() == 3) {
                Position next1 = board.findPosition(board.getPositions(), 2, 4, 4);
                Position next2 = board.findPosition(board.getPositions(), 3, 4, 4);

                if (next1 != null) {
                    nextSteps.add(new NextStep(next1, Direction.DIAG_CENTRE_LEFT));
                }
                if (next2 != null) {
                    nextSteps.add(new NextStep(next2, Direction.DIAG_CENTRE_RIGHT));
                }

                return nextSteps;
            }
        }

        // 2/1/1 -> 2/2/2 -> 2/3/3 -> 2/4/4
        // puis deux choix :
        // 1/4/4 via DIAG_CENTRE_RIGHT
        // 3/4/4 via DIAG_CENTRE_LEFT
        else if (tier == 2 && line == 4 && col == 4) {
            if (previous.getTiers() == 2 && previous.getLigne() == 3 && previous.getColonne() == 3) {
                Position next1 = board.findPosition(board.getPositions(), 1, 4, 4);
                Position next2 = board.findPosition(board.getPositions(), 3, 4, 4);

                if (next1 != null) {
                    nextSteps.add(new NextStep(next1, Direction.DIAG_CENTRE_RIGHT));
                }
                if (next2 != null) {
                    nextSteps.add(new NextStep(next2, Direction.DIAG_CENTRE_LEFT));
                }

                return nextSteps;
            }
        }

        // 3/1/1 -> 3/2/2 -> 3/3/3 -> 3/4/4
        // puis deux choix :
        // 1/4/4 via DIAG_CENTRE_LEFT
        // 2/4/4 via DIAG_CENTRE_RIGHT
        else if (tier == 3 && line == 4 && col == 4) {
            if (previous.getTiers() == 3 && previous.getLigne() == 3 && previous.getColonne() == 3) {
                Position next1 = board.findPosition(board.getPositions(), 1, 4, 4);
                Position next2 = board.findPosition(board.getPositions(), 2, 4, 4);

                if (next1 != null) {
                    nextSteps.add(new NextStep(next1, Direction.DIAG_CENTRE_LEFT));
                }
                if (next2 != null) {
                    nextSteps.add(new NextStep(next2, Direction.DIAG_CENTRE_RIGHT));
                }

                return nextSteps;
            }
        }

        //Bloc pour colonne = 5 :
        // 1/1/8 -> 1/2/7 -> 1/3/6 -> 1/4/5
        // puis deux choix :
        // 2/4/5 via DIAG_CENTRE_LEFT
        // 3/4/5 via DIAG_CENTRE_RIGHT
        if (tier == 1 && line == 4 && col == 5) {
            if (previous.getTiers() == 1 && previous.getLigne() == 3 && previous.getColonne() == 6) {
                Position next1 = board.findPosition(board.getPositions(), 2, 4, 5);
                Position next2 = board.findPosition(board.getPositions(), 3, 4, 5);

                if (next1 != null) {
                    nextSteps.add(new NextStep(next1, Direction.DIAG_CENTRE_LEFT));
                }
                if (next2 != null) {
                    nextSteps.add(new NextStep(next2, Direction.DIAG_CENTRE_RIGHT));
                }

                return nextSteps;
            }
        }

        // 2/1/8 -> 2/2/7 -> 2/3/6 -> 2/4/5
        // puis deux choix :
        // 1/4/5 via DIAG_CENTRE_RIGHT
        // 3/4/5 via DIAG_CENTRE_LEFT
        else if (tier == 2 && line == 4 && col == 5) {
            if (previous.getTiers() == 2 && previous.getLigne() == 3 && previous.getColonne() == 6) {
                Position next1 = board.findPosition(board.getPositions(), 1, 4, 5);
                Position next2 = board.findPosition(board.getPositions(), 3, 4, 5);

                if (next1 != null) {
                    nextSteps.add(new NextStep(next1, Direction.DIAG_CENTRE_RIGHT));
                }
                if (next2 != null) {
                    nextSteps.add(new NextStep(next2, Direction.DIAG_CENTRE_LEFT));
                }

                return nextSteps;
            }
        }

        // 3/1/8 -> 3/2/7 -> 3/3/6 -> 3/4/5
        // puis deux choix :
        // 1/4/5 via DIAG_CENTRE_LEFT
        // 2/4/5 via DIAG_CENTRE_RIGHT
        else if (tier == 3 && line == 4 && col == 5) {
            if (previous.getTiers() == 3 && previous.getLigne() == 3 && previous.getColonne() == 6) {
                Position next1 = board.findPosition(board.getPositions(), 1, 4, 5);
                Position next2 = board.findPosition(board.getPositions(), 2, 4, 5);

                if (next1 != null) {
                    nextSteps.add(new NextStep(next1, Direction.DIAG_CENTRE_LEFT));
                }
                if (next2 != null) {
                    nextSteps.add(new NextStep(next2, Direction.DIAG_CENTRE_RIGHT));
                }

                return nextSteps;
            }
        }

        return nextSteps;
    }
}