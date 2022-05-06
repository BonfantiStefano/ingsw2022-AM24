package it.polimi.ingsw.server;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ServerMain {
    private static final int DEFAULT_PORT = 1234;
    private static final int MIN_PORT = 1024;
    private static final int MAX_PORT = 65535;

    public static void main(String[] args) {
        int port;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Eriantys server");
        System.out.println("Would you like to use the default port?");
        String config = scanner.nextLine();
        if(config.equalsIgnoreCase("yes")){
            port = DEFAULT_PORT;
        } else {
            System.out.println("Insert a value between " + MIN_PORT + " e " + MAX_PORT + ":");
            try {
                port = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Numeric format requested");
                port = DEFAULT_PORT;
            }
            while(port < MIN_PORT || port > MAX_PORT) {
                System.out.println("Insert a value between " + MIN_PORT + " e " + MAX_PORT + ":");
                try {
                    port = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Numeric format requested");
                    port = DEFAULT_PORT;
                }
            }
        }
        Server server = new Server();
        server.startServer(port);
    }

}
