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
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

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

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;



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
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> moves = new ArrayList<>();
        switch (this.getPieceType()) {
            case ROOK:
                int[][] directions = {{1,0},{0,1},{-1,0},{0,-1}};
                for (int[] dir :directions) {
                    int step = 1;
                    while (true) {
                        int row = myPosition.getRow() + dir[0] * step;
                        int col = myPosition.getColumn() + dir[1] * step;

                        if (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
                            ChessPosition p = new ChessPosition(row, col);
                            ChessPiece val = board.getPiece(p);

                            if (val == null) {
                                ChessMove M = new ChessMove(myPosition, p, null);
                                moves.add(M);
                                step++;
                            } else if (val.getTeamColor() != this.getTeamColor()) {
                                ChessMove M = new ChessMove(myPosition, p, null);
                                moves.add(M);
                                break;
                            } else break;
                        } else break;
                    }
                }
                break;
            case BISHOP:
                int[][] bishop_directions = {{1,1},{-1,1},{-1,-1},{1,-1}};
                for (int[] dir : bishop_directions) {
                    int step = 1;
                    while (true) {
                        int row = myPosition.getRow() + dir[0] * step;
                        int col = myPosition.getColumn() + dir[1] * step;

                        if (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
                            ChessPosition p = new ChessPosition(row, col);
                            ChessPiece val = board.getPiece(p);

                            if (val == null) {
                                ChessMove M = new ChessMove(myPosition, p, null);
                                moves.add(M);
                                step++;
                            } else if (val.getTeamColor() != this.getTeamColor()) {
                                ChessMove M = new ChessMove(myPosition, p, null);
                                moves.add(M);
                                break;
                            } else break;
                        } else break;
                    }
                }
                break;
            case QUEEN:
                int[][] queen_directions = {{1,1},{-1,1},{-1,-1},{1,-1},{1,0},{0,1},{-1,0},{0,-1}};
                for (int[] dir : queen_directions) {
                    int step = 1;
                    while (true) {
                        int row = myPosition.getRow() + dir[0] * step;
                        int col = myPosition.getColumn() + dir[1] * step;

                        if (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
                            ChessPosition p = new ChessPosition(row, col);
                            ChessPiece val = board.getPiece(p);

                            if (val == null) {
                                ChessMove M = new ChessMove(myPosition, p, null);
                                moves.add(M);
                                step++;
                            } else if (val.getTeamColor() != this.getTeamColor()) {
                                ChessMove M = new ChessMove(myPosition, p, null);
                                moves.add(M);
                                break;
                            } else break;
                        } else break;
                    }
                }
                break;
            case KING:
                int[][] moveset = {{1, 1}, {0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, -1}, {-1, 1}, {-1, -1}};
                for (int[] move : moveset) {
                    int row = myPosition.getRow() + move[0];
                    int col = myPosition.getColumn()+ move[1];
                    if (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
                        ChessPosition p = new ChessPosition(row, col);
                        ChessPiece val = board.getPiece(p);
                        if (val == null) {
                            ChessMove M = new ChessMove(myPosition, p, null);
                            moves.add(M);
                        } else if (val.getTeamColor() != this.getTeamColor()) {
                            ChessMove M = new ChessMove(myPosition, p, null);
                            moves.add(M);

                        }
                    }

                }
                break;
            case PAWN:
                PieceType[] promotions = {ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.BISHOP};
                if (this.getTeamColor() == ChessGame.TeamColor.WHITE) {

                    ChessPosition row1 = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                    ChessPiece r1 = board.getPiece(row1);

                    if (r1 == null) {
                        if (row1.getRow() == 8) {
                            for (PieceType p : promotions) {
                                ChessMove pawn_1 = new ChessMove(myPosition, row1, p);
                                moves.add(pawn_1);
                            }
                        } else {
                            ChessMove pawn_1 = new ChessMove(myPosition, row1, null);
                            moves.add(pawn_1);
                        if (myPosition.getRow() == 2) {
                            ChessPosition row2 = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                            ChessPiece r2 = board.getPiece(row2);
                            if (r2 == null) {
                                ChessMove pawn_2 = new ChessMove(myPosition, row2, null);
                                moves.add(pawn_2);
                                }
                            }
                        }
                    }
                    if (myPosition.getColumn() > 1 && myPosition.getColumn() <= 8) {
                        ChessPosition capture_left = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                        ChessPiece l = board.getPiece(capture_left);
                        if (l != null && l.getTeamColor() != this.getTeamColor()) {
                            if (row1.getRow() == 8) {
                                for (PieceType p : promotions) {
                                    ChessMove pawn_1 = new ChessMove(myPosition, capture_left, p);
                                    moves.add(pawn_1);
                                }
                            } else {
                                    ChessMove capture = new ChessMove(myPosition, capture_left, null);
                                    moves.add(capture);
                                }
                        }
                    }
                    if (myPosition.getColumn() >= 1 && myPosition.getColumn() < 8) {
                        ChessPosition capture_right = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                        ChessPiece r = board.getPiece(capture_right);
                        if (r != null && r.getTeamColor() != this.getTeamColor()) {
                            if (row1.getRow() == 8) {
                                for (PieceType p : promotions) {
                                    ChessMove pawn_1 = new ChessMove(myPosition, capture_right, p);
                                    moves.add(pawn_1);
                                }
                            } else {
                                ChessMove capture = new ChessMove(myPosition, capture_right, null);
                                moves.add(capture);
                            }
                        }
                    }

                } else {





                    ChessPosition row1 = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                    ChessPiece r1 = board.getPiece(row1);

                    if (r1 == null) {
                        if (row1.getRow() == 1) {
                            for (PieceType p : promotions) {
                                ChessMove pawn_1 = new ChessMove(myPosition, row1, p);
                                moves.add(pawn_1);
                            }
                        } else {
                            ChessMove pawn_1 = new ChessMove(myPosition, row1, null);
                            moves.add(pawn_1);
                            if (myPosition.getRow() == 7) {
                                ChessPosition row2 = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                                ChessPiece r2 = board.getPiece(row2);
                                if (r2 == null) {
                                    ChessMove pawn_2 = new ChessMove(myPosition, row2, null);
                                    moves.add(pawn_2);
                                }
                            }
                        }
                    }
                    if (myPosition.getColumn() >= 1 && myPosition.getColumn() < 8) {
                        ChessPosition capture_left = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                        ChessPiece l = board.getPiece(capture_left);
                        if (l != null && l.getTeamColor() != this.getTeamColor()) {
                            if (row1.getRow() == 1) {
                                for (PieceType p : promotions) {
                                    ChessMove pawn_1 = new ChessMove(myPosition, capture_left, p);
                                    moves.add(pawn_1);
                                }
                            } else {
                                ChessMove capture = new ChessMove(myPosition, capture_left, null);
                                moves.add(capture);
                            }
                        }
                    }
                    if (myPosition.getColumn() > 1 && myPosition.getColumn() <= 8) {
                        ChessPosition capture_right = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                        ChessPiece r = board.getPiece(capture_right);
                        if (r != null && r.getTeamColor() != this.getTeamColor()) {
                            if (row1.getRow() == 1) {
                                for (PieceType p : promotions) {
                                    ChessMove pawn_1 = new ChessMove(myPosition, capture_right, p);
                                    moves.add(pawn_1);
                                }
                            } else {
                                ChessMove capture = new ChessMove(myPosition, capture_right, null);
                                moves.add(capture);
                            }
                        }
                    }
                }
                break;
            case KNIGHT:
                int[][] knightmoves = {{-2,1},{-1,2},{1,2},{2,1},{2,-1},{1,-2},{-2,-1},{-1,-2}};
                for (int[] move : knightmoves) {
                    int row = myPosition.getRow() + move[0];
                    int col = myPosition.getColumn()+ move[1];
                    if (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
                        ChessPosition p = new ChessPosition(row, col);
                        ChessPiece val = board.getPiece(p);
                        if (val == null) {
                            ChessMove M = new ChessMove(myPosition, p, null);
                            moves.add(M);
                        } else if (val.getTeamColor() != this.getTeamColor()) {
                            ChessMove M = new ChessMove(myPosition, p, null);
                            moves.add(M);

                        }
                    }

                }
                break;
        }

        return moves;
    }
}
