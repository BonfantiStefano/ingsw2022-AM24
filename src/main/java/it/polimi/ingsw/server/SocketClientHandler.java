package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.server.answer.Answer;
import it.polimi.ingsw.server.answer.Error;
import it.polimi.ingsw.server.answer.Ping;
import it.polimi.ingsw.server.answer.Welcome;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

//Not final, work in progress
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

    public SocketClientHandler(Socket socket, Server server, int clientID){
        this.socket = socket;
        this.server = server;
        this.active = false;
        this.clientID = clientID;
        this.pingController = new Thread(() -> {
            while (active){
                try{
                    Thread.sleep(PING_PERIOD);
                    sendMessage(new Ping());
                }catch (InterruptedException e){
                    break;
                }
            }
        });
        try {
            outputStream = new ObjectOutputStream(this.socket.getOutputStream());
            System.out.println("creato l'outputStream");
            outputStream.flush();
            System.out.println("Flushato l'output stream");
            inputStream = new ObjectInputStream(this.socket.getInputStream());
            System.out.println("Ho creato gli stream");
        } catch (IOException e) {
            System.out.println("Error during initialization of the client!");
            System.exit(0);
        }
    }

    @Override
    public void run() {
        System.out.println("Il socketClientHandler è partito");
        active = true;
        pingController.start();
        startTimer();
        String jsonString;
        //TODO implements a method getLobbies in the server that return a WELCOME message that will be sent to the client
        //sendMessage(server.getLobbies());
        while (active) {
            try {
                //System.out.println("Sono nel while");
                jsonString = (String) inputStream.readObject();
                if(jsonString != null) {
                    System.out.println(jsonString);
                    Request message = parseMessage(jsonString);
                    if (message != null) {
                        server.forwardMessage(message, clientID);
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                handleClientDisconnection();
                e.printStackTrace();
            } catch (Exception e) {
                sendMessage(new Error(e.getMessage()));
            }
        }
    }

    public void startTimer(){
        timer = new Thread(() -> {
            try{
                Thread.sleep(5*PING_PERIOD);
                System.out.println("Timeout expires");
                handleClientDisconnection();
            } catch (InterruptedException e){
                e.printStackTrace();
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
        }
    }

    public void sendMessage(Answer serverAnswer) {
        try {
            outputStream.reset();
            outputStream.writeObject(toJson(serverAnswer));
            outputStream.flush();
        } catch (IOException e) {
            closeSocket();
        }
    }

    private void closeSocket() {
        try {
            inputStream.close();
        } catch (IOException e) {
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
    }

    public String toJson(Answer answer){
        Gson gson = new Gson();
        JsonElement jsonElement;
        jsonElement = gson.toJsonTree(answer);
        jsonElement.getAsJsonObject().addProperty("type", answer.getClass().getSimpleName());

        return gson.toJson(jsonElement);
    }

    private void handleClientDisconnection(){
        System.out.println("Trying to stopping the timer");
        stopTimer();
        System.out.println("Trying to set active to false");
        this.active = false;
        //the server has to verify if the game as already started
        //server.handleClientDisconnection(clientID);
        sendMessage(new Error("You have been disconnected, you can rejoin the game in the future"));
        //closeSocket();
    }

    public Request parseMessage(String jsonString) throws Exception {
        Gson gson = new Gson();
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
                handleClientDisconnection();
                return gson.fromJson(jsonString, Disconnect.class);
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
                if(server.handleJoin(gson.fromJson(jsonString, Join.class), clientID)) {
                    return gson.fromJson(jsonString, Join.class);
                } else {
                    System.out.println("Error wow");
                    sendMessage(new Error("Nickname already chosen or lobby full, please retry"));
                    return null;
                }
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
            default : throw new Exception("Invalid message");
        }
    }
}

