import factories.BoardFactory;
import model.Board;
import model.Direction;
import model.Position;
import view.*;
//import model.*;

public class Main {
    public static void main(String[] args) {

        Board board = BoardFactory.createInitialBoard();
  

        ConsoleView consoleView = new ConsoleView();

        consoleView.diplayCreationReussie();

        Position p = board.findPosition(board.getPositions(),1, 3, 4);
        //System.out.println(board.getPiece(p));



        consoleView.displayPossibleMoves(p, board);
    }
}