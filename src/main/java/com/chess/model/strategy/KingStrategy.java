package model.strategy;

import java.util.ArrayList;
import java.util.List;

import model.*;

public class KingStrategy implements MovementStrategy{

    @Override
    public List<Position> getPossibleMoves(Position from, Board board, Piece piece){

        return new ArrayList<>();
    }
    
}
