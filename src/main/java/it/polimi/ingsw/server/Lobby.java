package it.polimi.ingsw.server;

import it.polimi.ingsw.client.request.Disconnect;
import it.polimi.ingsw.client.request.GameParams;
import it.polimi.ingsw.client.request.Join;
import it.polimi.ingsw.client.request.Request;
import it.polimi.ingsw.controller.controllers.Controller;
import it.polimi.ingsw.controller.ERRORS;
import it.polimi.ingsw.controller.controllers.ExpertController;
import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.server.answer.*;
import it.polimi.ingsw.server.answer.Error;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

//Not final, work in progress
/**
 * Class Lobby manages all the interaction between all the players of a lobby and the controller.
 *
 * @author Stefano Bonfanti
 */
public class Lobby {
    private Controller controller;
    private ConcurrentHashMap<String, Integer> mapNicknameId;
    private ConcurrentHashMap<Integer, String> mapIdNickname;
    private ConcurrentHashMap<Integer, SocketClientHandler> mapIdSocket;
    private ArrayList<Integer> clientsId;
    private ArrayList<Integer> disconnectedClientsId;
    private final int numPlayers;
    private ArrayList<Mage> mages;
    private ArrayList<ColorT> towers;
    private boolean mode;
    private GameStatus gameStatus;
    private Thread timerPause;
    private static final int WAITING_TIME = 50000;
    //private VirtualView virtualView;

    //metodo inutile usato solo perché altrimenti per i test dell' expert controller dovrei creare tutti i parametri, poi
    //lo cambierò.
    /**
     * Constructor Lobby creates a new empty Lobby instance.
     */
    public Lobby() {
        numPlayers = 3;
    }

    /**
     * Constructor Lobby creates a new Lobby instance.
     * @param gameParams GameParams - the message from which the parameters used to create the lobby are taken.
     * @param socketClientHandler SocketClientHandler - the socket of the client.
     * @param idClients int - the id of the client who has sent the message.
     */
    public Lobby(GameParams gameParams, SocketClientHandler socketClientHandler, int idClients) {
        createController(gameParams);
        mapIdNickname = new ConcurrentHashMap<>();
        mapIdSocket = new ConcurrentHashMap<>();
        mapNicknameId = new ConcurrentHashMap<>();
        clientsId = new ArrayList<>();
        mages = new ArrayList<>();
        towers = new ArrayList<>();
        disconnectedClientsId = new ArrayList<>();
        mapIdNickname.put(idClients, gameParams.getNickname());
        mapNicknameId.put(gameParams.getNickname(), idClients);
        mapIdSocket.put(idClients, socketClientHandler);
        numPlayers = gameParams.getNumPlayers();
        clientsId.add(idClients);
        mode = gameParams.isExpert();
        mages.add(gameParams.getMage());
        towers.add(gameParams.getColorT());
        gameStatus = GameStatus.SETUP;
        //virtualView = new VirtualView();
    }

    /**
     * Method createController is used from the lobby to create a new instance of the controller(or expertController).
     * @param gameParams GameParams - the message used by the controller to set all its parameters.
     */
    private void createController(GameParams gameParams) {
        if(gameParams.isExpert()) {
            controller = new ExpertController(this, gameParams);
        } else {
            controller = new Controller(this, gameParams);
        }
    }

    //TODO introdurre la sincronizzazione per gestire i casi in cui più player vogliono aggiungersi contemporaneamente, prima testare
    /**
     * Method AddPlayer verify the correctness of the Join's parameters and adds a player to the lobby.
     * @param join Join - the message where all the parameters are contained.
     * @param socketClientHandler SocketClientHandler - the socket of the client.
     * @param idClients int - the client's id who sent the message.
     * @return a boolean that notify the server if the addition of the player is corrected.
     */
    public /*synchronized*/ boolean addPlayer(Join join, SocketClientHandler socketClientHandler, int idClients) {
        if(mapIdNickname.size() < numPlayers && gameStatus.equals(GameStatus.SETUP)) {
            boolean ris = true;
            for (int counter = 0; counter < clientsId.size() && ris; counter++) {
                //Check uniqueness of the nickname
                if (mapIdNickname.get(clientsId.get(counter)).equals(join.getNickname())) {
                    ris = false;
                    socketClientHandler.sendMessage(new Error(ERRORS.NICKNAME_TAKEN.toString()));
                }
                //Check uniqueness of the tower's color
                if (towers.contains(join.getColorT())) {
                    ris = false;
                    socketClientHandler.sendMessage(new Error(ERRORS.COLOR_TOWER_TAKEN.toString()));
                }
                //Check uniqueness of the mage
                if (mages.contains(join.getMage())) {
                    ris = false;
                    socketClientHandler.sendMessage(new Error(ERRORS.MAGE_TAKEN.toString()));
                }
            }
            //If everything is okay the player must be added
            if (ris) {
                mapIdNickname.put(idClients, join.getNickname());
                mapNicknameId.put(join.getNickname(), idClients);
                mapIdSocket.put(idClients, socketClientHandler);
                mages.add(join.getMage());
                towers.add(join.getColorT());
                clientsId.add(idClients);
                socketClientHandler.sendMessage(new Information("You have joined the game"));
                sendMessageToOthers(join.getNickname(), new Information(join.getNickname() + " entered the lobby"));
                controller.handleMessage(join, mapIdNickname.get(idClients));
                if(clientsId.size() == numPlayers) {
                    gameStatus = GameStatus.PLAYING;
                }
            }
            return ris;
        } else {
            socketClientHandler.sendMessage(new Error("Error: the lobby is full"));
        }
        return false;
    }

    /**
     * Method checkReconnection handles the re-connection of a player.
     * @param join Join - the message sent by the player.
     * @param socketClientHandler SocketClientHandler - the new player's socket.
     * @param clientId int - The new client's id.
     * @return an int - the old value of the client's id that will be removed from the server.
     */
    public int checkReconnection(Join join, SocketClientHandler socketClientHandler, int clientId) {
        if(mapNicknameId.containsKey(join.getNickname()) && disconnectedClientsId.contains(mapNicknameId.get(join.getNickname()))
                && gameStatus != GameStatus.ENDED /*TODO manca il controllo se il mago e il colore delle torri era quello di prima*/) {
            int oldId = mapNicknameId.get(join.getNickname());
            //Notification of the re-connection to the players
            socketClientHandler.sendMessage(new Information("Welcome back " + join.getNickname()));
            sendMessageToOthers(join.getNickname(), new Information(join.getNickname() + " re-connected"));
            //Handles of the re-connection
            mapNicknameId.replace(join.getNickname(), clientId);
            mapIdNickname.remove(oldId);
            mapIdNickname.put(clientId, join.getNickname());
            mapIdSocket.remove(oldId);
            mapIdSocket.put(clientId, socketClientHandler);
            disconnectedClientsId.remove((Integer) oldId);
            clientsId.remove((Integer) oldId);
            clientsId.add(clientId);
            controller.handleMessage(join, join.getNickname());
            //If the game was in pause the timer is stopped and the game can continue
            if(gameStatus == GameStatus.PAUSE) {
                stopTimer();
                sendMessageToAll(new Information("Game continue"));
                gameStatus = GameStatus.PLAYING;
            }
            return oldId;
        }
        //forse si può mettere che se uno prova a riconnettersi dopo che la partita è finita può dirgli che la partita è finita
        if(gameStatus == GameStatus.ENDED) {
            socketClientHandler.sendMessage(new Error("Error: game ended"));
        } else {
            socketClientHandler.sendMessage(new Error(ERRORS.NICKNAME_TAKEN.toString()));
        }
        return -1;
    }

    //Forse va sincronizzato questo metodo, o forse è meglio la handleJoin nel server, così non si possono verificare casi in cui uno si
    //disconnette mentre l' altro si riconnette (magari si hanno rallentamenti quando ho molte lobby, potrei però bloccare solo la lobby che
    // mi serve così non creo rallentamenti)
    /**
     * Method handleDisconnection is used to disconnect a player from the lobby.
     * @param clientId int - the client's id of the player who has to be disconnected.
     */
    public void handleDisconnection(int clientId) {
        disconnectedClientsId.add(clientId);
        if((disconnectedClientsId.size() == 1 && clientsId.size() == 1 && gameStatus == GameStatus.SETUP) ||
                (disconnectedClientsId.size() == numPlayers && gameStatus == GameStatus.PAUSE)) {
            System.out.println("The lobby must be closed");
            //Togliere commento delle istruzioni sotto solo quando avremo finito altrimenti mi sarà difficile fare il debug
            gameStatus = GameStatus.ENDED;
            //dobbiamo comunicarlo anche al controller che la partita è finita?
        } else {
            controller.handleMessage(new Disconnect(), mapIdNickname.get(clientId));
            if(clientsId.size() - disconnectedClientsId.size() == 1 && gameStatus == GameStatus.PLAYING) {
                gameStatus = GameStatus.PAUSE;
                startTimer();
                System.out.println("The lobby is in pause, waiting a re-connection");
            }
        }
    }

    /**
     * Method sendMessage sends an Answer message to the client which has the nickname given by parameter.
     * @param nickname String - the nickname of the player who will receive the message.
     * @param answer AnswerWithString - the message that will be sent to the client.
     */
    public void sendMessage(String nickname, AnswerWithString answer){
        //Si può cambiare in !mapIdSocket.isEmpty() && !mapNicknameId
        if(mapIdSocket != null && mapNicknameId != null) {
            mapIdSocket.get(mapNicknameId.get(nickname)).sendMessage(answer);
        }
    }

    /**
     * Method sendMessageToOthers sends an Answer message to all the clients except the one whose nickname is given by parameter.
     * @param nickname String - the nickname of the player who will not receive the message.
     * @param answer AnswerWithString - the message that will be sent to the clients.
     */
    public void sendMessageToOthers(String nickname, AnswerWithString answer) {
        if(clientsId != null) {
            for (Integer id : clientsId) {
                //how to check if a client is active or disconnected
                if (mapIdSocket != null && mapNicknameId != null && !id.equals(mapNicknameId.get(nickname)) && !disconnectedClientsId.contains(id)) {
                    mapIdSocket.get(id).sendMessage(answer);
                }
            }
        }
    }

    /**
     * Method sendMessageToAll sends an Answer message to all the clients.
     * @param answer AnswerWithString - the message that will be sent to the clients.
     */
    public void sendMessageToAll(Answer answer) {
        if(clientsId != null) {
            for (Integer id : clientsId) {
                if (!disconnectedClientsId.contains(id) && mapIdSocket != null) {
                    mapIdSocket.get(id).sendMessage(answer);
                }
            }
        }
    }

    /**
     * Method gameEnded sets the status of the game to ended.
     */
    public void gameEnded(){
        gameStatus = GameStatus.ENDED;
    }

    /**
     * Method handleMessage forward the message to the controller if the game is started.
     * @param request Request - the message sent by the client.
     * @param clientId int - the sender's id.
     */
    public void handleMessage(Request request, int clientId) {
        if(clientsId.size() == numPlayers && gameStatus == GameStatus.PLAYING) {
            controller.handleMessage(request, mapIdNickname.get(clientId));
        } else if(gameStatus == GameStatus.SETUP){
            mapIdSocket.get(clientId).sendMessage(new Error("Error: the game is not started"));
        } else if(gameStatus == GameStatus.PAUSE){
            mapIdSocket.get(clientId).sendMessage(new Error("Error: the game is in pause, please wait the re-connection of a player"));
        } else {
            mapIdSocket.get(clientId).sendMessage(new Error("Error: the game is finished"));
        }
    }

    /**
     * Method isPresent returns if there is in the lobby a player with the given nickname.
     * @param nickname String - the nickname that will be checked.
     * @return a boolean, true if the nickname is in the lobby, otherwise false.
     */
    public boolean isPresent(String nickname) {
        return mapNicknameId.containsKey(nickname);
    }

    /**
     * Method getGameStatus return the status of the game.
     * @return GameStatus - the status of the game.
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Method getMages returns the List of the already chosen mages.
     * @return ArrayList<Mage></> the mages already chosen.
     */
    public ArrayList<Mage> getMages() {
       return mages;
    }

    /**
     * Method getColorTowers returns the List of the already chosen towers' color.
     * @return ArrayList<ColorT></> the color of the tower already chosen.
     */
    public ArrayList<ColorT> getColorTowers() {
        return towers;
    }

    /**
     * Method getNicknames returns the List of the taken nickname.
     * @return ArrayList<String></> the nickname already taken.
     */
    public ArrayList<String> getNicknames() {
        ArrayList<String> nicknames = new ArrayList<>();
        for(Integer id : clientsId) {
            nicknames.add(mapIdNickname.get(id));
        }
        return nicknames;
    }

    /**
     * Method getNumPlayers returns the number of game's players.
     * @return an int - the number of game's players.
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Method isMode returns if the game is expert or basic.
     * @return a boolean - true if the game is in expertMode, false otherwise.
     */
    public boolean isMode() {
        return mode;
    }

    /**
     * Method startTimer starts the timer that waits the reconnection of a player.
     */
    public void startTimer(){
        timerPause = new Thread(() -> {
            try{
                Thread.sleep(WAITING_TIME);
                System.out.println("Timeout expires");
                gameStatus = GameStatus.ENDED;
                for(Integer clientId : clientsId) {
                    if(!disconnectedClientsId.contains(clientId)) {
                        sendMessage(mapIdNickname.get(clientId), new Information("You are the only connected player, you won!"));
                    }
                }
            } catch (InterruptedException ignored){
            }
        });
        timerPause.start();
    }

    /**
     * Method used to stop the timer
     */
    public void stopTimer(){
        if (timerPause != null && timerPause.isAlive()){
            timerPause.interrupt();
            timerPause = null;
        }
    }
}