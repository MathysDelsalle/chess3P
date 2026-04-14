package model.strategy;

import java.util.List;

import model.Board;
import model.Move;
import model.Piece;
import model.Position;

public interface MovementStrategy {
    List<Move> getPossibleMoves(Position from, Board board, Piece piece);
}