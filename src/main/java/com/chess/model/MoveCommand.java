package com.chess.model;

import com.chess.model.engine.GameEngine;

public class MoveCommand implements Command {
    private final GameEngine engine;
    private final Move move;

    private Piece movedPiece;
    private Piece capturedPiece;
    private boolean oldHasMoved;

    private int oldCurrentPlayerId;
    private boolean oldGameOver;
    private People oldWinner;

    private Position oldEnPassantTarget;
    private People oldEnPassantAllowedPlayer;
    private Position oldEnPassantCapturedPawnPos;
    private Position oldPromotionPendingPosition;

    private boolean castling;
    private Position rookFrom;
    private Position rookTo;
    private Piece rookPiece;
    private boolean oldRookHasMoved;

    private boolean enPassant;
    private Position enPassantCapturedPosition;
    private Piece enPassantCapturedPiece;

    public MoveCommand(GameEngine engine, Move move) {
        this.engine = engine;
        this.move = move;
    }

    @Override
    public boolean execute() {
        Board board = engine.getBoard();

        movedPiece = board.getPiece(move.getFrom());
        capturedPiece = board.getPiece(move.getTo());

        if (movedPiece == null) return false;
        if (!engine.isMoveValid(move)) return false;

        oldHasMoved = movedPiece.getHasMoved();
        oldCurrentPlayerId = engine.getCurrentPlayerId();
        oldGameOver = engine.isGameOver();
        oldWinner = engine.getWinner();

        oldEnPassantTarget = board.getEnPassantTarget();
        oldEnPassantAllowedPlayer = board.getEnPassantAllowedPlayer();
        oldEnPassantCapturedPawnPos = board.getEnPassantCapturedPawnPosition();
        oldPromotionPendingPosition = board.getPromotionPendingPosition();

        castling = engine.isCastlingMove(move);

        if (castling) {
            rookFrom = engine.findCastlingRookPosition(move, movedPiece);
            rookTo = board.getNeighborsDirection(move.getFrom(), move.getDirection());

            if (rookFrom != null) {
                rookPiece = board.getPiece(rookFrom);

                if (rookPiece != null) {
                    oldRookHasMoved = rookPiece.getHasMoved();
                }
            }
        }

        enPassant =
                movedPiece.getType() == PieceType.Pawn
                && board.getEnPassantTarget() != null
                && board.getEnPassantAllowedPlayer() != null
                && board.getEnPassantAllowedPlayer().equals(movedPiece.getOwner())
                && move.getTo().equals(board.getEnPassantTarget())
                && board.getPiece(move.getTo()) == null;

        if (enPassant) {
            enPassantCapturedPosition = board.getEnPassantCapturedPawnPosition();

            if (enPassantCapturedPosition != null) {
                enPassantCapturedPiece = board.getPiece(enPassantCapturedPosition);
            }
        }

        engine.applyMove(move);
        if (board.hasPendingPromotion()) {
            engine.promotePendingPawn(PieceType.Queen);
        }
        board.recomputeAttackMaps();
        engine.nextTurn();

        return true;
    }

    @Override
    public void undo() {
        Board board = engine.getBoard();

        board.setPiece(move.getFrom(), movedPiece);
        board.setPiece(move.getTo(), capturedPiece);

        if (movedPiece != null) {
            movedPiece.setHasMoved(oldHasMoved);
        }

        if (castling && rookFrom != null && rookTo != null) {
            board.setPiece(rookFrom, rookPiece);
            board.setPiece(rookTo, null);

            if (rookPiece != null) {
                rookPiece.setHasMoved(oldRookHasMoved);
            }
        }

        if (enPassant && enPassantCapturedPosition != null) {
            board.setPiece(enPassantCapturedPosition, enPassantCapturedPiece);
        }

        board.setEnPassantTarget(oldEnPassantTarget);
        board.setEnPassantAllowedPlayer(oldEnPassantAllowedPlayer);
        board.setEnPassantCapturedPawnPosition(oldEnPassantCapturedPawnPos);
        board.setPromotionPendingPosition(oldPromotionPendingPosition);

        engine.setCurrentPlayerId(oldCurrentPlayerId);
        engine.setGameOver(oldGameOver);
        engine.setWinner(oldWinner);

        board.recomputeAttackMaps();
    }
}
