package com.chess.model.strategy;

import java.util.List;

import com.chess.model.Board;
import com.chess.model.Move;
import com.chess.model.Piece;
import com.chess.model.Position;
import com.chess.model.AttackInfo;


public interface MovementStrategy {

    List<Move> getPossibleMoves(Position from, Board board, Piece piece);
    AttackInfo getAttackedAndProtectedSquares(Position from, Board board, Piece piece);
}