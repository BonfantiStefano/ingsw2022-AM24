package it.polimi.ingsw;

import it.polimi.ingsw.client.CLIView.CLI;
import it.polimi.ingsw.client.GUIView.GUI;
import it.polimi.ingsw.server.ServerMain;

import java.util.Scanner;

/**
 * Class Eriantys is the main class of whole game, where you can choose if you between client and server.
 *
 * @author Baratto Marco, Bonfanti Stefano, Chyzheuskaya Hanna.
 */
public class Eriantys {
    /**
     * Method main selects Client or Server based on the arguments provided.
     *
     * @param args of type String[]
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Hi! Welcome to Eriantys!\nWhat do you want to launch?");
            System.out.println("0. Server\n1. Client");
            System.out.println("Type the number of the desired option!");
            Scanner scanner = new Scanner(System.in);
            int input = 0;
            int client = 0;
            try {
                input = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException exception) {
                System.err.println("Numeric format requested, application will now close...");
                System.exit(-1);
            }
            switch (input) {
                case 0 -> ServerMain.main(args);
                case 1 -> {
                    System.out.println("0. CLI\n1. GUI");
                    System.out.println("Type the number of the interface you want to play with!");
                    try {
                        client = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException exception) {
                        System.err.println("Numeric format requested, application will now close...");
                        System.exit(-1);
                    }
                    switch (client) {
                        case 0 -> CLI.main(args);
                        case 1 -> {
                            System.out.println("The Application is starting...");
                            GUI.main(null);
                        }
                        default -> System.err.println("Invalid argument, please run the executable again");
                    }
                }
                default -> System.err.println("Invalid argument, please run the executable again with one of these options:\n0.server\n1.client");
            }
        } else {
            switch (args[0]) {
                case "-c" -> CLI.main(args);
                case "-g" -> GUI.main(null);
                case "-s" -> ServerMain.main(args);
                default -> System.err.println("Invalid argument, please re-run the executable again.");
            }
        }
    }
}
