package chess;

import java.util.Collection;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor team;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        team = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    private boolean isInCheck_copy(TeamColor teamColor, ChessBoard board) {
        ChessPosition king = findKing(teamColor);
        for (int i = 1; i <= 8; i++ ) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition check = new ChessPosition(i,j);
                if (board.getPiece(check) != null && board.getPiece(check).getTeamColor() != teamColor) {
                    Collection<ChessMove> moves = board.getPiece(check).pieceMoves(board, check);
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

        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        for (ChessMove move : moves) {
            ChessBoard base = board.boardCopy();
            base.addPiece(move.getEndPosition(), piece);
            if (isInCheck_copy(piece.getTeamColor(), base)) {


            }
        }
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }


    private ChessPosition findKing(TeamColor color) {
        ChessPosition king = null;
        for (int i = 1; i <= 8; i++ ) {
            for (int j = 1; j <= 8; j++ ) {
                ChessPosition check = new ChessPosition(i,j);
                if (board.getPiece(check) != null && board.getPiece(check).getPieceType() == ChessPiece.PieceType.KING && color == board.getPiece(check).getTeamColor() ) {
                    king = check;

                }
                }
            }
            return king;
            }

    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition king = findKing(teamColor);
        for (int i = 1; i <= 8; i++ ) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition check = new ChessPosition(i,j);
                if (board.getPiece(check) != null && board.getPiece(check).getTeamColor() != teamColor) {
                    Collection<ChessMove> moves = board.getPiece(check).pieceMoves(board, check);
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

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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
