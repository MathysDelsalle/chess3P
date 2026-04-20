package com.chess.view;

import java.util.Map;
import java.util.Set;

import com.chess.model.Board;
import com.chess.model.Piece;
import com.chess.model.Position;

public class ConsoleView {
    public void diplayCreationReussie(){
        System.out.println("Plateau créé sans soucis :)) .");
    }

    public void displayPossibleMoves(Position p, Board board){
        System.out.println("-------------------------------------------------------------------");
        System.out.println(board.getPiece(p).getMovementStrategy().getPossibleMoves(p, board,board.getPiece(p)));

    }

    public void displayAttackMap(Map<Position, Set<Piece>> underAttack){
        underAttack.forEach((pos, pieces) -> {
        System.out.println("Case " + pos + " attaquée par : " + pieces);
        System.out.println("-------------------------------------------------------------");
    });
    }

    public void displayControledMap(Map<Position, Set<Piece>> protectedRightNow){
        protectedRightNow.forEach((pos, pieces) -> {
        System.out.println("Case " + pos + " attaquée par : " + pieces+ " mais protégée");
        System.out.println("-------------------------------------------------------------");
    });
    }
}
