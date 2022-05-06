package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.CLIView.CLI;
import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.server.answer.Answer;
import it.polimi.ingsw.server.answer.Error;
import it.polimi.ingsw.server.answer.Ping;
import it.polimi.ingsw.server.virtualview.VirtualView;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

//Not final, work in progress
//TODO implementare timer che aspetta per un intervallo un messaggio dal server dopodichè dice che il server è stato disconnesso e si scollega
public class Client {
    private ObjectOutputStream os;
    private ObjectInputStream is;
    private boolean active;
    private final Scanner scanner;
    private final CLI cli;

    public static void main(String[] args) {
        Scanner initialScanner = new Scanner(System.in);
        System.out.println("Enter IP");
        String ip = initialScanner.nextLine();
        System.out.println("IP is: " + ip);
        System.out.println("Enter port");
        int port = initialScanner.nextInt();
        System.out.println("Port is: "+ port);
        Client c = new Client();
        c.startClient(ip, port);
    }

    public Client() {
        active = false;
        scanner = new Scanner(System.in);
        cli = new CLI();
    }

    public void startClient(String ip, int port) {
        active = true;
        try {
            //creazione del socket
            try (Socket socket = new Socket(ip, port)) {
                System.out.println("Connection established");
                //creazione degli stream
                try {
                    os = new ObjectOutputStream(socket.getOutputStream());
                    os.flush();
                    is = new ObjectInputStream(socket.getInputStream());
                } catch (IOException e) {
                    System.out.println("Error during initialization of the client!");
                    System.exit(0);
                }

                //Avvio del thread che si occupa della lettura dei messaggi che gli invia il server e del loro smistamento
                System.out.println("Stream created");
                startServerReader();

                //Lettura dell'input da tastiera, traduzione in json e invio del messaggio
                while (active) {
                    String input = scanner.nextLine();
                    String jsonInput = jsonFromInput(input);
                    sendMessage(jsonInput);
                    if (input.equals("Quit")) {
                        handleClientDisconnection();
                    }
                    System.out.println(jsonInput);
                }
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Connection closed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage (String string) {
        try {
            os.reset();
            os.writeObject(string);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleClientDisconnection() {
        active = false;
        try {
            os.close();
            is.close();
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Answer parseMessage(String jsonString) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        switch (jsonObject.get("type").getAsString()) {
            /*
            case "SelectAssistant" :
                return gson.fromJson(jsonString, SelectAssistant.class);
            case "SelectCloud" :
                return gson.fromJson(jsonString, SelectCloud.class);
            case "MoveMN" :
                return gson.fromJson(jsonString, MoveMN.class);
            case "MoveToIsland" :
                return gson.fromJson(jsonString, MoveToIsland.class);
            case "PlayCharacter" :
                return gson.fromJson(jsonString, PlayCharacter.class);
            case "SpecialMoveIsland" :
                return gson.fromJson(jsonString, SpecialMoveIsland.class);

             */
            case "Ping" :
                sendMessage("Pong");
                return null;
            default : System.out.println("Invalid message");
                return null;
        }
    }

    public String jsonFromInput(String s){
         switch (s){
             case "Join" :
                 return toJson(new Join("Carlo", Mage.MAGE1, ColorT.BLACK, 0));
             case "Jo1" :
                 return toJson(new Join("Alessia", Mage.MAGE2, ColorT.GREY, 0));
             case "Jo1.1" :
                 return toJson(new Join("Emily", Mage.MAGE3, ColorT.WHITE, 0));
             case "Jo2" :
                 return toJson(new Join("Giulia", Mage.MAGE1, ColorT.BLACK, 1));
             case "MoveMN" :
                 return toJson(new MoveMN(5));
             case "MoveToIsland" :
                 return toJson(new MoveToIsland(ColorS.RED, 1));
             case "EntranceToHall" :
                 return toJson(new EntranceToHall(ColorS.BLUE));
             case "ChooseCloud" :
                 return toJson(new ChooseCloud(1));
             case "GameParams" :
                 return toJson(new GameParams(3, true, "Carlo",Mage.MAGE1, ColorT.BLACK));
             case "gp" :
                 return toJson(new GameParams(75, true, "Carlo",Mage.MAGE1, ColorT.BLACK));
             case "Quit" :
                 return toJson(new Disconnect());
             default : System.out.println("Invalid String");
                 return null;
        }
    }

    public String toJson(Object r){
        GsonBuilder builder = new GsonBuilder();
        builder.disableHtmlEscaping();
        Gson gson = builder.create();
        JsonElement jsonElement;
        jsonElement = gson.toJsonTree(r);
        jsonElement.getAsJsonObject().addProperty("type", r.getClass().getSimpleName());

        return gson.toJson(jsonElement);
    }

    public void startServerReader() {
        new Thread(() -> {
            while (active) {
                try {
                    String s = (String) is.readObject();
                    //metodo primordiale per gestire ping e disconnessioni, poi dovrò mettere un metodo che mi fa il de-parsing della stringa
                    //e in base al risulato vedere come comportarmi
                        /*if(s.equals("{\"type\":\"Ping\"}")) {
                            sendMessage(s)
                        } else */
                    if (s.equals("{\"error\":\"You have been disconnected, you can rejoin the game in the future\",\"type\":\"Error\"}")) {
                        System.out.println(s);
                        handleClientDisconnection();
                    }
                    System.out.println(s);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    handleClientDisconnection();
                }
            }
        }).start();
    }
}