import factories.BoardFactory;
import model.Board;
import model.Position;
import view.*;
//import model.*;

public class Main {
    public static void main(String[] args) {

        Board board = BoardFactory.createInitialBoard();
  

        ConsoleView consoleView = new ConsoleView();

        consoleView.diplayCreationReussie();

        Position p = board.findPosition(board.getPositions(),1, 4, 4);

        consoleView.displayPossibleMoves(p, board);
    }
}