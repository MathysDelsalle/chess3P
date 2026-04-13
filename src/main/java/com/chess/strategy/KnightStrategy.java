package strategy;

import java.util.ArrayList;
import java.util.List;

import modele.*;

public class KnightStrategy implements MovementStrategy{

    @Override
    public List<Position> getPossibleMoves(Position from, Board board, Piece piece){

        return new ArrayList<>();
    }
}
