package com.chess.factories;

import java.util.Map;

import com.chess.model.Color;
import com.chess.model.People;
import com.chess.model.Piece;
import com.chess.model.PieceType;
import com.chess.model.strategy.*;

//factory de pieces

public class PieceFactory {

    private static final Map<PieceType, MovementStrategy> strategies = Map.of(
        PieceType.Rook, new RookStrategy(),
        PieceType.Bishop, new BishopStrategy(),
        PieceType.Queen, new QueenStrategy(),
        PieceType.King, new KingStrategy(),
        PieceType.Knight, new KnightStrategy(),
        PieceType.Pawn, new PawnStrategy()
    );


    public static Piece createPiece(PieceType type, People owner, Color color,Map<PieceType, MovementStrategy> strategies,int tier) {
        Piece P = new Piece(type, owner,color,tier);
        P.setMovementStrategy(type, strategies);
        return P;
    }

    public static Map<PieceType, MovementStrategy> getStrategies() {
        return strategies;
    }

}
