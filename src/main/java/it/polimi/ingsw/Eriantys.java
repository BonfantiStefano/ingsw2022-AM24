package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.ServerMain;

import java.util.InputMismatchException;
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
    public static void main(String[] args){
        System.out.println("Hi! Welcome to Eriantys!\nWhat do you want to launch?");
        System.out.println("0. Server\n1. Client");
        System.out.println("Type the number of the desired option!");
        Scanner scanner = new Scanner(System.in);
        int input = 0;
        int client = 0;
        try {
            input = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("Numeric format requested, application will now close...");
            System.exit(-1);
        }
        switch (input) {
            case 0 -> ServerMain.main(null);
            case 1 -> {
                System.out.println("0. CLI\n1. GUI");
                System.out.println("Type the number of the desired option!");
                try {
                    client = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.err.println("Numeric format requested, application will now close...");
                    System.exit(-1);
                }
                switch (client) {
                    case 0 -> Client.main(null);
                    case 1 -> System.out.println("GUI non disponibile");//Gui.start();
                    default -> System.err.println("Invalid argument, please run the executable again");
                }
            }
            default -> System.err.println("Invalid argument, please run the executable again with one of these options:\n0.server\n1.client");
        }
    }
}
