package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board;

    public ChessBoard() {

        board = new ChessPiece[9][9];


    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int row = position.getRow();
        int col = position.getColumn();

        board[row][col] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        return board[row][col];

    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        ChessPiece.PieceType[] pieces = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};

        for (int i=1; i<=8; i++) {
            ChessPosition position = new ChessPosition(2, i);
            ChessPiece pawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            addPiece(position, pawn);
        }
        for (int i=1; i<=8; i++) {
            ChessPosition position = new ChessPosition(7, i);
            ChessPiece pawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            addPiece(position, pawn);
        }
        for (int i=1; i<=8; i++) {
            ChessPiece.PieceType P = pieces[i];
            ChessPosition position1 = new ChessPosition(1, i);
            ChessPosition position2 = new ChessPosition(8, i);
            ChessPiece piece1 = new ChessPiece(ChessGame.TeamColor.WHITE, P);
            ChessPiece piece2 = new ChessPiece(ChessGame.TeamColor.BLACK, P);
            addPiece(position1, piece1);
            addPiece(position2, piece2);
        }
    }
}
