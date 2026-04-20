package com.chess.model.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.chess.model.Board;
import com.chess.model.Move;
import com.chess.model.People;
import com.chess.model.Piece;
import com.chess.model.PieceType;
import com.chess.model.Position;

public class GameEngine {
    private final Board board;
    private final List<People> players;
    private int currentPlayerId;
    private People winner = null;
    private boolean gameOver = false;

    public GameEngine(Board board,List<People> players){
        this.board = board;
        this.players = new ArrayList<>(players);
        this.currentPlayerId = players.get(0).getId();
    }

    public Board getBoard() {
        return board;
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public People getCurrentPlayer(){
        return players.get(currentPlayerId);
    }

    public List<People> getPlayers() {
        return players;
    }

    public void nextTurn() {
        currentPlayerId = (currentPlayerId + 1) % players.size();
    }

    public boolean isMoveValid(Move move){
        Piece piece = board.getPiece(move.getFrom());
        
        if (piece == null) return false;
        if (!piece.getOwner().equals(getCurrentPlayer())) return false;

        List<Move> possibleMoves = piece.getMovementStrategy().getPossibleMoves(move.getFrom(), board, piece);

        if(!possibleMoves.contains(move)) return false;

        // simulation
        Piece captured = board.getPiece(move.getTo());

        board.setPiece(move.getTo(), piece);
        board.setPiece(move.getFrom(), null);

        board.recomputeAttackMaps();

        boolean kingInCheck = isKingInCheck(piece.getOwner());

        // rollback
        board.setPiece(move.getFrom(), piece);
        board.setPiece(move.getTo(), captured);

        board.recomputeAttackMaps();

        return !kingInCheck;
    }

    public Position findKingPosition(People owner) {
        for (Position pos : board.getPositions().values()) {
            Piece piece = board.getPiece(pos);

            if (piece != null
                    && piece.getType() == PieceType.King
                    && piece.getOwner().equals(owner)) {
                return pos;
            }
        }
        return null;
    }

    public boolean isKingInCheck(People owner) {
        Position kingPosition = findKingPosition(owner);

        if (kingPosition == null) {
                return false;
            }

        Set<Piece> attackers = board.getUnderAttack().get(kingPosition);

        if (attackers == null) {
            return false;
        }

        for (Piece p : attackers) {
            if (!p.getOwner().equals(owner)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasAnyLegalMove(People player) {
        for (Position pos : board.getPositions().values()) {
            Piece piece = board.getPiece(pos);

            if (piece == null) {
                continue;
            }

            if (!piece.getOwner().equals(player)) {
                continue;
            }

            List<Move> possibleMoves =
                    piece.getMovementStrategy().getPossibleMoves(pos, board, piece);

            for (Move move : possibleMoves) {
                if (isMoveValid(move)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isCheckmate(People player) {
    return isKingInCheck(player) && !hasAnyLegalMove(player);
    }

    public boolean isStalemate(People player) {
    return !isKingInCheck(player) && !hasAnyLegalMove(player);
    }

    public void applyMove(Move move) {
        Piece piece = board.getPiece(move.getFrom());

        board.setPiece(move.getTo(), piece);
        board.setPiece(move.getFrom(), null);

        piece.setHasMoved(true);
    }

    public boolean playMove(Move move){
        if(gameOver){
            return false;
        }

        if (!isMoveValid(move)) {
            return false;
        }

        applyMove(move);
        board.recomputeAttackMaps();
        
        People playing = getCurrentPlayer();

        //en cas de pat
        People eliminatedPlayer = null;

        for (People player : players) {
            if (!player.equals(playing) && isCheckmate(player)) {
                winner = playing;
                gameOver = true;
                return true;
            } else if (!player.equals(playing) && isStalemate(player)) {
                eliminatedPlayer = player;
                break;
            }
        }

        if (eliminatedPlayer != null) {
            players.remove(eliminatedPlayer);
        }
        nextTurn();
        return true;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public People getWinner() {
        return winner;
    }
}
