package view;

import model.Board;
import model.Position;

public class ConsoleView {
    public void diplayCreationReussie(){
        System.out.println("Plateau créé sans soucis :)) .");
    }

    public void displayPossibleMoves(Position p, Board board){
        System.out.println(board.getPiece(p).getMovementStrategy().getPossibleMoves(p, board,board.getPiece(p)));
    }
}
