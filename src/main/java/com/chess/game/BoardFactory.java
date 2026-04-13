package game;

import modele.*;

import java.util.HashMap;
import java.util.Map;

public class BoardFactory {

    
    
    //méthode pour tout créer
    public static Board createInitialBoard(){
        Board board = new Board();
        Player p = new Player("Joueur 1");
        Bot b1 = new Bot(1);
        Bot b2 = new Bot(2);

        Map<Integer, Position> positions = createPositions(board);

        connectPositions(board, positions);
        connectDiag(board, positions);
        placeInitialPieces(board, positions, p, b1, b2);

        return board;
    }


    //methode pour créer les cases
    public static Map<Integer, Position> createPositions(Board board){
        Map<Integer, Position> positions = new HashMap<>();

         for (int i = 1; i <= 96; i++) {
            Position p = new Position(i);
            positions.put(i, p);
            board.addPosition(p);
        }

        return positions;
    }

    //methodes pour connecter les cases par lignes horizontales
    public static void connectHorizontalLine(Board board, Map<Integer, Position> positions, int start, int end) {
        for (int i = start; i < end; i++) {
            board.connectSide(positions.get(i), positions.get(i + 1),Direction.LEFT);
        }
    }

    //methodes pour connecter les cases par lignes verticales
    public static void connectVerticalLine(Board board, Map<Integer, Position> positions, int start, int end) {
        for (int i = start; i < end; i += 8) {
            board.connectSide(positions.get(i), positions.get(i + 8),Direction.DOWN);
        }
    }

    /*connecte les cases vosines entre elles en appelant les méthodes de connexion de ligne*/
    public static void connectPositions(Board board, Map<Integer, Position> positions) {

    // lignes horizontales
    for (int i = 1; i <= 89; i += 8) {
        connectHorizontalLine(board, positions, i, i + 7);
    }

    // lignes verticales bleues
    for (int i = 1; i <= 4; i++) {
        connectVerticalLine(board, positions, i, i + 56);
    }

    // lignes verticales jaunes
    int idligne = 60;
    int idligne2 = 92;

    for (int i = 0; i < 4; i++) {
        idligne += 1;
        idligne2 += 1;

        board.connectSide(positions.get(idligne), positions.get(idligne - 8),Direction.DOWN);
        board.connectSide(positions.get(idligne - 8), positions.get(idligne - 16),Direction.DOWN);
        board.connectSide(positions.get(idligne - 16), positions.get(idligne - 24),Direction.DOWN);

        board.connectSide(positions.get(idligne - 24), positions.get(idligne2),Direction.DOWN);

        board.connectSide(positions.get(idligne2), positions.get(idligne2 - 8),Direction.DOWN);
        board.connectSide(positions.get(idligne2 - 8), positions.get(idligne2 - 16),Direction.DOWN);
        board.connectSide(positions.get(idligne2 - 16), positions.get(idligne2 - 24),Direction.DOWN);
    }

    // lignes verticales rouges
    idligne = 4;
    idligne2 = 93;

    for (int i = 0; i <4; i++) {
        idligne += 1;
        idligne2 -= 1;

        board.connectSide(positions.get(idligne), positions.get(idligne + 8),Direction.DOWN);
        board.connectSide(positions.get(idligne + 8), positions.get(idligne + 16),Direction.DOWN);
        board.connectSide(positions.get(idligne + 16), positions.get(idligne + 24),Direction.DOWN);

        board.connectSide(positions.get(idligne + 24), positions.get(idligne2),Direction.DOWN);

        board.connectSide(positions.get(idligne2), positions.get(idligne2 - 8),Direction.DOWN);
        board.connectSide(positions.get(idligne2 - 8), positions.get(idligne2 - 16),Direction.DOWN);
        board.connectSide(positions.get(idligne2 - 16), positions.get(idligne2 - 24),Direction.DOWN);
    }


    /*DEBUG
    for(int k=0;k<96;k++){
        int temp =k+1;
        System.out.println("Voisins de "+ temp + " : " + board.getNeighbors(positions.get(temp)));
    }*/
    
    }

    //méthode pour connecter les cases par les diagonales (elle leur attribue une position mais aussi une direction)
    public static void connectDiagCustom(Board board, Map<Integer, Position> positions, int[] ids, Direction dir) {
        for(int i=0;i<ids.length-1;i++){
        board.connectDiag(positions.get(ids[i]), positions.get(ids[i+1]),dir);
        }
    }


    public static void connectDiag(Board board, Map<Integer, Position> positions){

        //tableau de tableau d'entier qui définit toutes les diagonales (a améliorer même si je n'ai pas trouvé mieux pour le moment).
        int[][] diagonals = {
        {1, 10, 19, 28},
        {2, 9},
        {2,11,20,29},
        {3, 10, 17},
        {3,12,21,30,90,81},
        {4,11,18,25},
        {4,13,22,31,89},
        {5,12,19,26,33},
        {5,14,23,32},
        {6,13,20,27,34,41},
        {6,15,24},
        {7,16},
        {7,14,21,28},
        {8,15,22,29},
        {9,18,27,36},
        {17,26,35,44,53,62},
        {16,23,30,92},
        {24,31,91,84,77,70},
        {25,34,43,52,61},
        {32,90,83,76,69},
        {33,42,51,60},
        {41,50,59},
        {49,42,35,28},
        {49,58},
        {57,50,43,36},
        {58,51,44,37},
        {59,52,45,38,95,88},
        {60,53,46,39,96},
        {61,54,47,40},
        {62,55,48},
        {63,56},
        {63,54,45,36},
        {64,55,46,37},
        {56,47,38,93},
        {48,39,94,85,76,67},
        {40,95,86,77,68},
        {96,87,78,69},
        {88,79,70},
        {80,71},
        {80,87,94,37},
        {72,79,86,93},
        {71,78,85,92},
        {68,75,82,89},
        {67,74,81},
        {66,73},
        {66,75,84,93},
        {65,74,83,92},
        {73,82,91,29},
        {28,92},
        {92,37},
        {37,28},
        {36,29},
        {29,93},
        {93,36}
        
        };

        //directions pour chaque diagonales je sais que c'est moche mais pour le moment j'ai pas mieux.
        Direction[] directions = {
            
            
            Direction.DIAG_DOWN_RIGHT,
            Direction.DIAG_DOWN_LEFT,
            Direction.DIAG_DOWN_RIGHT,
            Direction.DIAG_DOWN_LEFT,
            Direction.DIAG_DOWN_RIGHT,
            Direction.DIAG_DOWN_LEFT,
            Direction.DIAG_DOWN_RIGHT,
            Direction.DIAG_DOWN_LEFT,
            Direction.DIAG_DOWN_RIGHT,
            Direction.DIAG_DOWN_LEFT,
            Direction.DIAG_DOWN_RIGHT,
            Direction.DIAG_DOWN_RIGHT,
            Direction.DIAG_DOWN_LEFT,
            Direction.DIAG_DOWN_LEFT,
            Direction.DIAG_DOWN_RIGHT,
            Direction.DIAG_DOWN_RIGHT,
            Direction.DIAG_DOWN_LEFT,
            Direction.DIAG_DOWN_LEFT,
            Direction.DIAG_DOWN_RIGHT,
            Direction.DIAG_DOWN_LEFT,
            Direction.DIAG_DOWN_RIGHT,
            Direction.DIAG_DOWN_RIGHT,
            Direction.DIAG_UP_RIGHT,
            Direction.DIAG_DOWN_RIGHT,
            Direction.DIAG_UP_RIGHT,
            Direction.DIAG_UP_RIGHT,
            Direction.DIAG_UP_RIGHT,
            Direction.DIAG_UP_RIGHT,
            Direction.DIAG_UP_RIGHT,
            Direction.DIAG_UP_RIGHT,
            Direction.DIAG_UP_RIGHT,
            Direction.DIAG_UP_LEFT,
            Direction.DIAG_UP_RIGHT,
            Direction.DIAG_UP_RIGHT,
            Direction.DIAG_UP_RIGHT,
            Direction.DIAG_UP_RIGHT,
            Direction.DIAG_UP_RIGHT,
            Direction.DIAG_UP_RIGHT,
            Direction.DIAG_UP_RIGHT,
            Direction.DIAG_UP_LEFT,
            Direction.DIAG_UP_LEFT,
            Direction.DIAG_UP_LEFT,
            Direction.DIAG_UP_LEFT,
            Direction.DIAG_UP_LEFT,
            Direction.DIAG_UP_LEFT,
            Direction.DIAG_DOWN_LEFT,
            Direction.DIAG_DOWN_LEFT,
            Direction.DIAG_DOWN_LEFT,
            Direction.DIAG_CENTRE_LEFT,
            Direction.DIAG_CENTRE_LEFT,
            Direction.DIAG_CENTRE_LEFT,
            Direction.DIAG_CENTRE_LEFT,
            Direction.DIAG_CENTRE_LEFT,
            Direction.DIAG_CENTRE_LEFT
            
            
        };

        //boucle qui connecte toute les diags
        for (int j = 0; j < diagonals.length; j++) {
            connectDiagCustom(board, positions, diagonals[j], directions[j]);
        }
        /*DEBUG
        for(int k=0;k<96;k++){
            int temp =k+1;
            System.out.println("Diagonales de "+ temp + " : " + board.getDiags(positions.get(temp)));
        }*/    
    }

    //crée et place les pieces pour chaque joueur a leur emplacement au début de la partie
    public static void placeInitialPieces(Board board, Map<Integer, Position> positions, Player p, Bot b1, Bot b2){
        
        //blue player ( bot1 )
        board.setPiece(positions.get(1), PieceFactory.createPiece(PieceType.Rook, b1, Color.blue,PieceFactory.getStrategies()));
        board.setPiece(positions.get(9), PieceFactory.createPiece(PieceType.Knight, b1, Color.blue,PieceFactory.getStrategies()));
        board.setPiece(positions.get(17), PieceFactory.createPiece(PieceType.Bishop, b1, Color.blue,PieceFactory.getStrategies()));
        board.setPiece(positions.get(25), PieceFactory.createPiece(PieceType.Queen, b1, Color.blue,PieceFactory.getStrategies()));
        board.setPiece(positions.get(33), PieceFactory.createPiece(PieceType.King, b1, Color.blue,PieceFactory.getStrategies()));
        board.setPiece(positions.get(41), PieceFactory.createPiece(PieceType.Bishop, b1, Color.blue,PieceFactory.getStrategies()));
        board.setPiece(positions.get(49), PieceFactory.createPiece(PieceType.Knight, b1, Color.blue,PieceFactory.getStrategies()));
        board.setPiece(positions.get(57), PieceFactory.createPiece(PieceType.Rook, b1, Color.blue,PieceFactory.getStrategies()));
        for(int i=2;i<=58;i+=8){
        board.setPiece(positions.get(i), PieceFactory.createPiece(PieceType.Pawn, b1, Color.blue,PieceFactory.getStrategies()));
        }

        //red player ( bot2 )
        board.setPiece(positions.get(8), PieceFactory.createPiece(PieceType.Rook, b2, Color.red,PieceFactory.getStrategies()));
        board.setPiece(positions.get(16), PieceFactory.createPiece(PieceType.Knight, b2, Color.red,PieceFactory.getStrategies()));
        board.setPiece(positions.get(24), PieceFactory.createPiece(PieceType.Bishop, b2, Color.red,PieceFactory.getStrategies()));
        board.setPiece(positions.get(89), PieceFactory.createPiece(PieceType.Queen, b2, Color.red,PieceFactory.getStrategies()));
        board.setPiece(positions.get(32), PieceFactory.createPiece(PieceType.King, b2, Color.red,PieceFactory.getStrategies()));
        board.setPiece(positions.get(81), PieceFactory.createPiece(PieceType.Bishop, b2, Color.red,PieceFactory.getStrategies()));
        board.setPiece(positions.get(73), PieceFactory.createPiece(PieceType.Knight, b2, Color.red,PieceFactory.getStrategies()));
        board.setPiece(positions.get(65), PieceFactory.createPiece(PieceType.Rook, b2, Color.red,PieceFactory.getStrategies()));
        for(int i=7;i<=31;i+=8){
        board.setPiece(positions.get(i), PieceFactory.createPiece(PieceType.Pawn, b2, Color.red,PieceFactory.getStrategies()));
        }
        for(int i=66;i<=90;i+=8){
        board.setPiece(positions.get(i), PieceFactory.createPiece(PieceType.Pawn, b2, Color.red,PieceFactory.getStrategies()));
        }

        //white player ( player )
        board.setPiece(positions.get(64), PieceFactory.createPiece(PieceType.Rook, p, Color.white,PieceFactory.getStrategies()));
        board.setPiece(positions.get(56), PieceFactory.createPiece(PieceType.Knight, p, Color.white,PieceFactory.getStrategies()));
        board.setPiece(positions.get(48), PieceFactory.createPiece(PieceType.Bishop, p, Color.white,PieceFactory.getStrategies()));
        board.setPiece(positions.get(40), PieceFactory.createPiece(PieceType.Queen, p, Color.white,PieceFactory.getStrategies()));
        board.setPiece(positions.get(96), PieceFactory.createPiece(PieceType.King, p, Color.white,PieceFactory.getStrategies()));
        board.setPiece(positions.get(88), PieceFactory.createPiece(PieceType.Bishop, p, Color.white,PieceFactory.getStrategies()));
        board.setPiece(positions.get(80), PieceFactory.createPiece(PieceType.Knight, p, Color.white,PieceFactory.getStrategies()));
        board.setPiece(positions.get(72), PieceFactory.createPiece(PieceType.Rook, p, Color.white,PieceFactory.getStrategies()));
        for(int i=71;i<=95;i+=8){
        board.setPiece(positions.get(i), PieceFactory.createPiece(PieceType.Pawn, p, Color.white,PieceFactory.getStrategies()));
        }
        for(int i=39;i<=63;i+=8){
        board.setPiece(positions.get(i), PieceFactory.createPiece(PieceType.Pawn, p, Color.white,PieceFactory.getStrategies()));
        }

        /*DEBUG
        for(int i=1;i<=96;i++){
            if(board.getPiece(positions.get(i))!=null){
                System.out.println(board.getPiece(positions.get(i)));
                }
            
            
        }*/

    }
}
