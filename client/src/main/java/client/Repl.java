package client;

import java.util.Scanner;

public class Repl {
    private final ServerFacade facade;
    private final Scanner scanner = new Scanner(System.in);
    private final PreloginClient prelogin;
    private PostloginClient postlogin;

    public Repl(int port) {
        facade = new ServerFacade(port);
        prelogin = new PreloginClient(facade, scanner);

    }

    public void run() {
        System.out.println("Welcome to 240 Chess. Type Help to get started.");
        boolean running = true;
        while (running) {
            if (postlogin == null) {
                running = preloginStep();
            } else {
                postloginStep();
            }
        }
    }
    private boolean preloginStep() {
        System.out.print("[LOGGED_OUT] >>> ");
        String result = prelogin.eval(scanner.nextLine());
        if (result.equals(PreloginClient.QUIT)) {
            return false;
        }
        System.out.println(result);
        if (prelogin.isLoggedIn()) {
            postlogin = new PostloginClient(facade, scanner, prelogin.getAuthToken());
        }
        return true;

    }

    private void postloginStep() {
        System.out.print("[LOGGED_IN] >>> ");
        System.out.println(postlogin.eval(scanner.nextLine()));
        if (postlogin.isLoggedOut()) {
            postlogin = null;
            prelogin.clearSession();
        }
    }
}
