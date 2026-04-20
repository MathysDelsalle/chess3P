package com.chess.model.strategy;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.chess.model.Board;
import com.chess.model.Move;
import com.chess.model.Piece;
import com.chess.model.Position;
import com.chess.model.AttackInfo;

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
    public AttackInfo getAttackedAndProtectedSquares(Position from, Board board, Piece piece) {
        Set<Position> attackedSquares = new LinkedHashSet<>();
        Set<Position> protectedSquares = new LinkedHashSet<>();

        AttackInfo rookInfo = rookStrategy.getAttackedAndProtectedSquares(from, board, piece);
        AttackInfo bishopInfo = bishopStrategy.getAttackedAndProtectedSquares(from, board, piece);

        attackedSquares.addAll(rookInfo.getAttackedSquares());
        attackedSquares.addAll(bishopInfo.getAttackedSquares());

        protectedSquares.addAll(rookInfo.getProtectedSquares());
        protectedSquares.addAll(bishopInfo.getProtectedSquares());

        return new AttackInfo(new ArrayList<>(attackedSquares), new ArrayList<>(protectedSquares)
        );
    }
}