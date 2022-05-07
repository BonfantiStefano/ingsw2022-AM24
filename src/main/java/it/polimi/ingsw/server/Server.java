package it.polimi.ingsw.server;

import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.server.answer.Answer;
import it.polimi.ingsw.server.answer.Error;
import it.polimi.ingsw.server.answer.Information;
import it.polimi.ingsw.server.answer.Welcome;
import it.polimi.ingsw.server.virtualview.VirtualLobby;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Not final, work in progress
//TODO implementare metodo per gestire il caso in cui non riesce a connettersi a nessun client e quindi deve spegnersi, tutte le lobby le eliminiamo
public class Server {
    private ExecutorService executorService;
    private Map<Integer, SocketClientHandler> mapIdSocket;
    private Map<Integer, Lobby> mapIdLobby;
    private ArrayList<Lobby> lobbies;
    private int idClients;

    public Server() {
        executorService = Executors.newCachedThreadPool();
        mapIdSocket = new HashMap<>();
        mapIdLobby = new HashMap<>();
        lobbies = new ArrayList<>();
    }

    public void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Server started on port " + port);
                while (true) {
                    //Until the server is stopped, he keeps accepting new connections from clients who connect to its socket
                    try {
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("A client is connected for the server");
                        SocketClientHandler socketClientHandler = new SocketClientHandler(clientSocket, this, idClients);
                        mapIdSocket.put(idClients, socketClientHandler);
                        executorService.submit(socketClientHandler);
                        idClients++;
                    } catch (IOException e) {
                        System.out.println("An exception caused the server to stop working.");
                        System.exit(0);
                    }
                }
        } catch (IOException e) {
            System.err.println("Error during Socket initialization, the application will close");
            System.exit(0);
        }
    }

    public void handleClientDisconnection(int clientId) {
        mapIdLobby.get(clientId).handleDisconnection(clientId);
        mapIdSocket.remove(clientId);
    }

    public void handleJoin(Join join, int clientId) {
        if(mapIdLobby.containsKey(clientId)) {
            //Case when a client has already sent a good GameParams or join
            sendMessage(clientId, new Error("Error: you are already in a lobby"));
        } else if(lobbies.isEmpty()) {
            //Case when the first client connects to the server
            sendMessage(clientId, new Error("Error: there is no lobby available, please create a new one"));
        } else if(join.getIndex() >= 0 && join.getIndex() < lobbies.size() && lobbies.get(join.getIndex()).isPresent(join.getNickname())){
            //Case when a client maybe is a disconnected player
            int oldClientId = lobbies.get(join.getIndex()).checkReconnection(join, mapIdSocket.get(clientId), clientId);
            if(oldClientId != -1) {
                mapIdLobby.remove(oldClientId);
                mapIdLobby.put(clientId, lobbies.get(join.getIndex()));
            }
        } else if(join.getIndex() >= 0 && join.getIndex() < lobbies.size()) {
            //Standard case when a player want to join an available lobby
            System.out.println("Provo ad aggiungere il player alla lobby");
            boolean ris = lobbies.get(join.getIndex()).addPlayer(join, mapIdSocket.get(clientId), clientId);
            if (ris) {
                mapIdLobby.put(clientId, lobbies.get(join.getIndex()));
                System.out.println("Il player è stato aggiunto correttamente");
            }
        } else {
            sendMessage(clientId, new Error("Error: invalid lobby index, please retry"));
            sendMessage(clientId, getLobbies());
        }
    }

    public void forwardMessage(Request request, int clientID) {
        if(mapIdLobby.containsKey(clientID)) {
            mapIdLobby.get(clientID).handleMessage(request, clientID);
        } else {
            sendMessage(clientID, new Error("Error: you are not in a lobby, please send a Join or GameParams request"));
        }
    }

    public void createLobby(GameParams gameParams, int clientId) {
        if(mapIdLobby.containsKey(clientId)) {
            sendMessage(clientId, new Error("Error: you are already in a lobby"));
        } else {
            Lobby lobby = new Lobby(gameParams, mapIdSocket.get(clientId), clientId);
            lobbies.add(lobby);
            mapIdLobby.put(clientId, lobby);
            System.out.println("La lobby è stata creata correttamente");
            sendMessage(clientId, new Information("The lobby has been created"));
        }
    }

    public Answer getLobbies() {
        ArrayList<VirtualLobby> virtualLobbies = new ArrayList<>();
        for(Lobby lobby : lobbies) {
            if(lobby.getGameStatus().equals(GameStatus.SETUP)) {
                virtualLobbies.add(new VirtualLobby(lobby.getNicknames(), lobby.getMages(), lobby.getColorTowers(),
                        lobby.getNumPlayers(), lobby.isMode(), lobbies.indexOf(lobby)));
            }
        }
        return new Welcome(virtualLobbies);
    }

    public void sendMessage(int clientID, Answer answer){
        mapIdSocket.get(clientID).sendMessage(answer);
    }
}
