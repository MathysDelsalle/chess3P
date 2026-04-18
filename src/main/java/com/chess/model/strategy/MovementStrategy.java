package model.strategy;

import java.util.List;

import model.Board;
import model.Move;
import model.Piece;
import model.Position;


public interface MovementStrategy {
    List<Move> getPossibleMoves(Position from, Board board, Piece piece);
    AttackInfo getAttackedAndProtectedSquares(Position from, Board board, Piece piece);
    public record AttackInfo(List<Position> attackedSquares, List<Position> protectedSquares){}
}