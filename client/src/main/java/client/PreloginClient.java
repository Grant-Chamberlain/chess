package client;

import service.LoginRequest;
import service.RegisterRequest;
import service.RegisterResult;
import service.LoginResult;

import java.util.Locale;
import java.util.Scanner;

public class PreloginClient {
    public static final String quit = "quit";

    private final ServerFacade facade;
    private final Scanner scanner;
    private String username;
    private String authToken;

    public PreloginClient(ServerFacade facade, Scanner scanner) {
        this.facade = facade;
        this.scanner = scanner;
    }

    public String eval(String input) {
        String command = input.trim().toLowerCase();
        return switch (command) {
            case "register" -> register();
            case "login" -> login();
            case quit -> quit;
            default -> help();
        };
    }

    public boolean isLoggedIn() {
        return authToken != null;
    }

    public String getUsername() {
        return username;
    }
    public String getAuthToken() {
        return authToken;
    }
    public void clearSession() {
        username = null;
        authToken = null;
    }
    private String register() {
        String newUsername = prompt("Username: ");
        String passwrod = prompt("Password: ");
        String email = prompt("Email: ");
        try {
            RegisterResult result =
                    facade.register(new RegisterRequest(newUsername, passwrod, email));
            return startSession(result.username(), result.authToken());
        } catch (ResponseException e) {
            return "Login failed:" + e.getMessage();
        }

    }
    private String login() {
        String existingusername = prompt("Username: ");
        String password = prompt("Password: ");
        try {
            LoginResult result = facade.login(new LoginRequest(existingusername, password));
            return startSession(result.username(), result.authToken());
        } catch (ResponseException e) {
            return "Login failed: " + e.getMessage();
        }
    }

    private String startSession(String sessionUsername, String sessionToken) {
        username = sessionUsername;
        authToken = sessionToken;
        return "Logged in as " + username + ".";

    }

    private String help() {
        return """
                 Available commands:
                                  register - create a new account
                                  login    - log in to an existing account
                                  quit     - exit the program
                                  help     - show this message""";
    }
    private String prompt(String question) {
        System.out.print(question);
        return scanner.nextLine().trim();
    }
}
