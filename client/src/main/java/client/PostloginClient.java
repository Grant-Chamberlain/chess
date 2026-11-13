package client;

import chess.ChessBoard;
import chess.ChessGame;
import model.CreateGameRequest;
import model.GameSummary;
import model.JoinGameRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PostloginClient {
    private static final String INVALID_GAME = "That game number doesn't exist. Use list to see games";

    private final ServerFacade facade;
    private final Scanner scanner;
    private final String authToken;
    private List<GameSummary> games = new ArrayList<>();
    private boolean loggedOut = false;

    public PostloginClient(ServerFacade facade, Scanner scanner, String authToken) {
        this.facade = facade;
        this.scanner = scanner;
        this.authToken = authToken;
    }
    public String eval(String input) {
        String command = input.trim().toLowerCase();
        return switch (command) {
            case "logout" -> logout();
            case "create", "create game" -> createGame();
            case "list", "list games" -> listGames();
            case "play", "play game" -> playGame();
            case "observe", "observe game" -> observeGame();
            default -> help();
        };
    }

    public boolean isLoggedOut() {
        return loggedOut;
    }

    private String logout() {
        try {
            facade.logout(authToken);
            loggedOut = true;
            return "Logged out.";
        } catch (ResponseException e) {
            return "Logout failed " + e.getMessage();
        }
    }

    private String createGame() {
        String gameName= prompt("Game name: ");
        try {
            facade.createGame(new CreateGameRequest(gameName), authToken);
            return "Created game \"" + gameName + "\".";
        } catch (ResponseException e)  {
            return "Could not create game: " + e.getMessage();
        }
    }

    private String listGames() {
        try {
            refreshGames();
            return games.isEmpty() ? "No games yet. Use create to make one." : formatGames();
        } catch( ResponseException e) {
            return "Could not list games: " + e.getMessage();
        }
    }

    private String playGame() {
        try {
            GameSummary game = selectGame(prompt("Game number: "));
            if (game == null) {
                return INVALID_GAME;
            }
            String color = prompt("Color (white/black): ").toUpperCase();
            if (!color.equals("WHITE") && !color.equals("BLACK")) {
                return "Color must be white or black.";
            }
            facade.joinGame(new JoinGameRequest(color, game.gameID()), authToken);
            return "Joined \"" + game.gameName() + "\" as " + color.toLowerCase() + "." + "\n"
                    + boardPrintHelper(color);
        } catch (ResponseException e) {
            return "Could not join game: " + e.getMessage();
        }
    }

    private String observeGame() {
        try {
            GameSummary game = selectGame(prompt("Game number: "));
            if (game == null) {
                return INVALID_GAME;
            }
            return "Observing \"" + game.gameName() + "\"." + "\n" + boardPrintHelper("WHITE");
        } catch (ResponseException e) {
            return "Could not observe game: " + e.getMessage();
        }
    }

    private GameSummary selectGame(String gameText) throws ResponseException{
        if (games.isEmpty()) {
            refreshGames();
        }
        int number = parseNumber(gameText);
        if (number >= 1 && number <= games.size()) {
            return games.get(number-1);
        }
        return null;
    }

    private void refreshGames() throws ResponseException{
        games = new ArrayList<>(facade.listGames(authToken).games());
    }

    private String formatGames() {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < games.size(); i++) {
            GameSummary game = games.get(i);
            text.append(String.format("%d. %s (white; %s, black: %s)%n", i +1,
                game.gameName(), playerName(game.whiteUsername()), playerName(game.blackUsername())));
        }
        return text.toString().trim();
    }

    private String playerName(String username) {
        return username == null ? "open" : username;
    }

    private int parseNumber(String text) {
        try{
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    private String boardPrintHelper(String color) {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        return BoardPrinter.draw(board, ChessGame.TeamColor.valueOf(color));

    }





    private String help() {
        return """
                Available commands:
                  create  - create a new game
                  list    - list all games
                  play    - join a game as white or black
                  observe - watch a game
                  logout  - log out
                  help    - show this message""";
    }

    private String prompt(String question) {
        System.out.print(question);
        return scanner.nextLine().trim();
    }

}
