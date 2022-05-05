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
    }

    public void handleClientDisconnection(int clientId) {
        /*
        if (!gameStarted) {
            mapIdLobby.get(clientId).handleDisconnection(clientId);
        } else {
            getControllerByIdClient(clientId).setInactivePlayer(mapIdNickname.get(clientId));
        }
        magari basta fare:
        mapIdLobby.get(clientId).handleDisconnection(clientId);
        e poi gestisco tutto nella lobby
         */
    }

    public void handleJoin(Join join, int clientId) {
        if(mapIdLobby.containsKey(clientId) && !mapIdLobby.get(clientId).isDisconnected(clientId)) {
            sendMessage(clientId, "Error: you are already in a lobby");
        } else if(lobbies.isEmpty()) {
            sendMessage(clientId, "Error: there is no lobby available, please create a new one");
        //Capire come gestire le riconessioni, si può fare o nel metodo addPlayer che verifica che quel giocatore sia uguale ad uno che esiste
        //già nella lobby oppure facendo un altro if simile al primo in cui chiamo un metodo che mi pone ad active tale client
        } else if(join.getIndex() < 0 || join.getIndex() > lobbies.size()) {
            boolean ris = lobbies.get(join.getIndex()).addPlayer(join, mapIdSocket.get(clientId), clientId);
            if (ris) {
                mapIdLobby.put(clientId, lobbies.get(join.getIndex()));
                forwardMessage(join, clientId);
            }
        }
    }

    public void forwardMessage(Request request, int clientID) {
        if(mapIdLobby.containsKey(clientID)) {
            mapIdLobby.get(clientID).handleMessage(request, clientID);
        } else {
            sendMessage(clientID, "Error: you are not in a lobby, please send a Join or GameParams request");
        }
    }

    public void createLobby(GameParams gameParams, int clientId) {
        if(mapIdLobby.containsKey(clientId)) {
            sendMessage(clientId, "Error: you are already in a lobby");
        } else {
            System.out.println("Creazione di una nuova lobby");
            Lobby lobby = new Lobby(gameParams, mapIdSocket.get(clientId), clientId);
            lobbies.add(lobby);
            mapIdLobby.put(clientId, lobby);
            System.out.println("La lobby è stata creata correttamente");
        }
    }

    //TODO cambiare questo metodo e anche la classe Welcome
    public Answer getLobbies() {
        //Not final, it needs to be improved
        //Se voglio i nickname, maghi e torri posso o fare un set/mappa di queste cose?
        return new Welcome(lobbies);
    }

    //TODO capire cosa è meglio tra inviare una stringa e poi vi creo un messaggio generico che ha una stringa o fare una sendMessage che prende
    //in ingresso una Answer e quindi sono gli altri a crearsi l'oggetto Error o simili e glielo passano già fatto al metodo
    public void sendMessage(int clientID, String content){
        Answer answer = new Error(content);
        mapIdSocket.get(clientID).sendMessage(answer);
    }
}
