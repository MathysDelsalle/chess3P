package model.strategy;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import model.Board;
import model.Move;
import model.Piece;
import model.Position;

public class QueenStrategy implements MovementStrategy {

    private final RookStrategy rookStrategy = new RookStrategy();
    private final BishopStrategy bishopStrategy = new BishopStrategy();

    @Override
    public List<Move> getPossibleMoves(Position from, Board board, Piece piece) {
        Set<Move> moves = new LinkedHashSet<>();

        moves.addAll(rookStrategy.getPossibleMoves(from, board, piece));
        moves.addAll(bishopStrategy.getPossibleMoves(from, board, piece));

        return new ArrayList<>(moves);
    }

    @Override
    public MovementStrategy.AttackInfo getAttackedAndProtectedSquares(Position from, Board board, Piece piece) {
        Set<Position> attackedSquares = new LinkedHashSet<>();
        Set<Position> protectedSquares = new LinkedHashSet<>();

        MovementStrategy.AttackInfo rookInfo = rookStrategy.getAttackedAndProtectedSquares(from, board, piece);
        MovementStrategy.AttackInfo bishopInfo = bishopStrategy.getAttackedAndProtectedSquares(from, board, piece);

        attackedSquares.addAll(rookInfo.attackedSquares());
        attackedSquares.addAll(bishopInfo.attackedSquares());

        protectedSquares.addAll(rookInfo.protectedSquares());
        protectedSquares.addAll(bishopInfo.protectedSquares());

        return new MovementStrategy.AttackInfo(new ArrayList<>(attackedSquares), new ArrayList<>(protectedSquares)
        );
    }
}