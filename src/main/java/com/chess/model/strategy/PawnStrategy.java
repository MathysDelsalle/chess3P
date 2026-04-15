package model.strategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.*;

public class PawnStrategy implements MovementStrategy{
 

   @Override
    public List<Move> getPossibleMoves(Position from, Board board, Piece piece) {
        List<Move> moves = new ArrayList<>();

        Direction normalDir = Direction.UP;
        
        
        Direction[] PAWN_DIRECTIONS = {
            Direction.DIAG_UP_LEFT,
            Direction.DIAG_UP_RIGHT,
            Direction.DIAG_CENTRE_LEFT,
            Direction.DIAG_CENTRE_RIGHT
        };
        //ce code vérifie d'abord si la piece a bougée puis si elle n'a pas bougé vérifie qu'l n'y a pas de piece sur la case d'après
        //s'il n'y a pas de piece elle ajoute le mouvement et vérifie s'il n'y a pas de piece sur la case encore après 
        //s'il n'y a encore pas de piece elle ajoute le mouvement de deux case (coup spécial pion)
        if(!piece.getHasMoved()){
            Position to = board.getNeighborsDirection(from, normalDir);
            Piece target = board.getPiece(to);
            if(target==null){
                moves.add(new Move(from, to , normalDir));
                Position temp = board.getNeighborsDirection(from, normalDir);
                to = board.getNeighborsDirection(temp, normalDir);
                target = board.getPiece(temp);
                if(target==null){
                    moves.add(new Move(from, to , normalDir));
                }
                
            }
        }
        //cette partie permet au pion d'avancer en ligne droite une case par une case et lorsqu'il passe une jonction, 
        // continue d'avancer en utilisant DOWN au lieu de UP
        else{
            Position to=board.getNeighborsDirection(from, normalDir);
            Position previous = board.getNeighborsDirection(from, normalDir.switchDir());
            if(from.getIsJunction() && previous.getIsJunction()){
                normalDir.switchDir();
                to=board.getNeighborsDirection(from, normalDir);
                moves.add(new Move(from,to,normalDir));
            }
            else{
                to=board.getNeighborsDirection(from, normalDir);
                moves.add(new Move(from,to,normalDir));
            }
        }
        
        //maitenant je dois faire la capture en diagonale du pion 

        return moves;
    }
    
}
