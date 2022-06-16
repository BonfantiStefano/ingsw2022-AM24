package it.polimi.ingsw.server.virtualview;

import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;
import it.polimi.ingsw.server.GameStatus;

import java.util.ArrayList;

/** VirtualLobby class is a simplified representation of a lobby.*/
public class VirtualLobby {
    private final ArrayList<String> nicknames;
    private final int numPlayers;
    private final ArrayList<Mage> mages;
    private final ArrayList<ColorT> towers;
    private final boolean mode;
    private final GameStatus gameStatus;
    private final int lobbyIndex;

    /**Constructor VirtualLobby creates a new VirtualLobby instance.*/
    public VirtualLobby(ArrayList<String> nicknames, ArrayList<Mage> mages, ArrayList<ColorT> towers, int numPlayers, boolean mode, int lobbyIndex, GameStatus gameStatus) {
        this.nicknames = nicknames;
        this.numPlayers = numPlayers;
        this.mages = mages;
        this.towers = towers;
        this.mode = mode;
        this.lobbyIndex = lobbyIndex;
        this.gameStatus = gameStatus;
    }

    /**
     * Method getLobbyIndex returns the number of the virtual Lobby
     * @return lobbyIndex - the virtual lobby's index
     */
    public int getLobbyIndex() {
        return lobbyIndex;
    }

    /**
     * Method getNicknames returns the list containing the nicknames of all the players that stay in the virtual lobby
     * @return the players' nicknames
     */
    public ArrayList<String> getNicknames() {
        return nicknames;
    }

    /**
     * Method getNumPlayers returns the number of the players in the virtual lobby
     * @return the number of the players
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Method getMages returns the list containing the mages owned by each player of the virtual lobby
     * @return mages of the players
     */
    public ArrayList<Mage> getMages() {
        return mages;
    }

    /**
     * Method getTowers returns the list containing the colors chosen by each player for their towers.
     * @return the towers' colors of the players
     */
    public ArrayList<ColorT> getTowers() {
        return towers;
    }

    /**
     * Method isMode returns if the game is played with expert rules
     * @return mode of type boolean - true if the game is actually played with expert rules, false otherwise
     */
    public boolean isMode() {
        return mode;
    }

    /**
     * Method getGameStatus returns the status of the game.
     * @return a GameStatus value.
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }
}
