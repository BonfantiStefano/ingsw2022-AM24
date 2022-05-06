package it.polimi.ingsw.server.virtualview;

import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;

import java.util.ArrayList;

public class VirtualLobby {
    private ArrayList<String> nicknames;
    private int numPlayers;
    private ArrayList<Mage> mages;
    private ArrayList<ColorT> towers;
    private boolean mode;
    private int lobbyIndex;

    public VirtualLobby(ArrayList<String> nicknames, ArrayList<Mage> mages, ArrayList<ColorT> towers, int numPlayers, boolean mode, int lobbyIndex) {
        this.nicknames = nicknames;
        this.numPlayers = numPlayers;
        this.mages = mages;
        this.towers = towers;
        this.mode = mode;
        this.lobbyIndex = lobbyIndex;
    }

    public int getLobbyIndex() {
        return lobbyIndex;
    }

    public ArrayList<String> getNicknames() {
        return nicknames;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public ArrayList<Mage> getMages() {
        return mages;
    }

    public ArrayList<ColorT> getTowers() {
        return towers;
    }

    public boolean isMode() {
        return mode;
    }
}
