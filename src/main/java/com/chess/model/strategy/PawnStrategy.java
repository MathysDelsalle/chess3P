package com.chess.model.strategy;

import java.util.ArrayList;
import java.util.List;

import com.chess.model.*;

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

        //checke les diagonales , vérifie si la diagonale existe et si y'a une piece dessus alors si l'owner est différent on peut la manger
        //et si on est a une jonction alors on avance dans l'autre sens
        for(Direction dir : PAWN_DIRECTIONS){

            Position previous = board.getNeighborsDirection(from, normalDir);
            Position to=board.getNeighborsDirection(from, dir.switchDir());

            if(piece.getStartTier()==from.getTiers()){
                previous = board.getNeighborsDirection(from, normalDir.switchDir());
                to=board.getNeighborsDirection(from, dir);
            }

            if(to!=null){
                previous = board.getNeighborsDirection(from, normalDir.switchDir());
                Piece target = board.getPiece(to);
                if(previous!=null && from.getIsJunction() && previous.getIsJunction()){
                    to = board.getNeighborsDirection(from, dir.switchDir());
                    target = board.getPiece(to);
                    if(target!=null && !target.getOwner().equals(piece.getOwner())){
                        moves.add(new Move(from, to, dir.switchDir(),piece));
                    }
                }
                else{
                    if(target!=null && !target.getOwner().equals(piece.getOwner())){
                        moves.add(new Move(from, to, dir,piece));
                    }
                
                }
                
            }
        }

        //en passant
        Position enPassantTarget = board.getEnPassantTarget();

        if (enPassantTarget != null) {
            for (Direction dir : PAWN_DIRECTIONS) {
                Position to = board.getNeighborsDirection(from, dir.switchDir());
                Direction moveDirection = dir.switchDir();

                if (piece.getStartTier() == from.getTiers()) {
                    to = board.getNeighborsDirection(from, dir);
                    moveDirection = dir;
                }

                Position previous = board.getNeighborsDirection(from, normalDir);

                if (piece.getStartTier() == from.getTiers()) {
                    previous = board.getNeighborsDirection(from, normalDir.switchDir());
                }

                if (previous != null && from.getIsJunction() && previous.getIsJunction()) {
                    to = board.getNeighborsDirection(from, dir.switchDir());
                    moveDirection = dir.switchDir();
                }

                if (to != null && to.equals(enPassantTarget)) {
                    moves.add(new Move(from, to, moveDirection, piece));
                }
            }
        }
        
        //ce code vérifie d'abord si la piece a bougée puis si elle n'a pas bougé vérifie qu'l n'y a pas de piece sur la case d'après
        //s'il n'y a pas de piece elle ajoute le mouvement et vérifie s'il n'y a pas de piece sur la case encore après 
        //s'il n'y a encore pas de piece elle ajoute le mouvement de deux case (coup spécial pion)
        if(!piece.getHasMoved()){
            Position to = board.getNeighborsDirection(from, normalDir);
            Piece target = board.getPiece(to);
            if(target==null){
                moves.add(new Move(from, to , normalDir,piece));
                Position temp = board.getNeighborsDirection(to, normalDir);
                if(to.getIsJunction() && temp.getIsJunction()){
                    temp=board.getNeighborsDirection(to, normalDir.switchDir());
                }
                target = board.getPiece(temp);
                if(target==null){
                    moves.add(new Move(from, temp , normalDir,piece));
                }
                
            }
        }
        //cette partie permet au pion d'avancer en ligne droite une case par une case et lorsqu'il passe une jonction, 
        // continue d'avancer en utilisant DOWN au lieu de UP
        else{
            Position previous = board.getNeighborsDirection(from, normalDir);
            Position to=board.getNeighborsDirection(from, normalDir.switchDir());
            if(piece.getStartTier()==from.getTiers()){
                previous = board.getNeighborsDirection(from, normalDir.switchDir());
                to=board.getNeighborsDirection(from, normalDir);
            }
            
            if(to!=null){
                
                Piece target = board.getPiece(to);
                if(target==null){
                    if(previous != null && from.getIsJunction() && previous.getIsJunction()){
                        target = board.getPiece(to);
                        if(target==null){
                            moves.add(new Move(from,to,normalDir.switchDir(),piece));
                        }
                        
                    }
                    else{
                        moves.add(new Move(from,to,normalDir,piece));
                        
                    }
                }
            }
            else{
                //promotion
            }
               
        }
        
        
        return moves;
    }
    

    @Override
    public AttackInfo getAttackedAndProtectedSquares(Position from, Board board, Piece piece) {

        List<Position> attackedSquares = new ArrayList<>();
        List<Position> protectedSquares = new ArrayList<>();

        Direction normalDir = Direction.UP;

        Direction[] PAWN_DIRECTIONS = {
            Direction.DIAG_UP_LEFT,
            Direction.DIAG_UP_RIGHT,
            Direction.DIAG_CENTRE_LEFT,
            Direction.DIAG_CENTRE_RIGHT
        };

        for (Direction dir : PAWN_DIRECTIONS) {

            Position previous = board.getNeighborsDirection(from, normalDir);
            Position to = board.getNeighborsDirection(from, dir.switchDir());

            if (piece.getStartTier() == from.getTiers()) {
                previous = board.getNeighborsDirection(from, normalDir.switchDir());
                to = board.getNeighborsDirection(from, dir);
            }

            if (to != null) {
                if (previous != null && from.getIsJunction() && previous.getIsJunction()) {
                    to = board.getNeighborsDirection(from, dir.switchDir());
                }

                if (to != null && !attackedSquares.contains(to)) {
                    attackedSquares.add(to);
                }
            }
        }

        return new AttackInfo(attackedSquares, protectedSquares);
    }

}
