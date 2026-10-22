package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private static final PieceType[] PROMOTION_TYPES = {PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT};
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;
    private static final int[][] ROOK_DIRECTIONS = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    private static final int[][] BISHOP_DIRECTIONS = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}};
    private static final int[][] QUEEN_DIRECTIONS = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}, {1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    private static final int[][] KING_DIRECTIONS = {{1, 1}, {0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, -1}, {-1, 1}, {-1, -1}};
    private static final int[][] KNIGHT_DIRECTIONS = {{-2, 1}, {-1, 2}, {1, 2}, {2, 1}, {2, -1}, {1, -2}, {-2, -1}, {-1, -2}};

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;


    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    private static boolean isOnBoard(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> moves = new ArrayList<>();

        switch (this.getPieceType()) {
            case ROOK:
                moves.addAll(slideMoves(board, myPosition, ROOK_DIRECTIONS));
                break;
            case BISHOP:
                moves.addAll(slideMoves(board, myPosition, BISHOP_DIRECTIONS));
                break;
            case QUEEN:

                moves.addAll(slideMoves(board, myPosition, QUEEN_DIRECTIONS));
                break;
            case KING:
                moves.addAll(stepMoves(board, myPosition, KING_DIRECTIONS));
                break;
            case PAWN:
                moves.addAll(pawnMoves(board, myPosition));
                break;
            case KNIGHT:
                moves.addAll(stepMoves(board, myPosition, KNIGHT_DIRECTIONS));
                break;
        }

        return moves;
    }

    private Collection<ChessMove> stepMoves(ChessBoard board, ChessPosition myPosition, int[][] moveset) {
        Collection<ChessMove> moves = new ArrayList<>();

        for (int[] move : moveset) {
            int row = myPosition.getRow() + move[0];
            int col = myPosition.getColumn() + move[1];
            if (isOnBoard(row, col)) {
                ChessPosition chessPosition = new ChessPosition(row, col);
                ChessPiece chessPiece = board.getPiece(chessPosition);
                if (chessPiece == null) {
                    ChessMove chessMove = new ChessMove(myPosition, chessPosition, null);
                    moves.add(chessMove);
                } else if (chessPiece.getTeamColor() != this.getTeamColor()) {
                    ChessMove chessMove = new ChessMove(myPosition, chessPosition, null);
                    moves.add(chessMove);

                }
            }

        }

        return moves;
    }

    private Collection<ChessMove> slideMoves(ChessBoard board, ChessPosition myPosition, int[][] moveset) {
        Collection<ChessMove> moves = new ArrayList<>();
        for (int[] dir : moveset) {
            int step = 1;
            while (true) {
                int row = myPosition.getRow() + dir[0] * step;
                int col = myPosition.getColumn() + dir[1] * step;

                if (isOnBoard(row, col)) {
                    ChessPosition chessPosition = new ChessPosition(row, col);
                    ChessPiece chessPiece = board.getPiece(chessPosition);

                    if (chessPiece == null) {
                        ChessMove chessMove = new ChessMove(myPosition, chessPosition, null);
                        moves.add(chessMove);
                        step++;
                    } else if (chessPiece.getTeamColor() != this.getTeamColor()) {
                        ChessMove chessMove = new ChessMove(myPosition, chessPosition, null);
                        moves.add(chessMove);
                        break;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return moves;
    }

    private void addPawnMove(Collection<ChessMove> moves, ChessPosition start, ChessPosition end, int promotionRow) {
        if (end.getRow() == promotionRow) {
            for (PieceType type : PROMOTION_TYPES) {
                moves.add(new ChessMove(start, end, type));
            }
        } else {
            moves.add(new ChessMove(start, end, null));
        }
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        boolean isWhite = this.getTeamColor() == ChessGame.TeamColor.WHITE;
        int direction = isWhite ? 1 : -1;
        int startRow = isWhite ? 2 : 7;
        int promotionRow = isWhite ? 8 : 1;
        int row = position.getRow();
        int col = position.getColumn();

        ChessPosition oneStep = new ChessPosition(row + direction, col);
        if (board.getPiece(oneStep) == null) {
            addPawnMove(moves, position, oneStep, promotionRow);
            if (row == startRow) {
                ChessPosition twoStep = new ChessPosition(row + 2 * direction, col);
                if (board.getPiece(twoStep) == null) {
                    moves.add(new ChessMove(position, twoStep, null));
                }
            }
        }

        for (int colOffset : new int[]{-1, 1}) {
            int captureCol = col + colOffset;
            if (captureCol >= 1 && captureCol <= 8) {
                ChessPosition potentialMove = new ChessPosition(row + direction, captureCol);
                ChessPiece spotCheck = board.getPiece(potentialMove);
                if (spotCheck != null && spotCheck.getTeamColor() != this.getTeamColor()) {
                    addPawnMove(moves, position, potentialMove, promotionRow);
                }
            }
        }
        return moves;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }
}
