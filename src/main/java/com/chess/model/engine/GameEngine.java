package com.chess.model.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.chess.factories.BoardFactory;
import com.chess.factories.PieceFactory;
import com.chess.model.Board;
import com.chess.model.Direction;
import com.chess.model.Move;
import com.chess.model.People;
import com.chess.model.Piece;
import com.chess.model.PieceType;
import com.chess.model.Position;

public class GameEngine {
    private final Board board;
    private final List<People> players;
    private final List<People> initialPlayers;
    private int currentPlayerId;
    private People winner = null;
    private boolean gameOver = false;
    private boolean botTurnInProgress = false;

    public GameEngine(Board board,List<People> players){
        this.board = board;
        this.players = new ArrayList<>(players);
        this.initialPlayers = new ArrayList<>(players);
        this.currentPlayerId = 0;
    }

    public Board getBoard() {
        return board;
    }

    public int getCurrentPlayerId() {
        System.out.println("Tour de : " + players.get(currentPlayerId));
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
        List<Move> possibleMoves = piece.getMovementStrategy().getPossibleMoves(move.getFrom(), board, piece);

        if(!possibleMoves.contains(move)) return false;

        Piece captured = board.getPiece(move.getTo());

        board.setPiece(move.getTo(), piece);
        board.setPiece(move.getFrom(), null);

        board.recomputeAttackMaps();

        boolean kingInCheck = isKingInCheck(piece.getOwner());

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

        if (piece == null) {
            return;
        }

        People currentPlayer = piece.getOwner();

        boolean castling = isCastlingMove(move);

        boolean enPassant = false;
        if (piece.getType() == PieceType.Pawn
                && board.getEnPassantTarget() != null
                && board.getEnPassantAllowedPlayer() != null
                && board.getEnPassantAllowedPlayer().equals(currentPlayer)) {

            if (move.getTo().equals(board.getEnPassantTarget())
                    && board.getPiece(move.getTo()) == null) {
                enPassant = true;
            }
        }

        Position rookFrom = null;
        Position rookTo = null;

        if (castling) {
            rookFrom = findCastlingRookPosition(move, piece);
            rookTo = board.getNeighborsDirection(move.getFrom(), move.getDirection());
        }

        boolean mustClearEnPassant =
                board.getEnPassantAllowedPlayer() != null
                && board.getEnPassantAllowedPlayer().equals(currentPlayer);

        board.setPiece(move.getTo(), piece);
        board.setPiece(move.getFrom(), null);
        piece.setHasMoved(true);

        if (piece.getType() == PieceType.Pawn && isPromotionSquare(move.getTo(), piece)) {
            board.setPromotionPendingPosition(move.getTo());
        }

        if (castling && rookFrom != null && rookTo != null) {
            Piece rook = board.getPiece(rookFrom);

            board.setPiece(rookTo, rook);
            board.setPiece(rookFrom, null);

            if (rook != null) {
                rook.setHasMoved(true);
            }
        }

        if (enPassant) {
            Position capturedPawnPos = board.getEnPassantCapturedPawnPosition();
            if (capturedPawnPos != null) {
                board.setPiece(capturedPawnPos, null);
            }
        }

        if (mustClearEnPassant) {
            board.setEnPassantTarget(null);
            board.setEnPassantCapturedPawnPosition(null);
            board.setEnPassantAllowedPlayer(null);
        }

        if (piece.getType() == PieceType.Pawn) {
            Position from = move.getFrom();
            Position middle = board.getNeighborsDirection(from, move.getDirection());

            if (middle != null) {
                Position doubleStep = board.getNeighborsDirection(middle, move.getDirection());

                if (doubleStep != null && doubleStep.equals(move.getTo())) {
                    board.setEnPassantTarget(middle);
                    board.setEnPassantCapturedPawnPosition(move.getTo());

                    //autorisation pour le joueur car sinon méthode d'oubli au prochaon déplacement de pièce pas adapté a trois joueur
                    int currentIndex = players.indexOf(currentPlayer);
                    int allowedIndex = (currentIndex + 2) % players.size();
                    board.setEnPassantAllowedPlayer(players.get(allowedIndex));
                }
            }
        }
    }

    public boolean playMove(Move move){
        if(gameOver){
            return false;
        }

        Piece piece = board.getPiece(move.getFrom());
        if (piece == null) {
            return false;
        }

        if (!piece.getOwner().equals(getCurrentPlayer())) {
            return false;
        }

        if (!isMoveValid(move)) {
            return false;
        }

        applyMove(move);
        board.recomputeAttackMaps();
        
        People playing = getCurrentPlayer();

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

    //detection du roque :

    public boolean isCastlingMove(Move move) {
        Piece piece = board.getPiece(move.getFrom());

        if (piece == null || piece.getType() != PieceType.King) {
            return false;
        }

        Position firstStep = board.getNeighborsDirection(move.getFrom(), move.getDirection());
        if (firstStep == null) return false;

        Position secondStep = board.getNeighborsDirection(firstStep, move.getDirection());
        if (secondStep == null) return false;

        return secondStep.equals(move.getTo());
    }

    public Position findCastlingRookPosition(Move move, Piece king) {
        Direction dir = move.getDirection();

        Position current = board.getNeighborsDirection(move.getFrom(), dir);

        while (current != null) {
            Piece piece = board.getPiece(current);

            if (piece != null) {
                if (piece.getType() == PieceType.Rook
                        && piece.getOwner().equals(king.getOwner())) {
                    return current;
                }

                return null;
            }

            current = board.getNeighborsDirection(current, dir);
        }

        return null;
    }

    //promotion 

    public boolean isPromotionSquare(Position position, Piece pawn) {
        Direction normalDir = Direction.UP;

        Position next;

        if (pawn.getStartTier() == position.getTiers()) {
            next = board.getNeighborsDirection(position, normalDir);
        } else {
            next = board.getNeighborsDirection(position, normalDir.switchDir());
        }

        return next == null;
    }

    public void promotePendingPawn(PieceType newType) {
        if (board.getPromotionPendingPosition()==null) return;

        Piece pawn = board.getPiece(board.getPromotionPendingPosition());
        if (pawn == null || pawn.getType() != PieceType.Pawn) return;
        int score;
        if(newType==PieceType.Rook){
            score = 5;
        }
        else if(newType==PieceType.Queen){
            score = 9;
        }
        else{
            score = 3;
        }
        Piece promoted = PieceFactory.createPiece(newType, pawn.getOwner(), pawn.getColor(), PieceFactory.getStrategies(), pawn.getStartTier(),score);
        promoted.setHasMoved(true);

        board.setPiece(board.getPromotionPendingPosition(), promoted);
        board.setPromotionPendingPosition(null);
    }

    //tour bot
    public boolean isBotTurnInProgress(){
        return botTurnInProgress;
    }

    public void setBotTurnInProgress(boolean botTurnInProgress) {
        this.botTurnInProgress = botTurnInProgress;
    }

    public void resetGame() {
        board.reset();

        players.clear();
        players.addAll(initialPlayers);

        BoardFactory.createPositions(board);
        BoardFactory.connectPositions(board, board.getPositions());
        BoardFactory.placeInitialPieces(
                board,
                board.getPositions(),
                initialPlayers.get(0),
                initialPlayers.get(1),
                initialPlayers.get(2)
        );

        board.recomputeAttackMaps();

        currentPlayerId = 0;
        winner = null;
        gameOver = false;
        botTurnInProgress = false;
    }
}
