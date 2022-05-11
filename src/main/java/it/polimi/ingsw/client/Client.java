package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.CLIView.CLI;
import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.server.answer.*;
import it.polimi.ingsw.server.answer.Error;
import it.polimi.ingsw.server.answer.Update.*;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

//Not final, work in progress
//TODO implementare timer che aspetta per un intervallo un messaggio dal server dopodichè dice che il server è stato disconnesso
// e si scollega --> implementato, da togliere quando vogliamo e vedere se funziona
public class Client {
    private ObjectOutputStream os;
    private ObjectInputStream is;
    private boolean active;
    private final CLI cli;
    private Thread timer;
    private static final int TIMEOUT = 50000;

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
        cli = new CLI(this);
    }

    public void startClient(String ip, int port) {
        active = true;
        startTimer();
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

                //Avvio del metodo che si occupa della lettura dei messaggi che gli invia il server e del loro smistamento
                System.out.println("Stream created");
                startServerReader();
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
        stopTimer();
        try {
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Answer parseMessage(String jsonString) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        switch (jsonObject.get("type").getAsString()) {
            case "Welcome" :
                cli.setWelcome(gson.fromJson(jsonString, Welcome.class));
                //TODO far partire questi metodi solo la prima volta
                new Thread(cli).start();
                return gson.fromJson(jsonString, Welcome.class);
            case "Error" :
                return gson.fromJson(jsonString, Error.class);
            case "Information" :
                return gson.fromJson(jsonString, Information.class);
            case "Ping" :
                sendMessage(cli.toJson(new Pong()));
                return null;
            case "NotifyDisconnection" :
                //Dopo si toglie la system out
                System.out.println(gson.fromJson(jsonString, NotifyDisconnection.class).getString());
                handleClientDisconnection();
                //TODO manca da chiudere lo scanner se no il programma non si ferma
                return null;
            case "AddPlayer":
                return gson.fromJson(jsonString, AddPlayer.class);
            case "CreateCharacters":
                return gson.fromJson(jsonString, CreateCharacters.class);
            case "CreateClouds":
                return gson.fromJson(jsonString, CreateClouds.class);
            case "ReplaceCharacter":
                return gson.fromJson(jsonString, ReplaceCharacter.class);
            case "ReplaceCloud":
                return gson.fromJson(jsonString, ReplaceCloud.class);
            case "UpdateCoins":
                return gson.fromJson(jsonString, UpdateCoins.class);
            case "UpdateIsland":
                return gson.fromJson(jsonString, UpdateIsland.class);
            case "UpdateMN":
                return gson.fromJson(jsonString, UpdateMN.class);
            case "UpdatePlayer":
                return gson.fromJson(jsonString, UpdatePlayer.class);
            case "UpdateProfs":
                return gson.fromJson(jsonString, UpdateProfs.class);
            case "UpdateWorld":
                return gson.fromJson(jsonString, UpdateWorld.class);
            default : System.out.println("Invalid message");
                return null;
        }
    }

    public void startServerReader() {
        while (active) {
            try {
                String s = (String) is.readObject();
                if(s!= null) {
                    //questo if poi andrà rimosso, utile per debuggare.
                    if (!s.equals("{\"type\":\"Ping\"}")) {
                        System.out.println(s);
                    }
                    stopTimer();
                    startTimer();
                    Answer a = parseMessage(s);
                    //TODO handle all other messages
                    if (a instanceof Update)
                        cli.handleMessage((Update) a);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Socket chiuso, al posto di e.printStackTrace");
                //e.printStackTrace();
                handleClientDisconnection();
            }
        }
    }

    //Utile se vogliamo implementare un timer che ci dice che il server non è raggiungibile
    public void startTimer(){
        timer = new Thread(() -> {
            try{
                Thread.sleep(TIMEOUT);
                System.out.println("The server isn't available");
                handleClientDisconnection();
            } catch (InterruptedException e){
                //System.out.println("The timeout timer has been stopped");
            }
        });
        timer.start();
    }

    public void stopTimer(){
        if (timer != null && timer.isAlive()){
            timer.interrupt();
            timer = null;
        }
    }

    public boolean isActive() {
        return active;
    }
}