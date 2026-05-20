package com.chess.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.chess.controller.engine.GameEngine;

public class Bot implements People {
    
    private static int nextid=1;
    private int botId;
    private String name;
    private PeopleType type;
    private static final Map<PieceType, Integer> pieceValues = Map.of(
                                                                        PieceType.Pawn, 1,
                                                                        PieceType.Knight, 3,
                                                                        PieceType.Bishop, 3,
                                                                        PieceType.Rook, 5,
                                                                        PieceType.Queen, 9,
                                                                        PieceType.King, 100
                                                                    );

    public Bot(){
        botId=nextid++;
        name = "Bot : " + botId;
        type = PeopleType.Bot;
    } 

    @Override
    public String toString() {
        
        return "Bot "+ botId;
    }

    @Override
    public int getId() {
        return botId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PeopleType getType() {
        return type;
    }

    public Move chooseBestMove(GameEngine engine, People bot) {
        List<Move> moves = getAllLegalMoves(engine, bot);

        return moves.parallelStream()
                .map(move -> {
                    GameEngine copy = engine.deepCopy();
                    Move copiedMove = copy.findEquivalentMove(move);

                    MoveCommand command = new MoveCommand(copy, copiedMove);

                    if (!command.execute()) {
                        return new ScoredMove(move, Integer.MIN_VALUE);
                    }

                    int score = minimax(copy, 3, Integer.MIN_VALUE,Integer.MAX_VALUE, bot);

                    command.undo();

                    return new ScoredMove(move, score);
                })
                //compare les score de a et b
                .max((a, b) -> Integer.compare(a.score(), b.score()))
                //extrait le meilleur coup possible
                .map(sm -> sm.move())
                //et sinon renvoie null
                .orElse(null);
    }

    //evalue le meilleur coup
    public int evaluate(GameEngine engine, People bot) {
        Board board = engine.getBoard();
        int score = 0;

        for (Position pos : board.getPositions().values()) {
            Piece p = board.getPiece(pos);
            if (p == null) continue;

            int value = pieceValues.getOrDefault(p.getType(), 0) * 100;

            // bonus mobilité / centre / avancement
            int bonus = 0;
            bonus += pos.getLigne() * 5; 
            // avance vers le centre
            if (pos.getLigne() == 4) bonus += 20; 
            // jonctions/centre importants
            if (pos.getColonne() >= 3 && pos.getColonne() <= 6) bonus += 10;

            if (p.getOwner().equals(bot)) {
                score += value + bonus;
            } else {
                score -= value + bonus;
            }
        }
        int mobility = getAllLegalMoves(engine, bot).size();
        score += mobility;

        return score;
    }

    public int minimax(GameEngine engine, int depth, int alpha, int beta, People bot) {

        //pour faire en sorte que les bots cherchent le mat
        for (People player : engine.getPlayers()) {
            if (engine.isCheckmate(player)) {
                if (player.equals(bot)) {
                    return -100000;
                } else {
                    return 100000;
                }
            }
        }

        if (depth == 0 || engine.isGameOver()) {
            return evaluate(engine, bot);
        }

        People current = engine.getCurrentPlayer();
        List<Move> moves = getAllLegalMoves(engine, current);
        boolean maximizing = current.equals(bot);

        if (maximizing) {
            int best = Integer.MIN_VALUE;
            for (Move move : moves) {
                MoveCommand command = new MoveCommand(engine, move);
                if (!command.execute()) continue;
                int score = minimax(engine, depth - 1, alpha, beta, bot);
                command.undo();

                best = Math.max(best, score);
                alpha = Math.max(alpha, best);
                if (beta <= alpha) break;// break beta
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (Move move : moves) {
                MoveCommand command = new MoveCommand(engine, move);
                if (!command.execute()) continue;
                int score = minimax(engine, depth - 1, alpha, beta, bot);
                command.undo();

                best = Math.min(best, score);
                beta = Math.min(beta, best);
                if (beta <= alpha) break; //break aplha
            }
            return best;
        }
    }

    public List<Move> getAllLegalMoves(GameEngine engine, People bot) {
        List<Move> legalMoves = new ArrayList<>();
        Board board = engine.getBoard();

        for (Position pos : board.getPositions().values()) {
            Piece piece = board.getPiece(pos);
            if (piece == null || !piece.getOwner().equals(bot)) continue;

            List<Move> possibleMoves = piece.getMovementStrategy().getPossibleMoves(pos, board, piece);

            for (Move move : possibleMoves) {
                if (engine.isMoveValid(move)) {
                    legalMoves.add(move);
                }
            }
        }

        // Captures en premier pour maximiser les coupures Alpha-Beta
        legalMoves.sort((a, b) -> {
            boolean aCapture = board.getPiece(a.getTo()) != null;
            boolean bCapture = board.getPiece(b.getTo()) != null;
            return Boolean.compare(!aCapture, !bCapture);
        });

        return legalMoves;
    }

}
