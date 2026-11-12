package client;

import service.GameSummary;

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


}
