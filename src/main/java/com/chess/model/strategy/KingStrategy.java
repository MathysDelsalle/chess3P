package com.chess.model.strategy;

import java.util.ArrayList;
import java.util.List;

import com.chess.model.*;
import com.chess.model.AttackInfo;

public class KingStrategy implements MovementStrategy{
 

   @Override
    public List<Move> getPossibleMoves(Position from, Board board, Piece piece) {
        List<Move> moves = new ArrayList<>();  
        Direction[] KING_DIRECTIONS = {
            Direction.UP,
            Direction.DOWN,
            Direction.RIGHT,
            Direction.LEFT,
            Direction.DIAG_UP_LEFT,
            Direction.DIAG_UP_RIGHT,
            Direction.DIAG_DOWN_LEFT,
            Direction.DIAG_DOWN_RIGHT,
            Direction.DIAG_CENTRE_LEFT,
            Direction.DIAG_CENTRE_RIGHT
        };

        Direction[] ROQUE_DIR = {

            Direction.RIGHT,
            Direction.LEFT
        };

        for(Direction dir : KING_DIRECTIONS){

            Position previous = board.getNeighborsDirection(from, dir.switchDir());
            Position to=board.getNeighborsDirection(from, dir);
            if(piece.getStartTier()==from.getTiers()){
                previous = board.getNeighborsDirection(from, dir);
                to=board.getNeighborsDirection(from, dir.switchDir());
            }

            if(to!=null){
                previous = board.getNeighborsDirection(from, dir.switchDir());
                Piece target = board.getPiece(to);
                if(previous!=null && from.getIsJunction() && previous.getIsJunction()){
                    to = board.getNeighborsDirection(from, dir.switchDir());
                    target = board.getPiece(to);
                    if(target==null || !target.getOwner().equals(piece.getOwner())){
                        moves.add(new Move(from, to, dir.switchDir(),piece));
                    }
                }
                else{
                    if(target==null || !target.getOwner().equals(piece.getOwner())){
                        moves.add(new Move(from, to, dir,piece));
                    }
                
                }
                
            }
        }
        if(!piece.getHasMoved()){
            for(Direction dir : ROQUE_DIR){
                Position to = board.getNeighborsDirection(from, dir);
                Position intermediateStep;
                Position newKingPlace;
                while(to!=null){
                    Piece p = board.getPiece(to);
                    if(p!=null && p.getType()!=PieceType.Rook){
                        break;
                    }
                    if(p!=null && p.getType()==PieceType.Rook && p.getOwner().equals(piece.getOwner())){
                        if(!p.getHasMoved()){
                            if(to.getColonne()==1){
                                intermediateStep=board.getNeighborsDirection(from, dir);
                                newKingPlace=board.getNeighborsDirection(intermediateStep, dir);
                                moves.add(new Move(from, newKingPlace, dir, piece));
                            }
                            else if(to.getColonne()==8){
                                intermediateStep=board.getNeighborsDirection(from, dir);
                                newKingPlace=board.getNeighborsDirection(intermediateStep, dir);
                                moves.add(new Move(from, newKingPlace, dir, piece));
                            }
                        }
                        break;
                        
                    }
                    else{
                        to = board.getNeighborsDirection(to, dir);
                    }
                }
            }
        }
        return moves;
    }
    

    @Override
    public AttackInfo getAttackedAndProtectedSquares(Position from, Board board, Piece piece) {

        List<Position> attackedSquares = new ArrayList<>();
        List<Position> protectedSquares = new ArrayList<>();

        Direction[] KING_DIRECTIONS = {
            Direction.UP,
            Direction.DOWN,
            Direction.RIGHT,
            Direction.LEFT,
            Direction.DIAG_UP_LEFT,
            Direction.DIAG_UP_RIGHT,
            Direction.DIAG_DOWN_LEFT,
            Direction.DIAG_DOWN_RIGHT,
            Direction.DIAG_CENTRE_LEFT,
            Direction.DIAG_CENTRE_RIGHT
        };

        for (Direction dir : KING_DIRECTIONS) {

            Position previous = board.getNeighborsDirection(from, dir.switchDir());
            Position to = board.getNeighborsDirection(from, dir);

            if (piece.getStartTier() == from.getTiers()) {
                previous = board.getNeighborsDirection(from, dir);
                to = board.getNeighborsDirection(from, dir.switchDir());
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
