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
/**
 * Class Server used to manage new connecting clients, the forwarding of the messages from the socket that handle the client to the
 * right lobby.
 *
 * @author Bonfanti Stefano
 */
public class Server {
    private ExecutorService executorService;
    private Map<Integer, SocketClientHandler> mapIdSocket;
    private Map<Integer, Lobby> mapIdLobby;
    private ArrayList<Lobby> lobbies;
    private int idClients;

    /**
     * Constructor Server creates a new Server instance.
     */
    public Server() {
        executorService = Executors.newCachedThreadPool();
        mapIdSocket = new HashMap<>();
        mapIdLobby = new HashMap<>();
        lobbies = new ArrayList<>();
    }

    /**
     * Method startServer is used to start the server on the port given by parameter, then he keeps accepting new connections
     * form clients till the server is alive.
     * @param port int - the port where the server is started.
     */
    public void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Server started on port " + port);
                while (true) {
                    //Until the server is stopped, he keeps accepting new connections from clients who connect to its socket
                    try {
                        Socket clientSocket = serverSocket.accept();
                        SocketClientHandler socketClientHandler = new SocketClientHandler(clientSocket, this, idClients);
                        mapIdSocket.put(idClients, socketClientHandler);
                        executorService.submit(socketClientHandler);
                        System.out.println("Socket " + idClients + " aperto");
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

    /**
     * Method handleClientDisconnection calls the method handleDisconnection of the corresponding lobby then remove from the map
     * id-Socket this client id
     * @param clientId int - the id of the client who has to be disconnected.
     */
    public void handleClientDisconnection(int clientId) {
        mapIdLobby.get(clientId).handleDisconnection(clientId);
        mapIdSocket.remove(clientId);
    }

    /**
     * Method handleJoin manages a join message from a client, it also handles the re-connection of a client.
     * @param join Join - the join message sent by the client.
     * @param clientId int - the client's id that sent the join to the server.
     */
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
            boolean ris = lobbies.get(join.getIndex()).addPlayer(join, mapIdSocket.get(clientId), clientId);
            if (ris) {
                mapIdLobby.put(clientId, lobbies.get(join.getIndex()));
            }
        } else {
            sendMessage(clientId, new Error("Error: invalid lobby index, please retry"));
            //Capire se serve fare questo, poichè alcuni controlli li faccio già lato client
            sendMessage(clientId, getLobbies());
        }
    }

    /**
     * Method forwardMessage receives a request message from a client and sends it to the corresponding lobby.
     * @param request Request - the message that has to be forwarded.
     * @param clientID int - the client's id that sent the request to the server.
     */
    public void forwardMessage(Request request, int clientID) {
        if(mapIdLobby.containsKey(clientID)) {
            mapIdLobby.get(clientID).handleMessage(request, clientID);
        } else {
            sendMessage(clientID, new Error("Error: you are not in a lobby, please send a Join or GameParams request"));
        }
    }

    /**
     * Method createLobby is utilized to create a lobby based on the GameParams message received from a client.
     * @param gameParams GameParams - the message that is used to create the lobby.
     * @param clientId int - the client's id that sent the message to the server.
     */
    public void createLobby(GameParams gameParams, int clientId) {
        if(mapIdLobby.containsKey(clientId)) {
            sendMessage(clientId, new Error("Error: you are already in a lobby"));
        } else {
            Lobby lobby = new Lobby(gameParams, mapIdSocket.get(clientId), clientId);
            lobbies.add(lobby);
            mapIdLobby.put(clientId, lobby);
            sendMessage(clientId, new Information("The lobby has been created"));
            sendMessageToAll(getLobbies());
        }
    }

    private void sendMessageToAll(Answer lobbies) {
        for(Integer clientId : mapIdSocket.keySet()) {
            if(!mapIdLobby.containsKey(clientId)) {
                sendMessage(clientId, lobbies);
            }
        }
    }

    /**
     * Method getLobbies is used to create the Welcome message that must be sent to all the clients that connect to the server
     * @return Answer - the message containing all the lobbies available with their parameters.
     */
    public Answer getLobbies() {
        ArrayList<VirtualLobby> virtualLobbies = new ArrayList<>();
        for(Lobby lobby : lobbies) {
            if(!lobby.getGameStatus().equals(GameStatus.ENDED)) {
                virtualLobbies.add(new VirtualLobby(lobby.getNicknames(), lobby.getMages(), lobby.getColorTowers(),
                        lobby.getNumPlayers(), lobby.isMode(), lobbies.indexOf(lobby), lobby.getGameStatus()));
            }
        }
        return new Welcome(virtualLobbies);
    }

    /**
     * Method sendMessage sends a message to the client.
     * @param clientID int - the client's id that will receive the answer.
     * @param answer Answer - the message that has to be sent to the client.
     */
    public void sendMessage(int clientID, Answer answer){
        mapIdSocket.get(clientID).sendMessage(answer);
    }
}
