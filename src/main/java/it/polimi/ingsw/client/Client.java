package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.model.EVENT;
import it.polimi.ingsw.server.answer.*;
import it.polimi.ingsw.server.answer.Error;
import it.polimi.ingsw.server.answer.Update.*;
import it.polimi.ingsw.server.virtualview.VirtualView;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Class Client manages the connection of a client with the server.
 *  *
 *  * @author Baratto Marco, Bonfanti Stefano
 */
public class Client{
    private ObjectOutputStream os;
    private ObjectInputStream is;
    private boolean active;
    private final UserInterface userInterface;
    private Thread timer;
    private final Thread messageHandler;
    private VirtualView virtualView;
    private static final int TIMEOUT = 50000;
    private final BlockingQueue<Answer> messagesQueue;
    protected final PropertyChangeSupport listener = new PropertyChangeSupport(this);

    /**
     * Constructor Client creates a new Client instance.
     */
    public Client(UserInterface userInterface) {
        active = false;
        this.userInterface = userInterface;
        virtualView = new VirtualView();
        userInterface.setVirtualView(virtualView);
        messagesQueue = new ArrayBlockingQueue<>(20);
        this.messageHandler = new Thread(this::messageParser);
    }

    /**
     * Method startClient creates the socket, the streams and start reading the messages from the server.
     * @param ip String - the ip address of the server.
     * @param port int - the port utilized by the server.
     */
    public void startClient(String ip, int port) {
        active = true;
        startTimer();
        //Socket creation
        try (Socket socket = new Socket(ip, port)) {
            System.out.println("Connection established");
            //Stream creation
            try {
                os = new ObjectOutputStream(socket.getOutputStream());
                os.flush();
                is = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                System.out.println("Error during initialization of the client!");
                System.exit(0);
            }
            System.out.println("Stream created");
            //start thread that handles all messages received sequentially
            messageHandler.start();
            //The client starts to read the server input
            startServerReader();

        } catch (NoSuchElementException | IllegalStateException e) {
            System.out.println("Connection closed");
            System.exit(-1);
        } catch (IOException e) {
            System.out.println("Error during the creation of the socket");
            System.exit(-1);
        }
    }

    /**
     * Method startServerReader reads all the message that the server sends and forwards those messages to the user interface.
     */
    private void startServerReader() {
        while (active) {
            try {
                String s = (String) is.readObject();
                if(s!= null) {
                    //System.out.println(s);
                    stopTimer();
                    startTimer();
                    Answer a = parseMessage(s);
                    if (a!=null) {
                        messagesQueue.add(a);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Socket chiuso, al posto di e.printStackTrace");
                handleClientDisconnection(false);
            }
        }
    }

    /**
     * Method sendMessage sends a message to the server.
     * @param string String - the message, in json notation, that will be sent to the server.
     */
    public void sendMessage (String string) {
        try {
            os.reset();
            os.writeObject(string);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method handleClientDisconnection closes the streams.
     */
    public void handleClientDisconnection(boolean quit) {
        active = false;
        stopTimer();
        if(quit) {
            sendMessage(toJson(new Disconnect()));
        }
        try {
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    /**
     * Method parseMessage converts a json String to an Answer message.
     * @param jsonString String - the string received in input
     * @return an Answer message.
     */
    public Answer parseMessage(String jsonString) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        switch (jsonObject.get("type").getAsString()) {
            case "Welcome" :
                return gson.fromJson(jsonString, Welcome.class);
            case "Error" :
                return gson.fromJson(jsonString, Error.class);
            case "InformationGame" :
                return gson.fromJson(jsonString, InformationGame.class);
            case "InformationConnection" :
                return gson.fromJson(jsonString, InformationConnection.class);
            case "Ping" :
                sendMessage(toJson(new Pong()));
                return null;
            case "NotifyDisconnection" :
                //Se mi arriva questo messaggio non passo mai per la visit poichè facendo la handleClientDisconnection si chiude tutto
                //e stampo su sout il messaggio, decidere cosa fare
                System.out.println(gson.fromJson(jsonString, NotifyDisconnection.class).getString());
                handleClientDisconnection(false);
                return null;
            case "AddPlayer":
                return gson.fromJson(jsonString, AddPlayer.class);
            case "CreateCharacters":
                return gson.fromJson(jsonString, CreateCharacters.class);
            case "CreateClouds":
                return gson.fromJson(jsonString, CreateClouds.class);
            case "ReplaceCharacter":
                return gson.fromJson(jsonString, ReplaceCharacter.class);
            case "ReplaceCharacterStudents":
                return gson.fromJson(jsonString,ReplaceCharacterStudents.class);
            case "ReplaceCharacterWithNoEntry":
                return gson.fromJson(jsonString,ReplaceCharacterWithNoEntry.class);
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
            case "UpdateActiveCharacter":
                return gson.fromJson(jsonString, UpdateActiveCharacter.class);
            case "FullView":
                return gson.fromJson(jsonString, FullView.class);
            default : System.out.println("Invalid message: "+ jsonString);
                return null;
        }
    }

    /**
     * Method startTimer starts the timer that checks if the server is still alive.
     */
    public void startTimer(){
        timer = new Thread(() -> {
            try{
                Thread.sleep(TIMEOUT);
                System.out.println("The server isn't available");
                handleClientDisconnection(true);
            } catch (InterruptedException e){
                //System.out.println("The timeout timer has been stopped");
            }
        });
        timer.start();
    }

    /**
     * Method stopTimer stops the timer.
     */
    public void stopTimer(){
        if (timer != null && timer.isAlive()){
            timer.interrupt();
            timer = null;
        }
    }

    /**
     * Method isActive returns the active's value.
     * @return a boolean, true if the socket is alive, false otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Converts an Object to Json format
     * @param r the Client's request
     * @return Request Object containing the Message
     */
    public String toJson(Object r){
        Gson gson = new Gson();
        JsonElement jsonElement;
        jsonElement = gson.toJsonTree(r);
        jsonElement.getAsJsonObject().addProperty("type", r.getClass().getSimpleName());

        return gson.toJson(jsonElement);
    }

    /**
     * Method visit contains all the action that the Client has to do when receive a message from the Server.
     * @param u FullView - the messages containing all the parameters that the Client needs.
     */
    public void visit(FullView u){
        virtualView = u.getVirtualView();
        userInterface.setVirtualView(u.getVirtualView());
        // Esempio di listener, da modificare sicuramente (ho aggiunto questo evento) chiedere come farlo
        listener.firePropertyChange(String.valueOf(EVENT.UPDATE_ALL), null, u.getVirtualView());
    }

    /**
     * Method visit contains all the action that the Client has to do when receive a message from the Server.
     * @param u AddPlayer - the messages containing all the parameters that the Client needs.
     */
    public void visit(AddPlayer u){
        virtualView.addVirtualPlayer(u.getPlayer());
        listener.firePropertyChange(String.valueOf(EVENT.ADD_PLAYER), null, u.getPlayer());
    }

    /**
     * Method visit contains all the action that the Client has to do when receive a message from the Server.
     * @param u CreateCharacters - the messages containing all the parameters that the Client needs.
     */
    public void visit(CreateCharacters u){
        virtualView.setVirtualCharacters(u.getCharacters());
        listener.firePropertyChange(String.valueOf(EVENT.CREATE_CHARACTERS), null, u.getCharacters());
    }

    /**
     * Method visit contains all the action that the Client has to do when receive a message from the Server.
     * @param u CreateClouds - the messages containing all the parameters that the Client needs.
     */
    public void visit(CreateClouds u){
        virtualView.setVirtualClouds(u.getClouds());
        listener.firePropertyChange(String.valueOf(EVENT.CREATE_CLOUDS), null, u.getClouds());

    }

    /**
     * Method visit contains all the action that the Client has to do when receive a message from the Server.
     * @param u ReplaceCharacter - the messages containing all the parameters that the Client needs.
     */
    public void visit(ReplaceCharacter u){
        virtualView.setVirtualCharacters(u.getIndex(),u.getCharacter());
        listener.firePropertyChange(String.valueOf(EVENT.REPLACE_CHARACTER), null, u.getCharacter());
    }

    /**
     * Method visit contains all the action that the Client has to do when receive a message from the Server.
     * @param u ReplaceCharacterStudents - the messages containing all the parameters that the Client needs.
     */
    public void visit(ReplaceCharacterStudents u){
        virtualView.setVirtualCharacters(u.getIndex(),u.getVirtualCharacterWithStudents());
        listener.firePropertyChange(String.valueOf(EVENT.REPLACE_CHARACTER_S), null, u.getVirtualCharacterWithStudents());
    }

    /**
     * Method visit contains all the action that the Client has to do when receive a message from the Server.
     * @param u ReplaceCharacterWithNoEntry - the messages containing all the parameters that the Client needs.
     */
    public void visit(ReplaceCharacterWithNoEntry u){
        virtualView.setVirtualCharacters(u.getIndex(),u.getCharacterWithNoEntry());
        listener.firePropertyChange(String.valueOf(EVENT.REPLACE_CHARACTER_NE), null, u.getCharacterWithNoEntry());
    }

    /**
     * Method visit contains all the action that the Client has to do when receive a message from the Server.
     * @param u ReplaceCloud - the messages containing all the parameters that the Client needs.
     */
    public void visit(ReplaceCloud u){
        virtualView.setVirtualClouds(u.getIndex(),u.getCloud());
        listener.firePropertyChange(String.valueOf(EVENT.REPLACE_CLOUD), null, u.getCloud());
    }

    /**
     * Method visit contains all the action that the Client has to do when receive a message from the Server.
     * @param u UpdateCoins - the messages containing all the parameters that the Client needs.
     */
    public void visit(UpdateCoins u){
        virtualView.setVirtualCoins(u.getCoins());
        listener.firePropertyChange(String.valueOf(EVENT.BOARD_COINS), null, u.getCoins());
    }

    /**
     * Method visit contains all the action that the Client has to do when receive a message from the Server.
     * @param u ReplaceCharacterWithNoEntry - the messages containing all the parameters that the Client needs.
     */
    public void visit(UpdateIsland u){
        virtualView.setVirtualWorld(u.getIndex(),u.getIsland());
        listener.firePropertyChange(String.valueOf(EVENT.REPLACE_ISLAND), null, u.getIsland());
    }

    /**
     * Method visit contains all the action that the Client has to do when receive a message from the Server.
     * @param u UpdateWorld - the messages containing all the parameters that the Client needs.
     */
    public void visit(UpdateWorld u){
        virtualView.setVirtualWorld(u.getIslands());
        listener.firePropertyChange(String.valueOf(EVENT.CREATE_WORLD), null, u.getIslands());
    }

    /**
     * Method visit contains all the action that the Client has to do when receive a message from the Server.
     * @param u UpdateMN - the messages containing all the parameters that the Client needs.
     */
    public void visit(UpdateMN u){
        virtualView.setMnPos(u.getIndex());
        listener.firePropertyChange(String.valueOf(EVENT.CHANGE_MN_POS), null, u.getIndex());
    }

    /**
     * Method visit contains all the action that the Client has to do when receive a message from the Server.
     * @param u UpdatePlayer - the messages containing all the parameters that the Client needs.
     */
    public void visit(UpdatePlayer u){
        virtualView.setVirtualPlayers(u.getIndex(),u.getPlayer());
        listener.firePropertyChange(String.valueOf(EVENT.REPLACE_PLAYER), null, u.getIndex());
    }

    /**
     * Method visit contains all the action that the Client has to do when receive a message from the Server.
     * @param u UpdateProfs - the messages containing all the parameters that the Client needs.
     */
    public void visit(UpdateProfs u){
        virtualView.setVirtualProfs(u.getProfs());
        listener.firePropertyChange(String.valueOf(EVENT.REPLACE_PROFS), null, u.getProfs());
    }

    /**
     * Method visit contains all the action that the Client has to do when receive a message from the Server.
     * @param u UpdateActiveCharacter - the messages containing all the parameters that the Client needs.
     */
    public void visit(UpdateActiveCharacter u) {
        virtualView.getVirtualCharacters().get(u.getIndex()).setActive(u.isActive());
        listener.firePropertyChange(String.valueOf(EVENT.ACTIVE_CHARACTER), null, u);
    }

    /**
     * Method visit contains all the action that the Client has to do when receive a message from the Server.
     * @param error Error - the messages containing all the parameters that the Client needs.
     */
    public void visit(Error error){
        listener.firePropertyChange("ERROR", null, error);
    }

    /**
     * Method visit contains all the action that the Client has to do when receive a message from the Server.
     * @param informationGame InformationGame - the messages containing all the parameters that the Client needs.
     */
    public void visit(InformationGame informationGame){
        listener.firePropertyChange("INFORMATIONGAME", null, informationGame);
    }

    /**
     * Method visit contains all the action that the Client has to do when receive a message from the Server.
     * @param informationConnection InformationConnection - the messages containing all the parameters that the Client needs.
     */
    public void visit(InformationConnection informationConnection){
        listener.firePropertyChange("INFORMATIONCONNECTION", null, informationConnection);
    }

    /**
     * Method visit contains all the action that the Client has to do when receive a message from the Server.
     * @param notifyDisconnection NotifyDisconnection - the messages containing all the parameters that the Client needs.
     */
    public void visit(NotifyDisconnection notifyDisconnection) {
        //in teoria non raggiungo mai questo ramo
        listener.firePropertyChange("NOTIFYDISCONNECTION", null, notifyDisconnection);
    }

    /**
     * Method visit contains all the action that the Client has to do when receive a message from the Server.
     * @param welcome Welcome - the messages containing all the parameters that the Client needs.
     */
    public void visit(Welcome welcome) {
        listener.firePropertyChange("WELCOME", null, welcome);
    }

    /**
     * Method getSizeQueue returns the size of the incoming messages' queue.
     * @return int - the size of the queue.
     */
    public int getSizeQueue() {
        return messagesQueue.size();
    }

    /**
     * Method messageParser is used to take the messages from the queue and to handle it.
     */
    public void messageParser() {
        try {
            while (active) {
                Answer u = messagesQueue.take();
                handleMessage(u);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles all Updates calling the respective visitor method
     * @param a the Update received
     */
    public void handleMessage(Answer a){
        a.accept(this);
    }

    /**
     * Method addListener adds a PropertyChangeListener to the Client.
     * @param userInterface UserInterface - the class that will receive the notification by the Client.
     */
    public void addListener(PropertyChangeListener userInterface){
        listener.addPropertyChangeListener(userInterface);
    }
}