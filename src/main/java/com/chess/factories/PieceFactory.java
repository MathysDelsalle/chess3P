package factories;

import java.util.Map;

import model.Color;
import model.People;
import model.Piece;
import model.PieceType;
import model.strategy.*;

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


    public static Piece createPiece(PieceType type, People owner, Color color,Map<PieceType, MovementStrategy> strategies) {
        Piece P = new Piece(type, owner,color);
        P.setMovementStrategy(type, strategies);
        return P;
    }

    public static Map<PieceType, MovementStrategy> getStrategies() {
        return strategies;
    }

}
