package factories;

import java.util.Map;

import model.*;

public class BoardFactory {

    
    
    //méthode pour tout créer
    public static Board createInitialBoard(){
        Board board = new Board();
        Player p = new Player("Joueur 1");
        Bot b1 = new Bot(1);
        Bot b2 = new Bot(2);

        createPositions(board);

        connectPositions(board, board.getPositions());
        placeInitialPieces(board, board.getPositions(), p, b1, b2);

        board.recomputeAttackMaps();
        return board;
    }


    //methode pour créer les cases
    public static void createPositions(Board board){

        for(int tiers=1;tiers<4;tiers++){
            for(int ligne=1;ligne<5;ligne++){
                for(int colonne=1;colonne<9;colonne++){
                Position p = new Position(tiers,ligne,colonne);
                board.getPositions().put(p.getId(), p);
                board.addPosition(p);
                }
            }
        }
    }


    /*connecte les cases vosines entre elles en appelant les méthodes de connexion de ligne*/
    public static void connectPositions(Board board, Map<Integer, Position> positions) {
        for(Position p : positions.values()){
            connectDirectNeighbors(board, positions, p);
        }
        connectJunctionTiers(board, positions);

        /*Print de debut de tout les voisins de toutes les cases 
        for(int tiers=1;tiers<4;tiers++){
            for(int ligne=1;ligne<5;ligne++){
                for(int colonne=1;colonne<9;colonne++){
                System.out.println(board.findPosition(positions, tiers, ligne, colonne));
                System.out.println(board.getNeighbors(board.findPosition(positions, tiers, ligne, colonne)));
                }
            }
        }*/

    }

    public static void addNeighborIfExists(Board board,Position from, Direction direction, Position to) {
    if (to != null) {
        board.connectSide(from,to,direction);
    }
}
     public static void addJunction(Board board,Position from, Direction direction, Position to) {
    if (to != null) {
        board.connectOneWay(from, to, direction);
    }
}

    public static void connectCenterCases(Board board,Map<Integer, Position> positions, Position p) {
        
        //a demander au prof pour les ifs dans des ifs
        if(p.getLigne()==4 && p.getColonne()==4){
            addNeighborIfExists(board, p, Direction.RIGHT, board.findPosition(positions, p.getTiers(), p.getLigne(), p.getColonne() + 1));
            if(p.getTiers()==1){
                addJunction(board, p, Direction.UP, board.findPosition(positions, p.getTiers()+1, p.getLigne(), p.getColonne() + 1));
                addJunction(board, p, Direction.DIAG_UP_LEFT, board.findPosition(positions, p.getTiers()+1, p.getLigne(), p.getColonne() + 2));
                addNeighborIfExists(board, p, Direction.DIAG_CENTRE_LEFT, board.findPosition(positions, p.getTiers()+1, p.getLigne(), p.getColonne()));
                addNeighborIfExists(board, p, Direction.DIAG_CENTRE_RIGHT, board.findPosition(positions, p.getTiers()+2, p.getLigne(), p.getColonne()));
            }
            else if(p.getTiers()==2){
                addJunction(board, p, Direction.UP, board.findPosition(positions, p.getTiers()+1, p.getLigne(), p.getColonne() + 1));
                addJunction(board, p, Direction.DIAG_UP_LEFT, board.findPosition(positions, p.getTiers()+1, p.getLigne(), p.getColonne() + 2));
                addNeighborIfExists(board, p, Direction.DIAG_CENTRE_LEFT, board.findPosition(positions, p.getTiers()+1, p.getLigne(), p.getColonne()));
                addNeighborIfExists(board, p, Direction.DIAG_CENTRE_RIGHT, board.findPosition(positions, p.getTiers()-1, p.getLigne(), p.getColonne()));
            }
            else{
                addJunction(board, p, Direction.UP, board.findPosition(positions, p.getTiers()-2, p.getLigne(), p.getColonne() + 1));
                addJunction(board, p, Direction.DIAG_UP_LEFT, board.findPosition(positions, p.getTiers()-2, p.getLigne(), p.getColonne() + 2));
                addNeighborIfExists(board, p, Direction.DIAG_CENTRE_LEFT, board.findPosition(positions, p.getTiers()-2, p.getLigne(), p.getColonne()));
                addNeighborIfExists(board, p, Direction.DIAG_CENTRE_RIGHT, board.findPosition(positions, p.getTiers()-1, p.getLigne(), p.getColonne()));
            }
           

        }else if(p.getColonne()==5){
            addNeighborIfExists(board, p, Direction.RIGHT, board.findPosition(positions, p.getTiers(), p.getLigne(), p.getColonne() + 1));
            if(p.getTiers()==1){
                addJunction(board, p, Direction.UP, board.findPosition(positions, p.getTiers()+2, p.getLigne(), p.getColonne()-1));
                addJunction(board, p, Direction.DIAG_UP_RIGHT, board.findPosition(positions, p.getTiers()+2, p.getLigne(), p.getColonne() - 2));
                addNeighborIfExists(board, p, Direction.DIAG_CENTRE_RIGHT, board.findPosition(positions, p.getTiers()+2, p.getLigne(), p.getColonne()));
                addNeighborIfExists(board, p, Direction.DIAG_CENTRE_LEFT, board.findPosition(positions, p.getTiers()+1, p.getLigne(), p.getColonne()));
            }
            else if(p.getTiers()==2){
                addJunction(board, p, Direction.UP, board.findPosition(positions, p.getTiers()-1, p.getLigne(), p.getColonne()-1));
                addJunction(board, p, Direction.DIAG_UP_RIGHT, board.findPosition(positions, p.getTiers()-1, p.getLigne(), p.getColonne() - 2));
                addNeighborIfExists(board, p, Direction.DIAG_CENTRE_RIGHT, board.findPosition(positions, p.getTiers()-1, p.getLigne(), p.getColonne()));
                addNeighborIfExists(board, p, Direction.DIAG_CENTRE_LEFT, board.findPosition(positions, p.getTiers()+1, p.getLigne(), p.getColonne()));
            }
            else{
                addJunction(board, p, Direction.UP, board.findPosition(positions, p.getTiers()-1, p.getLigne(), p.getColonne()-1));
                addJunction(board, p, Direction.DIAG_UP_RIGHT, board.findPosition(positions, p.getTiers()-1, p.getLigne(), p.getColonne() - 2));
                addNeighborIfExists(board, p, Direction.DIAG_CENTRE_RIGHT, board.findPosition(positions, p.getTiers()-1, p.getLigne(), p.getColonne()));
                addNeighborIfExists(board, p, Direction.DIAG_CENTRE_LEFT, board.findPosition(positions, p.getTiers()-2, p.getLigne(), p.getColonne()));
            }
        }
    
}

        
    public static void connectDirectNeighbors(Board board, Map<Integer, Position> positions, Position p) {
    int tiers = p.getTiers();
    int ligne = p.getLigne();
    int colonne = p.getColonne();

    if(ligne==4 && (colonne == 4 || colonne == 5)){
        // Cas spéciaux du centre à traiter
        connectCenterCases(board, positions,p);
    }else{
        addNeighborIfExists(board, p, Direction.RIGHT, board.findPosition(positions, tiers, ligne, colonne + 1));
        addNeighborIfExists(board, p, Direction.UP, board.findPosition(positions, tiers, ligne+1, colonne));
        addNeighborIfExists(board, p, Direction.DIAG_UP_LEFT, board.findPosition(positions, tiers, ligne + 1, colonne - 1));
        addNeighborIfExists(board, p, Direction.DIAG_UP_RIGHT, board.findPosition(positions, tiers, ligne + 1, colonne + 1));  
    }
}

    public static void connectJunctionTiers(Board board, Map<Integer, Position> positions) {
    for (Position p : positions.values()) {
        if (p.getLigne()==4) {
            connectJunctionCase(board, positions, p);
        }
    }
}

    public static void connectJunctionCase(Board board, Map<Integer, Position> positions, Position p) {
    int tiers = p.getTiers();
    int ligne = p.getLigne();
    int colonne = p.getColonne();

    if(tiers==1){
        if(colonne<=3){
            addJunction(board, p, Direction.UP, board.findPosition(positions, tiers+1, ligne, 9-colonne));
            if(colonne==1){
                addJunction(board, p, Direction.DIAG_UP_RIGHT,board.findPosition(positions, tiers+1, ligne, 8-colonne));
            }
            else{
                addJunction(board, p, Direction.DIAG_UP_LEFT,board.findPosition(positions, tiers+1, ligne, 10-colonne));
                addJunction(board, p, Direction.DIAG_UP_RIGHT,board.findPosition(positions, tiers+1, ligne, 8-colonne));
            }
        }
        else if(colonne>5 && colonne<= 8){
            addJunction(board, p, Direction.UP, board.findPosition(positions, tiers+2, ligne, 9-colonne));
            if(colonne==8){
                addJunction(board, p, Direction.DIAG_UP_LEFT,board.findPosition(positions, tiers+2, ligne, 10-colonne));
            }
            else{
                addJunction(board, p, Direction.DIAG_UP_LEFT,board.findPosition(positions, tiers+2, ligne, 10-colonne));
                addJunction(board, p, Direction.DIAG_UP_RIGHT,board.findPosition(positions, tiers+2, ligne, 8-colonne));
            }
        }
       
    }else if(tiers==2){
        if(colonne<=3){
            addJunction(board, p, Direction.UP, board.findPosition(positions, tiers+1, ligne, 9-colonne));
            if(colonne==1){
                addJunction(board, p, Direction.DIAG_UP_RIGHT,board.findPosition(positions, tiers+1, ligne, 8-colonne));
            }
            else{
                addJunction(board, p, Direction.DIAG_UP_LEFT,board.findPosition(positions, tiers+1, ligne, 10-colonne));
                addJunction(board, p, Direction.DIAG_UP_RIGHT,board.findPosition(positions, tiers+1, ligne, 8-colonne));
            }
        }
        else if(colonne>5 && colonne<= 8){
            addJunction(board, p, Direction.UP, board.findPosition(positions, tiers-1, ligne, 9-colonne));
            if(colonne==8){
                addJunction(board, p, Direction.DIAG_UP_LEFT,board.findPosition(positions, tiers-1, ligne, 10-colonne));
            }
            else{
                addJunction(board, p, Direction.DIAG_UP_LEFT,board.findPosition(positions, tiers-1, ligne, 10-colonne));
                addJunction(board, p, Direction.DIAG_UP_RIGHT,board.findPosition(positions, tiers-1, ligne, 8-colonne));
            }
        }
       
    }else{
        if(colonne<=3){
            addJunction(board, p, Direction.UP, board.findPosition(positions, tiers-2, ligne, 9-colonne));
            if(colonne==1){
                addJunction(board, p, Direction.DIAG_UP_RIGHT,board.findPosition(positions, tiers-2, ligne, 8-colonne));
            }
            else{
                addJunction(board, p, Direction.DIAG_UP_LEFT,board.findPosition(positions, tiers-2, ligne, 10-colonne));
                addJunction(board, p, Direction.DIAG_UP_RIGHT,board.findPosition(positions, tiers-2, ligne, 8-colonne));
            }
        }
        else if(colonne>5 && colonne<= 8){
            addJunction(board, p, Direction.UP, board.findPosition(positions, tiers-1, ligne, 9-colonne));
            if(colonne==8){
                addJunction(board, p, Direction.DIAG_UP_LEFT,board.findPosition(positions, tiers-1, ligne, 10-colonne));
            }
            else{
                addJunction(board, p, Direction.DIAG_UP_LEFT,board.findPosition(positions, tiers-1, ligne, 10-colonne));
                addJunction(board, p, Direction.DIAG_UP_RIGHT,board.findPosition(positions, tiers-1, ligne, 8-colonne));
            }
        }
       
    }

}



    //crée et place les pieces pour chaque joueur a leur emplacement au début de la partie
    public static void placeInitialPieces(Board board, Map<Integer, Position> positions, Player p, Bot b1, Bot b2){
        int i=1;
        //blue player ( bot1 )
        board.setPiece(board.findPosition(positions, 2, 1, i), PieceFactory.createPiece(PieceType.Rook, b1, Color.blue,PieceFactory.getStrategies(),2));
        board.setPiece(board.findPosition(positions, 2, 1, i+1), PieceFactory.createPiece(PieceType.Knight, b1, Color.blue,PieceFactory.getStrategies(),2));
        board.setPiece(board.findPosition(positions, 2, 1, i+2), PieceFactory.createPiece(PieceType.Bishop, b1, Color.blue,PieceFactory.getStrategies(),2));
        board.setPiece(board.findPosition(positions, 2, 1, i+3), PieceFactory.createPiece(PieceType.Queen, b1, Color.blue,PieceFactory.getStrategies(),2));
        board.setPiece(board.findPosition(positions, 2, 1, i+4), PieceFactory.createPiece(PieceType.King, b1, Color.blue,PieceFactory.getStrategies(),2));
        board.setPiece(board.findPosition(positions, 2, 1, i+5), PieceFactory.createPiece(PieceType.Bishop, b1, Color.blue,PieceFactory.getStrategies(),2));
        board.setPiece(board.findPosition(positions, 2, 1, i+6), PieceFactory.createPiece(PieceType.Knight, b1, Color.blue,PieceFactory.getStrategies(),2));
        board.setPiece(board.findPosition(positions, 2, 1, i+7), PieceFactory.createPiece(PieceType.Rook, b1, Color.blue,PieceFactory.getStrategies(),2));

        //red player ( bot2 )
        board.setPiece(board.findPosition(positions, 3, 1, i), PieceFactory.createPiece(PieceType.Rook, b2, Color.red,PieceFactory.getStrategies(),3));
        board.setPiece(board.findPosition(positions, 3, 1, i+1), PieceFactory.createPiece(PieceType.Knight, b2, Color.red,PieceFactory.getStrategies(),3));
        board.setPiece(board.findPosition(positions, 3, 1, i+2), PieceFactory.createPiece(PieceType.Bishop, b2, Color.red,PieceFactory.getStrategies(),3));
        board.setPiece(board.findPosition(positions, 3, 1, i+3), PieceFactory.createPiece(PieceType.Queen, b2, Color.red,PieceFactory.getStrategies(),3));
        board.setPiece(board.findPosition(positions, 3, 1, i+4), PieceFactory.createPiece(PieceType.King, b2, Color.red,PieceFactory.getStrategies(),3));
        board.setPiece(board.findPosition(positions, 3, 1, i+5), PieceFactory.createPiece(PieceType.Bishop, b2, Color.red,PieceFactory.getStrategies(),3));
        board.setPiece(board.findPosition(positions, 3, 1, i+6), PieceFactory.createPiece(PieceType.Knight, b2, Color.red,PieceFactory.getStrategies(),3));
        board.setPiece(board.findPosition(positions, 3, 1, i+7), PieceFactory.createPiece(PieceType.Rook, b2, Color.red,PieceFactory.getStrategies(),3));

        //white player ( player )
        board.setPiece(board.findPosition(positions, 1, 1, i), PieceFactory.createPiece(PieceType.Rook, p, Color.white,PieceFactory.getStrategies(),1));
        board.setPiece(board.findPosition(positions, 1, 1, i+1), PieceFactory.createPiece(PieceType.Knight, p, Color.white,PieceFactory.getStrategies(),1));
        board.setPiece(board.findPosition(positions, 1, 1, i+2), PieceFactory.createPiece(PieceType.Bishop, p, Color.white,PieceFactory.getStrategies(),1));
        board.setPiece(board.findPosition(positions, 1, 1, i+3), PieceFactory.createPiece(PieceType.Queen, p, Color.white,PieceFactory.getStrategies(),1));
        board.setPiece(board.findPosition(positions, 1, 1, i+4), PieceFactory.createPiece(PieceType.King, p, Color.white,PieceFactory.getStrategies(),1));
        board.setPiece(board.findPosition(positions, 1, 1, i+5), PieceFactory.createPiece(PieceType.Bishop, p, Color.white,PieceFactory.getStrategies(),1));
        board.setPiece(board.findPosition(positions, 1, 1, i+6), PieceFactory.createPiece(PieceType.Knight, p, Color.white,PieceFactory.getStrategies(),1));
        board.setPiece(board.findPosition(positions, 1, 1, i+7), PieceFactory.createPiece(PieceType.Rook, p, Color.white,PieceFactory.getStrategies(),1));
        
        //pawns
        for(i=1;i<9;i++){
        board.setPiece(board.findPosition(positions, 3, 2, i), PieceFactory.createPiece(PieceType.Pawn, b2, Color.red,PieceFactory.getStrategies(),3));
        board.setPiece(board.findPosition(positions, 2, 2, i), PieceFactory.createPiece(PieceType.Pawn, b1, Color.blue,PieceFactory.getStrategies(),2));
        board.setPiece(board.findPosition(positions, 1, 2, i), PieceFactory.createPiece(PieceType.Pawn, p, Color.white,PieceFactory.getStrategies(),1));
        }

        /*DEBUG
         for(int tiers=1;tiers<4;tiers++){
            for(int ligne=1;ligne<5;ligne++){
                for(int colonne=1;colonne<9;colonne++){
                    if(board.findPosition(positions, tiers, ligne, colonne)!=null){
                        System.out.println(board.getPiece(board.findPosition(positions, tiers, ligne, colonne)));
                    }
                
                }
            }
        }*/

    }
}
