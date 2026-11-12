package client;

import java.util.Scanner;

public class Repl {
    private final Scanner scanner = new Scanner(System.in);
    private final PreloginClient prelogin;

    public Repl(int port) {
        ServerFacade facade = new ServerFacade(port);
        prelogin = new PreloginClient(facade, scanner);
    }

    public void run() {
        System.out.println("Welcome to 240 Chess. Type Help to get started.");
        String result = "";
        while (!result.equals(PreloginClient.quit)) {
            System.out.print("[LOGGED_OUT] >>> ");
            result = prelogin.eval(scanner.nextLine());
            if (!result.equals(PreloginClient.quit)) {
                System.out.println(result);
            }
        }
    }
}
