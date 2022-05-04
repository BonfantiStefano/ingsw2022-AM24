package it.polimi.ingsw.server;

import it.polimi.ingsw.client.request.GameParams;
import it.polimi.ingsw.client.request.Join;
import it.polimi.ingsw.client.request.Request;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.ERRORS;
import it.polimi.ingsw.controller.ExpertController;
import it.polimi.ingsw.server.answer.Answer;
import it.polimi.ingsw.server.answer.Error;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Not final, work in progress
public class Lobby {
    private Controller controller;
    private Map<String, Integer> mapNicknameId;
    private Map<Integer, String> mapIdNickname;
    private Map<Integer, SocketClientHandler> mapIdSocket;
    private ArrayList<Integer> clientsId;
    //TODO understand what to do when a Player disconnect before the game has started, this can cause problems in the Model (because if I
    // add a player there aren't methods to remove him)
    private ArrayList<Integer> disconnectedClientsId;
    private final int numPlayers;
    //private boolean full;
    //private VirtualView virtualView;

    //metodo inutile usato solo perchè altrimenti per i test dell'expert controller dovrei creare tutti i parametri, poi
    //lo cambierò.
    public Lobby() {
        numPlayers = 3;
    }

    public Lobby(GameParams gameParams, SocketClientHandler socketClientHandler, int idClients) {
        createController(gameParams);
        mapIdNickname = new HashMap<>();
        mapIdSocket = new HashMap<>();
        mapNicknameId = new HashMap<>();
        clientsId = new ArrayList<>();
        disconnectedClientsId = new ArrayList<>();
        mapIdNickname.put(idClients, gameParams.getNickname());
        mapNicknameId.put(gameParams.getNickname(), idClients);
        mapIdSocket.put(idClients, socketClientHandler);
        numPlayers = gameParams.getNumPlayers();
        clientsId.add(idClients);
        //virtualView = new VirtualView();
    }

    public void createController(GameParams gameParams) {
        if(gameParams.isExpert()) {
            controller = new ExpertController(this, gameParams);
        } else {
            controller = new Controller(this, gameParams);
        }
    }

    //TODO introdurre la sincronizzazione per gestire i casi in cui più player vogliono aggiungersi contemporaneamente
    //TODO implementare la verfica anche sul colore delle torri e sul mago
    public boolean addPlayer(Join join, SocketClientHandler socketClientHandler, int idClients) {
        //TODO implements the handling of the disconnected player
        if(disconnectedClientsId.contains(idClients)) {
            socketClientHandler.sendMessage(new Error("Welcome back " + mapIdNickname.get(idClients)));
            sendMessageToOthers(mapIdNickname.get(idClients), mapIdNickname.get(idClients) + " re-connected");
        } else if(mapIdNickname.size() < numPlayers) {
            boolean ris = true;
            for (int counter = 0; counter < numPlayers && ris; counter++) {
                if (mapIdNickname.get(counter).equals(join.getNickname())) {
                    ris = false;
                    socketClientHandler.sendMessage(new Error(ERRORS.NICKNAME_TAKEN.toString()));
                }
            }
            if (ris) {
                mapIdNickname.put(idClients, join.getNickname());
                mapNicknameId.put(join.getNickname(), idClients);
                mapIdSocket.put(idClients, socketClientHandler);
                clientsId.add(idClients);
            }
            return ris;
        } else {
            socketClientHandler.sendMessage(new Error("Error: the lobby is full"));
        }
        //full = true;
        return false;
    }

    public void sendMessage(String nickname, String content){
        /*
        //TODO change the dynamical type of the message (maybe, due to all the Server message contains a string is possible to insert
        // this in the interface)
        Answer answer = new Error(content);
        mapIdSocket.get(mapNicknameId.get(nickname)).sendMessage(answer);
        */
    }

    //This two messages might have to change the signature (depends on whom calls this method)
    public void sendMessageToOthers(String nickname, String content) {
        /*
        Answer answer = new Error(content);
        for(Integer id : clientsId) {
            //how to check if a client is active or disconnected
            if(!id.equals(mapNicknameId.get(nickname))) {
                mapIdSocket.get(id).sendMessage(answer);
            }
        }

         */
    }

    public void sendMessageToAll(String content) {
        Answer answer = new Error(content);
        for(Integer id : clientsId) {
            mapIdSocket.get(id).sendMessage(answer);
        }
    }

    //TODO understand what to do with this method and in general with this condition
    public void gameEnded(){}

    //TODO understand who has to reject a message (for example a MoveMN while the game isn't already started). The only message that
    //can cause problems is Disconnect (because Join and GameParams are managed in other ways, maybe i can do the same thing)
    public void handleMessage(Request request, int clientId) {
        //Understand what to do with a disconnected player
        if(clientsId.size() < numPlayers) {
            controller.handleMessage(request, mapIdNickname.get(clientId));
        } else {
            sendMessage(mapIdNickname.get(clientId), "Error: the game is not started");
        }
    }

    //TODO implementare un modo per capire se un giocatore è disconnesso e aggiornare questo metodo
    public boolean isDisconnected(int clientId) {
        //return disconnectedClientsId.contains(clientId);
        return false;
    }
}

