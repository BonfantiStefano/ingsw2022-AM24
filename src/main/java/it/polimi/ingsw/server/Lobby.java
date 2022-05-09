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
import it.polimi.ingsw.server.answer.Update.Update;

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
    private ArrayList<Integer> disconnectedClientsId;
    private final int numPlayers;
    private ArrayList<Mage> mages;
    private ArrayList<ColorT> towers;
    private boolean mode;
    private GameStatus gameStatus;
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

    public void createController(GameParams gameParams) {
        if(gameParams.isExpert()) {
            controller = new ExpertController(this, gameParams);
        } else {
            controller = new Controller(this, gameParams);
        }
    }

    //TODO introdurre la sincronizzazione per gestire i casi in cui più player vogliono aggiungersi contemporaneamente, prima testare
    public boolean addPlayer(Join join, SocketClientHandler socketClientHandler, int idClients) {
        if(mapIdNickname.size() < numPlayers && gameStatus.equals(GameStatus.SETUP)) {
            boolean ris = true;
            for (int counter = 0; counter < clientsId.size() && ris; counter++) {
                //Controllo unicità nickname
                if (mapIdNickname.get(clientsId.get(counter)).equals(join.getNickname())) {
                    ris = false;
                    socketClientHandler.sendMessage(new Error(ERRORS.NICKNAME_TAKEN.toString()));
                }
                //Controllo unicità colore torri
                if (towers.contains(join.getColorT())) {
                    ris = false;
                    socketClientHandler.sendMessage(new Error(ERRORS.COLOR_TOWER_TAKEN.toString()));
                }
                //Controllo unicità mago
                if (mages.contains(join.getMage())) {
                    ris = false;
                    socketClientHandler.sendMessage(new Error(ERRORS.MAGE_TAKEN.toString()));
                }
            }
            //Nel caso in cui non ci sono problemi devo aggiungerlo alla lobby
            if (ris) {
                mapIdNickname.put(idClients, join.getNickname());
                mapNicknameId.put(join.getNickname(), idClients);
                mapIdSocket.put(idClients, socketClientHandler);
                mages.add(join.getMage());
                towers.add(join.getColorT());
                clientsId.add(idClients);
                controller.handleMessage(join, mapIdNickname.get(idClients));
                //Poi andranno aggiunti i messaggi agli altri giocatori
                socketClientHandler.sendMessage(new Information("You have joined the game"));
                sendMessageToOthers(join.getNickname(), new Information(join.getNickname() + " entered the lobby"));
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

    public int checkReconnection(Join join, SocketClientHandler socketClientHandler, int clientId) {
        if(mapNicknameId.containsKey(join.getNickname()) && disconnectedClientsId.contains(mapNicknameId.get(join.getNickname()))
                && gameStatus != GameStatus.ENDED) {
            int oldId = mapNicknameId.get(join.getNickname());
            socketClientHandler.sendMessage(new Information("Welcome back " + join.getNickname()));
            sendMessageToOthers(join.getNickname(), new Information(join.getNickname() + " re-connected"));
            mapNicknameId.replace(join.getNickname(), clientId);
            mapIdNickname.remove(oldId);
            mapIdNickname.put(clientId, join.getNickname());
            mapIdSocket.remove(oldId);
            mapIdSocket.put(clientId, socketClientHandler);
            disconnectedClientsId.remove((Integer) oldId);
            clientsId.remove((Integer) oldId);
            clientsId.add((Integer) clientId);
            controller.handleMessage(join, join.getNickname());
            System.out.println(mapIdNickname.toString());
            return oldId;
        }
        //forse si può mettere che se uno prova a riconnettersi dopo che la partita è finita può dirgli che la partita è finita
        return -1;
    }

    //TODO implementare che quando ho un solo player e si disconnette elimino la lobby, negli altri casi viene sempre messo in pausa e continuano gli
    //altri a meno che me ne rimane uno solo e in quel caso dopo aver aspettato un certo intervallo di tempo chiudo la lobby
    public void handleDisconnection(int clientId) {
        disconnectedClientsId.add(clientId);
        if(disconnectedClientsId.size() == 1 && clientsId.size() == 1 && gameStatus != GameStatus.ENDED) {
            System.out.println("The lobby must be closed");
            //Togliere commento delle istruzioni sotto solo quando avremo finito altrimenti mi sarà difficile fare il debug
            //gameStatus = GameStatus.ENDED;
        } else {
            controller.handleMessage(new Disconnect(), mapIdNickname.get(clientId));
        } /*if(clientsId.size() - disconnectedClientsId.size() == 1 && gameStatus != GameStatus.ENDED) {
            //inserire timer e poi nel caso far finire la partita, mi basta mettere gameStatus ad Ended
        }*/
    }

    public void sendMessage(String nickname, AnswerWithString answer){
        //Si può cambiare in !mapIdSocket.isEmpty() && !mapNicknameId
        if(mapIdSocket != null && mapNicknameId != null) {
            mapIdSocket.get(mapNicknameId.get(nickname)).sendMessage(answer);
        }
    }

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

    public void sendMessageToAll(AnswerWithString answer) {
        if(clientsId != null) {
            for (Integer id : clientsId) {
                if (!disconnectedClientsId.contains(id) && mapIdSocket != null) {
                    mapIdSocket.get(id).sendMessage(answer);
                }
            }
        }
    }

    //TODO aggiornare il controller e togliere questo metodo
    public void gameEnded(){}

    public void handleMessage(Request request, int clientId) {
        //Understand what to do with a disconnected player
        if(clientsId.size() == numPlayers && gameStatus == GameStatus.PLAYING) {
            controller.handleMessage(request, mapIdNickname.get(clientId));
        } else {
            mapIdSocket.get(clientId).sendMessage(new Error("Error: the game is not started"));
        }
    }

    public boolean isPresent(String nickname) {
        return mapNicknameId.containsKey(nickname);
    }

    /*
    prima facevo:
    public boolean isPresent(String nickname) {
        for(Integer clientId : clientsId) {
            if(mapIdNickname.get(clientId).equals(nickname)) {
                return true;
            }
        }
        return false;
    }
     */

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public ArrayList<Mage> getMages() {
       return mages;
    }

    public ArrayList<ColorT> getColorTowers() {
        return towers;
    }

    public ArrayList<String> getNicknames() {
        ArrayList<String> nicknames = new ArrayList<>();
        for(Integer id : clientsId) {
            nicknames.add(mapIdNickname.get(id));
        }
        return nicknames;
        //Capire se mi basta fare return (ArrayList<Mage>) mapIdNickname.values();
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public boolean isMode() {
        return mode;
    }

    public void sendUpdate(Update u){

    }
}