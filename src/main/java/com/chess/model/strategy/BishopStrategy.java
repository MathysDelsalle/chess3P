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

        if (isCenter(from)) {
            List<NextStep> startSteps = getStartStepsFromCenter(from, board);

            for (NextStep step : startSteps) {
                exploreDirection(from, from, step.getPosition(), step.getDirection(), board, piece, moves, new HashSet<>());
            }
        } else {
            for (Direction startDirection : BISHOP_DIRECTIONS) {
                Position first = board.getNeighborsDirection(from, startDirection);

                if (first != null) {
                    exploreDirection(from, from, first, startDirection, board, piece, moves, new HashSet<>());
                }
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
            moves.add(new Move(from, current, currentDirection,piece));
        } else {
            if (!target.getOwner().equals(piece.getOwner())) {
                moves.add(new Move(from, current, currentDirection,piece));
            }
            return;
        }

        List<NextStep> nextSteps = getNextSteps(previous, current, currentDirection, board);

        for (NextStep step : nextSteps) {exploreDirection(from, current, step.getPosition(), step.getDirection(), board, piece, moves, new HashSet<>(visited));
        }
    }

   public List<NextStep> getNextSteps(Position previous, Position current, Direction direction, Board board) {
        List<NextStep> nextSteps = new ArrayList<>();

        // Cas centre : si on est sur une case centre,
        // on teste d'abord les embranchements spéciaux
        if (isCenter(current)) {
            List<NextStep> centerSteps = getCenterSteps(previous, current, direction, board);
            if (!centerSteps.isEmpty()) {
                return centerSteps;
            }
            
        }

        // Cas normal
        Position next = board.getNeighborsDirection(current, direction);

        if (next != null) {
            Direction nextDirection = direction;
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
                    nextSteps.add(new NextStep(next1, Direction.DIAG_DOWN_LEFT));
                }
                if (next2 != null) {
                    nextSteps.add(new NextStep(next2, Direction.DIAG_DOWN_LEFT));
                }

                return nextSteps;
            }else{
                Position next1 = board.findPosition(board.getPositions(), 2, 4, 4);
                Position next2 = board.findPosition(board.getPositions(), 3, 4, 4);
                Position next3 = board.findPosition(board.getPositions(), 2, 4, 6);
                Position next4 = board.findPosition(board.getPositions(), 1, 3, 3);
                Position next5 = board.findPosition(board.getPositions(), 1, 3, 5);

                if (next1 != null) {
                    nextSteps.add(new NextStep(next1, Direction.DIAG_DOWN_LEFT));
                }
                if (next2 != null) {
                    nextSteps.add(new NextStep(next2, Direction.DIAG_DOWN_LEFT));
                }
                if (next3 != null) {
                    nextSteps.add(new NextStep(next1, Direction.DIAG_DOWN_RIGHT));
                }
                if (next4 != null) {
                    nextSteps.add(new NextStep(next2, Direction.DIAG_DOWN_LEFT));
                }
                if (next5 != null) {
                    nextSteps.add(new NextStep(next1, Direction.DIAG_DOWN_RIGHT));
                }
                

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
                    nextSteps.add(new NextStep(next1, Direction.DIAG_DOWN_LEFT));
                }
                if (next2 != null) {
                    nextSteps.add(new NextStep(next2, Direction.DIAG_DOWN_LEFT));
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
                    nextSteps.add(new NextStep(next1, Direction.DIAG_DOWN_LEFT));
                }
                if (next2 != null) {
                    nextSteps.add(new NextStep(next2, Direction.DIAG_DOWN_LEFT));
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
                    nextSteps.add(new NextStep(next1, Direction.DIAG_DOWN_RIGHT));
                }
                if (next2 != null) {
                    nextSteps.add(new NextStep(next2, Direction.DIAG_DOWN_RIGHT));
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
                    nextSteps.add(new NextStep(next1, Direction.DIAG_DOWN_RIGHT));
                }
                if (next2 != null) {
                    nextSteps.add(new NextStep(next2, Direction.DIAG_DOWN_RIGHT));
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
                    nextSteps.add(new NextStep(next1, Direction.DIAG_DOWN_RIGHT));
                }
                if (next2 != null) {
                    nextSteps.add(new NextStep(next2, Direction.DIAG_DOWN_RIGHT));
                }

                return nextSteps;
            }
        }

        return nextSteps;
    }

    public void addStepIfExists(List<NextStep> steps, Position p, Direction d) {
    if (p != null) {
        steps.add(new NextStep(p, d));
    }
}

    public List<NextStep> getStartStepsFromCenter(Position from, Board board) {
        List<NextStep> steps = new ArrayList<>();

        int tier = from.getTiers();
        int ligne = from.getLigne();
        int colonne = from.getColonne();

        if (tier==1 && ligne==4) {
            if(colonne==4){
                //246
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier+1, ligne, colonne+2),Direction.DIAG_DOWN_RIGHT);
                //133
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier, ligne-1, colonne-1),Direction.DIAG_DOWN_LEFT);
                //135
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier, ligne-1, colonne+1),Direction.DIAG_DOWN_RIGHT);
                //244
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier+1, ligne, colonne),Direction.DIAG_DOWN_LEFT);
                //344
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier+2, ligne, colonne),Direction.DIAG_DOWN_LEFT);
            }else if(colonne==5) {
                //343
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier+2, ligne, colonne-2),Direction.DIAG_DOWN_LEFT);
                //134
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier, ligne-1, colonne-1),Direction.DIAG_DOWN_LEFT);
                //136
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier, ligne-1, colonne+1),Direction.DIAG_DOWN_RIGHT);
                //255
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier+1, ligne, colonne),Direction.DIAG_DOWN_RIGHT);
                //355
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier+2, ligne, colonne),Direction.DIAG_DOWN_RIGHT);
            }
            
        }else if(tier==2 && ligne==4){
             if(colonne==4){
                //346
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier+1, ligne, colonne+2),Direction.DIAG_DOWN_RIGHT);
                //233
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier, ligne-1, colonne-1),Direction.DIAG_DOWN_LEFT);
                //235
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier, ligne-1, colonne+1),Direction.DIAG_DOWN_RIGHT);
                //344
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier+1, ligne, colonne),Direction.DIAG_DOWN_LEFT);
                //144
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier-1, ligne, colonne),Direction.DIAG_DOWN_LEFT);
            }else if(colonne==5) {
                //143
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier-1, ligne, colonne-2),Direction.DIAG_DOWN_LEFT);
                //234
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier, ligne-1, colonne-1),Direction.DIAG_DOWN_LEFT);
                //236
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier, ligne-1, colonne+1),Direction.DIAG_DOWN_RIGHT);
                //355
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier+1, ligne, colonne),Direction.DIAG_DOWN_RIGHT);
                //155
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier-1, ligne, colonne),Direction.DIAG_DOWN_RIGHT);
            }
            
        }else if(tier==3 && ligne==4){
            if(colonne==4){
                //146
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier-2, ligne, colonne+2),Direction.DIAG_DOWN_RIGHT);
                //333
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier, ligne-1, colonne-1),Direction.DIAG_DOWN_LEFT);
                //335
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier, ligne-1, colonne+1),Direction.DIAG_DOWN_RIGHT);
                //144
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier-2, ligne, colonne),Direction.DIAG_DOWN_LEFT);
                //244
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier+1, ligne, colonne),Direction.DIAG_DOWN_LEFT);
            }else if(colonne==5) {
                //243
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier-1, ligne, colonne-2),Direction.DIAG_DOWN_LEFT);
                //334
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier, ligne-1, colonne-1),Direction.DIAG_DOWN_LEFT);
                //336
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier, ligne-1, colonne+1),Direction.DIAG_DOWN_RIGHT);
                //155
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier-2, ligne, colonne),Direction.DIAG_DOWN_RIGHT);
                //255
                addStepIfExists(steps,board.findPosition(board.getPositions(), tier-1, ligne, colonne),Direction.DIAG_DOWN_RIGHT);
            }
        }

        return steps;
    }

    // --------- PARTIE ATTAQUES / CASES LATENTES ---------

    @Override
    public MovementStrategy.AttackInfo getAttackedAndProtectedSquares(Position from, Board board, Piece piece) {
        Set<Position> attackedSquares = new HashSet<>();
        Set<Position> protectedSquares = new HashSet<>();

        if (isCenter(from)) {
            List<NextStep> startSteps = getStartStepsFromCenter(from, board);

            for (NextStep step : startSteps) {
                exploreAttackDirection(from, from, step.getPosition(), step.getDirection(), board, 
                    attackedSquares, protectedSquares, new HashSet<>(), false);
            }
        } else {
            for (Direction startDirection : BISHOP_DIRECTIONS) {
                Position first = board.getNeighborsDirection(from, startDirection);

                if (first != null) {
                    exploreAttackDirection(from, from, first, startDirection, board, 
                        attackedSquares, protectedSquares, new HashSet<>(), false);
                }
            }
        }

        return new MovementStrategy.AttackInfo(new ArrayList<>(attackedSquares), new ArrayList<>(protectedSquares));
    }

    public void exploreAttackDirection(Position from, Position previous, Position current, Direction currentDirection, Board board, 
        Set<Position> attackedSquares, Set<Position> protectedSquares, Set<Position> visited, boolean pieceEncountered) {

        if (current == null || visited.contains(current)) {
            return;
        }

        visited.add(current);

        if (!pieceEncountered) {
            attackedSquares.add(current);
        } else {
            protectedSquares.add(current);
        }

        Piece target = board.getPiece(current);
        boolean nextPieceEncountered = pieceEncountered;

        if (target != null) {
            if (pieceEncountered) {
                return; // on s'arrête au 2e bloqueur
            }
            nextPieceEncountered = true;
        }

        List<NextStep> nextSteps = getNextSteps(previous, current, currentDirection, board);

        for (NextStep step : nextSteps) {
            exploreAttackDirection(from, current, step.getPosition(), step.getDirection(), board, attackedSquares, protectedSquares, 
            new HashSet<>(visited), nextPieceEncountered);
        }
    }

}