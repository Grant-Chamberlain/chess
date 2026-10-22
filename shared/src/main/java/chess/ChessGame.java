package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamColor;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        teamColor = TeamColor.WHITE;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame otherGame = (ChessGame) o;
        return Objects.equals(board, otherGame.board) && teamColor == otherGame.teamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamColor);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamColor;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }



    private ChessPosition findKingOnBoard(TeamColor teamColor, ChessBoard board) {
        ChessPosition king = null;
        for (int i = 1; i <= 8; i++ ) {
            for (int j = 1; j <= 8; j++ ) {
                ChessPosition chessPosition = new ChessPosition(i,j);
                if (board.getPiece(chessPosition) != null && board.getPiece(chessPosition).getPieceType() == ChessPiece.PieceType.KING && teamColor == board.getPiece(chessPosition).getTeamColor() ) {
                    king = chessPosition;

                }
            }
        }
        return king;
    }
    private boolean isInCheckOnBoard(TeamColor teamColor, ChessBoard board) {
        ChessPosition king = findKingOnBoard(teamColor, board);
        for (int i = 1; i <= 8; i++ ) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition chessPosition = new ChessPosition(i,j);
                if (board.getPiece(chessPosition) != null && board.getPiece(chessPosition).getTeamColor() != teamColor) {
                    Collection<ChessMove> moves = board.getPiece(chessPosition).pieceMoves(board, chessPosition);
                    for (ChessMove move : moves) {
                        if (move.getEndPosition().equals(king)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;

    }


    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        List<ChessMove> validMoves = new ArrayList<>();
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        for (ChessMove move : moves) {
            ChessBoard base = board.boardCopy();
            base.addPiece(move.getEndPosition(), piece);
            base.addPiece(move.getStartPosition(), null);
            if (!isInCheckOnBoard(piece.getTeamColor(), base)) {

                validMoves.add(move);

            }
        }
        return validMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        Collection<ChessMove> legalMoves = validMoves(move.getStartPosition());
        if (legalMoves == null || !legalMoves.contains(move) || piece.getTeamColor() != teamColor) {
            throw new InvalidMoveException();
        }

        if (move.getPromotionPiece() != null) {
            piece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        }
        board.addPiece(move.getEndPosition(), piece);
        board.addPiece(move.getStartPosition(), null);
        if (teamColor == TeamColor.BLACK) {
            teamColor = TeamColor.WHITE;
        } else {
            teamColor = TeamColor.BLACK;
        }
    }


    private ChessPosition findKing(TeamColor color) {
        return findKingOnBoard(color, this.board);
    }

    public boolean isInCheck(TeamColor teamColor) {
        return isInCheckOnBoard(teamColor, this.board);

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        return hasNoValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        return hasNoValidMoves(teamColor);
    }
    private boolean hasNoValidMoves(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition chessPosition = new ChessPosition(i, j);
                if (board.getPiece(chessPosition) != null && board.getPiece(chessPosition).getTeamColor() == teamColor) {
                    if (!validMoves(chessPosition).isEmpty()) {
                        return false;
                    }
                }
            }

        }
        return true;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
