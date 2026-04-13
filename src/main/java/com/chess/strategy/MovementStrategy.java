package strategy;

import modele.Board;
import modele.Position;
import modele.Piece;
import modele.Move;

import java.util.List;

public interface MovementStrategy {
    List<Move> getPossibleMoves(Position from, Board board, Piece piece);
}