package it.polimi.ingsw.server;

import it.polimi.ingsw.client.request.*;
import it.polimi.ingsw.server.answer.Answer;
import it.polimi.ingsw.server.answer.Error;
import it.polimi.ingsw.server.answer.Welcome;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Not final, work in progress
public class Server {
    private ExecutorService executorService;
    private Map<Integer, SocketClientHandler> mapIdSocket;
    private Map<Integer, Lobby> mapIdLobby;
    private ArrayList<Lobby> lobbies;
    private ServerSocket serverSocket;
    private int idClients;

    public Server() {
        executorService = Executors.newCachedThreadPool();
        mapIdSocket = new HashMap<>();
        mapIdLobby = new HashMap<>();
        lobbies = new ArrayList<>();
    }

    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            System.err.println("Error during Socket initialization, the application will close");
            System.exit(0);
        }
        //Thread t = new Thread(()-> {
            while (true) {
                //Until the server is stopped, he keeps accepting new connections from clients who connect to its socket
                try {
                    System.out.println("Waiting a new client");
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
        //});
        //t.start();
    }

    public void handleClientDisconnection(int clientId) {
        /*
        if (!gameStarted) {
            getLobbyByIdClient(clientId).remove(clientId);
        } else {
            getControllerByIdClient(clientId).setInactivePlayer(mapIdNickname.get(clientId));
        }
         */
    }

    public boolean handleJoin(Join join, int clientId) {
        //per gestire i vari casi di errori separatamente poteri usare if(lobbies.get(join.getIndex()).getFull()) ...
        if(join.getIndex() < 0 || join.getIndex() > lobbies.size()) {
            boolean ris = lobbies.get(join.getIndex()).addPlayer(join, mapIdSocket.get(clientId), clientId);
            if (ris) {
                mapIdLobby.put(clientId, lobbies.get(join.getIndex()));
            }
            return ris;
        }
        return false;
    }

    public void forwardMessage(Request request, int clientID) {
        if(mapIdLobby.containsKey(clientID)) {
            mapIdLobby.get(clientID).handleMessage(request, clientID);
        } else {
            sendMessage(clientID, "Error: you are not in a lobby, please send a Join or GameParams request");
        }
    }

    public void createLobby(GameParams gameParams, int clientId) {
        Lobby lobby = new Lobby(gameParams, mapIdSocket.get(clientId), clientId);
        lobbies.add(lobby);
        mapIdLobby.put(clientId, lobby);
    }

    public Answer getLobbies() {
        //Not final, it needs to be improved
        return new Welcome(lobbies);
    }

    public void sendMessage(int clientID, String content){
        Answer answer = new Error(content);
        mapIdSocket.get(clientID).sendMessage(answer);
    }
}
