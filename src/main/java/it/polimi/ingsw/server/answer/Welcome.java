package it.polimi.ingsw.server.answer;

import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.server.virtualview.VirtualLobby;

import java.util.ArrayList;

public class Welcome implements Answer{
    private ArrayList<VirtualLobby> lobbies;

    public Welcome(ArrayList<VirtualLobby> lobbies) {
        this.lobbies = new ArrayList<>(lobbies);
    }
    /*
    //NOT FINAL, just for debug
    private int idLobby;
    private boolean expertMode;
    private int numPlayers;

    public Welcome(int idLobby, boolean expertMode, int numPlayers) {
        this.idLobby = idLobby;
        this.expertMode = expertMode;
        this.numPlayers = numPlayers;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public boolean isExpertMode() {
        return expertMode;
    }

    public int getIdLobby() {
        return idLobby;
    }

     */
}
