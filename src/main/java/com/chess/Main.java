import game.BoardFactory;
import modele.*;

public class Main {
    public static void main(String[] args) {

        Board board = BoardFactory.createInitialBoard();

        System.out.println("Plateau créé sans soucis :)) .");

        Position p = board.getPosition(64);

        System.out.println(board.getPiece(p).getMovementStrategy().getPossibleMoves(p, board,board.getPiece(p)));
    }
}