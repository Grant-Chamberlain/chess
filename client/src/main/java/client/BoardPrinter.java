package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class BoardPrinter {
    private static final int BOARD_SIZE = 8;
    private static final String COLUMN_LETTERS = "abcdefgh";
    private static final String LIGHT_SQUARE = SET_BG_COLOR_LIGHT_GREY;
    private static final String DARK_SQUARE = SET_BG_COLOR_DARK_GREY;
    private static final String BORDER_STYLE = SET_BG_COLOR_BLACK + SET_TEXT_COLOR_WHITE;
    private static final String WHITE_TEAM_TEXT = SET_TEXT_COLOR_WHITE;
    private static final String BLACK_TEAM_TEXT = SET_TEXT_COLOR_RED;

    public static String draw(ChessBoard board, ChessGame.TeamColor perspective) {
        boolean whiteAtBottom = perspective != ChessGame.TeamColor.BLACK;
        StringBuilder text = new StringBuilder();
        text.append(columnLabels(whiteAtBottom));
        for (int i = 0; i < BOARD_SIZE; i++) {
            int row = whiteAtBottom ? BOARD_SIZE - i : i + 1;
            text.append(rowText(board, row, whiteAtBottom));
        }
        text.append(columnLabels(whiteAtBottom));
        return text.toString();
    }

    private static String columnLabels(boolean whiteAtBottom) {
        StringBuilder line = new StringBuilder(BORDER_STYLE).append("   ");
        for (int i = 0; i < BOARD_SIZE; i++) {
            int col = whiteAtBottom ? i + 1 : BOARD_SIZE - i;
            line.append("  ").append(COLUMN_LETTERS.charAt(col - 1)).append(" ");
        }
        line.append("   ").append(RESET_BG_COLOR).append(RESET_TEXT_COLOR);
        return line.append(System.lineSeparator()).toString();
    }

    private static String rowText(ChessBoard board, int row, boolean whiteAtBottom) {
        StringBuilder line = new StringBuilder(rowLabel(row));
        for (int i = 0; i < BOARD_SIZE; i++) {
            int col = whiteAtBottom ? i + 1 : BOARD_SIZE - i;
            ChessPiece piece = board.getPiece(new ChessPosition(row, col));
            line.append(squareText(piece, row, col));
        }
        return line.append(rowLabel(row)).append(System.lineSeparator()).toString();
    }

    private static String rowLabel(int row) {
        return BORDER_STYLE + " " + row + " " + RESET_BG_COLOR + RESET_TEXT_COLOR;
    }

    private static String squareText(ChessPiece piece, int row, int col) {
        String background = (row + col) % 2 == 0 ? DARK_SQUARE : LIGHT_SQUARE;
        return background + pieceText(piece) + RESET_BG_COLOR + RESET_TEXT_COLOR;
    }

    private static String pieceText(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }
        boolean isWhite = piece.getTeamColor() == ChessGame.TeamColor.WHITE;
        String textColor = isWhite ? WHITE_TEAM_TEXT : BLACK_TEAM_TEXT;
        return textColor + symbol(piece.getPieceType(), isWhite);
    }

    private static String symbol(ChessPiece.PieceType type, boolean isWhite) {
        return switch (type) {
            case KING -> isWhite ? WHITE_KING : BLACK_KING;
            case QUEEN -> isWhite ? WHITE_QUEEN : BLACK_QUEEN;
            case BISHOP -> isWhite ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> isWhite ? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK -> isWhite ? WHITE_ROOK : BLACK_ROOK;
            case PAWN -> isWhite ? WHITE_PAWN : BLACK_PAWN;
        };
    }
}
