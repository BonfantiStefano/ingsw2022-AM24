package it.polimi.ingsw.server;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class ServerMain used to start the server on a port, is possible to choose a port or to use the default configuration
 *
 * @author Bonfanti Stefano
 */
public class ServerMain {
    private static final int DEFAULT_PORT = 1234;
    private static final int MIN_PORT = 1024;
    private static final int MAX_PORT = 65535;

    /**
     * Method main is used to start the server side.
     * @param args of type String[]
     */
    public static void main(String[] args) {
        int port= -1;
        if(args.length == 0) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to the Eriantys server");
            System.out.println("Would you like to use the default port?");
            String config = scanner.nextLine();
            if (config.equalsIgnoreCase("yes")) {
                port = DEFAULT_PORT;
            } else {
                System.out.println("Insert a value between " + MIN_PORT + " and " + MAX_PORT + ":");
                try {
                    port = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Numeric format requested");
                    port = DEFAULT_PORT;
                } catch (NoSuchElementException exception) {
                    System.exit(-1);
                    port = -1;
                }
                while (port < MIN_PORT || port > MAX_PORT) {
                    System.out.println("Insert a value between " + MIN_PORT + " and " + MAX_PORT + ":");
                    try {
                        port = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Numeric format requested, server starts on the default port");
                        port = DEFAULT_PORT;
                    } catch (NoSuchElementException exception) {
                        System.exit(-1);
                    }
                }
            }
        } else {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException exception) {
                System.err.println("Numeric format requested, application will now close...");
                System.exit(-1);
            } catch (NoSuchElementException exception) {
                System.exit(-1);
            }
            if(port < MIN_PORT || port > MAX_PORT) {
                System.out.println("Use a port between " + MIN_PORT + " and " + MAX_PORT);
                System.exit(-1);
            }
            System.out.println("Welcome to the Eriantys server");
        }
        Server server = new Server();
        server.startServer(port);
    }

}
