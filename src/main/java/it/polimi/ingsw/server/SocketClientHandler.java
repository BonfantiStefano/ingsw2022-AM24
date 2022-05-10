package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.server.answer.*;
import it.polimi.ingsw.server.answer.Error;

import java.io.*;
import java.net.Socket;

//Not final, work in progress
/**
 * Class SocketClientHandler manages a single Client connected to the server.
 *
 * @author Stefano Bonfanti
 */
public class SocketClientHandler implements Runnable{
    private static final int PING_PERIOD = 10000;
    private final Socket socket;
    private final Server server;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final Integer clientID;
    private boolean active;
    private final Thread pingController;
    private Thread timer;

    /**
     * Constructor SocketClientHandler create a new instance of SocketClientHandler.
     * @param socket Socket - the client's socket.
     * @param server Server - the server that has accepted the client's connection.
     * @param clientID int - the client's id.
     */
    public SocketClientHandler(Socket socket, Server server, int clientID){
        this.socket = socket;
        this.server = server;
        this.active = false;
        this.clientID = clientID;
        this.pingController = new Thread(() -> {
            while (active){
                try{
                    //Modificare il valore
                    Thread.sleep(PING_PERIOD*PING_PERIOD);
                    sendMessage(new Ping());
                }catch (InterruptedException e){
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        });
        try {
            outputStream = new ObjectOutputStream(this.socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(this.socket.getInputStream());
            System.out.println("Ho creato gli stream");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error during initialization of the client!");
            System.exit(0);
        }
    }

    /**
     * Method run contains the actions that will be done till the socket is active.
     */
    @Override
    public void run() {
        System.out.println("Il socketClientHandler è partito");
        active = true;
        pingController.start();
        startTimer();
        String jsonString;
        sendMessage(server.getLobbies());
        while (active) {
            try {
                jsonString = (String) inputStream.readObject();
                if(jsonString != null) {
                    System.out.println(jsonString);
                    Request message = parseMessage(jsonString);
                    if (message != null) {
                        server.forwardMessage(message, clientID);
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                if(!socket.isClosed()) {
                    handleClientDisconnection(false);
                }
                //System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Method startTimer starts the timer that checks if the client is active.
     */
    public void startTimer(){
        timer = new Thread(() -> {
            try{
                //Correct the value
                Thread.sleep(5*PING_PERIOD);
                System.out.println("Timeout expires");
                handleClientDisconnection(true);
            } catch (InterruptedException e){
                System.out.println("The timeout timer has been stopped");

            }
        });
        timer.start();
    }

    /**
     * Method used to stop the timer
     */
    public void stopTimer(){
        if (timer != null && timer.isAlive()){
            timer.interrupt();
            timer = null;
        }
    }

    /**
     * Method sendMessage sends a message to the client on the output stream.
     * @param serverAnswer Answer - the message that will be sent to the client.
     */
    public synchronized void sendMessage(Answer serverAnswer) {
        try {
            outputStream.reset();
            outputStream.writeObject(toJson(serverAnswer));
            outputStream.flush();
        } catch (IOException e) {
            if(/*socket != null && */!socket.isClosed()) {
                closeSocket();
            }
        }
    }

    /**
     * Method closeSocket is used to close the socket.
     */
    private void closeSocket() {
        try {
            //outputStream.close();
            socket.close();
            System.out.println("The streams have been closed");
        } catch (IOException e) {
            System.out.println("Exception during the closure of the stream");
            e.printStackTrace();
        }
        //Questi in teoria si possono togliere
        /*finally {
            try {
                socket.close();
            } catch (IOException e) {
                //Vedere se è necessario perchè l'outputStream dovrebbe chiudere anche il socket
                e.printStackTrace();
            }
        }
        */
        /*
        try {
            socket.close();
            System.out.println("The socket has been closed");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(0);
        }

         */
    }

    /**
     * Method toJson converts an Answer message to the json codify
     * @param answer Answer - the message that has to be transformed.
     * @return a String that is the json codify of the message.
     */
    public String toJson(Answer answer){
        Gson gson = new Gson();
        JsonElement jsonElement;
        jsonElement = gson.toJsonTree(answer);
        jsonElement.getAsJsonObject().addProperty("type", answer.getClass().getSimpleName());

        return gson.toJson(jsonElement);
    }

    /**
     * Method handleClientDisconnection handles the disconnection of the client, stopping the timers and closing the socket.
     */
    private void handleClientDisconnection(boolean timeout){
        System.out.println("Trying to stopping the timer");
        stopTimer();
        System.out.println("Trying to set active to false");
        this.active = false;
        pingController.interrupt();
        if(timeout) {
            sendMessage(new NotifyDisconnection("You have been disconnected, you can rejoin the game in the future"));
        }
        server.handleClientDisconnection(clientID);
        if(/*socket != null &&*/ !socket.isClosed()) {
            closeSocket();
        }
    }

    /**
     * Method parseMessage is used to convert the string (that should be in json codify) given by parameter to the corresponding
     * message that will be returned.
     * @param jsonString String - the string that has to be parsed.
     * @return a Request, that represents the string given in input.
     */
    public Request parseMessage(String jsonString) {
        GsonBuilder builder = new GsonBuilder();
        builder.disableHtmlEscaping();
        Gson gson = builder.create();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        switch (jsonObject.get("type").getAsString()) {
            case "ChooseAssistant" :
                return gson.fromJson(jsonString, ChooseAssistant.class);
            case "ChooseCloud" :
                return gson.fromJson(jsonString, ChooseCloud.class);
            case "ChooseColor" :
                return gson.fromJson(jsonString, ChooseColor.class);
            case "ChooseIsland" :
                return gson.fromJson(jsonString, ChooseIsland.class);
            case "ChooseTwoColors" :
                return gson.fromJson(jsonString, ChooseTwoColors.class);
            case "Disconnect" :
                handleClientDisconnection(false);
                return null;
            case "EntranceToHall" :
                return gson.fromJson(jsonString, EntranceToHall.class);
            case "GameParams" :
                GameParams gameParams = gson.fromJson(jsonString, GameParams.class);
                if(gameParams.getNumPlayers() < 2 || gameParams.getNumPlayers() > 3) {
                    sendMessage(new Error("Error: is only possible to play in 2 or 3 players"));
                } else {
                    server.createLobby(gameParams, clientID);
                }
                return null;
            case "Join" :
                server.handleJoin(gson.fromJson(jsonString, Join.class), clientID);
                return null;
            case "MoveMN" :
                return gson.fromJson(jsonString, MoveMN.class);
            case "MoveToIsland" :
                return gson.fromJson(jsonString, MoveToIsland.class);
            case "PlayCharacter" :
                return gson.fromJson(jsonString, PlayCharacter.class);
            case "SpecialMoveIsland" :
                return gson.fromJson(jsonString, SpecialMoveIsland.class);
            case "Pong" :
                stopTimer();
                startTimer();
                return null;
            default :
                sendMessage(new Error("Invalid message"));
                return null;
        }
    }
}

