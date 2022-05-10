package it.polimi.ingsw.server.virtualview;

import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * VirtualView class is a simplified representation of the gameBoard (and the expert gameBoard).
 * It will update itself according to the state of the model each time an update event is emitted.
 */
public class VirtualView {

    private ArrayList<VirtualIsland> virtualWorld;
    private ArrayList<VirtualPlayer> virtualPlayers;
    private ArrayList<VirtualCloud> virtualClouds;
    private HashMap<ColorS, VirtualPlayer> virtualProfs;
    private ArrayList<VirtualCharacter> virtualCharacters;
    private int virtualCoins;
    int mnPos;

    /**Constructor VirtualView creates a new VirtualView instance.*/
    public VirtualView() {
        virtualWorld = new ArrayList<>();
        virtualPlayers = new ArrayList<>();
        virtualClouds = new ArrayList<>();
        virtualCharacters = new ArrayList<>();
        mnPos = -1;
        this.virtualCoins = 0;
    }

    /** Method setVirtualWorld is intended to be called whenever there is a change of an island of the World.
     * @param index - index of changed island
     * @param island - changed island
     */
    public void setVirtualWorld(int index, VirtualIsland island) {
        this.virtualWorld.set(index, island);
    }

    /** Method setVirtualClouds is intended to be called whenever there is a change of Clouds.
     * @param virtualClouds - changed clouds
     */
    public void setVirtualClouds(ArrayList<VirtualCloud> virtualClouds) {
        this.virtualClouds = virtualClouds;
    }

    /** Method setVirtualWorld is intended to be called whenever there is a change of World.
     * @param virtualWorld - changed world
     */
    public void setVirtualWorld(ArrayList<VirtualIsland> virtualWorld) {
        this.virtualWorld = virtualWorld;
    }

    /** Method setMnPos is intended to be called whenever there is a change of the position of Mother Nature.
     * @param mnPos - changed mother nature's position
     */
    public void setMnPos(int mnPos) {
        this.mnPos = mnPos;
    }

    /** Method addVirtualPlayer is intended to be called whenever a player is added in the game.
     * @param virtualPlayer - added player
     */
    public void addVirtualPlayer(VirtualPlayer virtualPlayer){
        virtualPlayers.add(virtualPlayer);
    }

    /** Method addVirtualPlayer is intended to be called whenever a player changes his state.
     * @param index - index of changed player
     * @param player - changed player
     */
    public void setVirtualPlayers(int index, VirtualPlayer player) {
        if(index < virtualPlayers.size())
           this.virtualPlayers.set(index, player);
        else{
            virtualPlayers.add(player);
        }
    }

    /** Method setVirtualClouds is intended to be called whenever a cloud changes its state.
     * @param index - index of changed cloud
     * @param cloud - changed cloud
     */
    public void setVirtualClouds(int index, VirtualCloud cloud) {
        if(index < virtualClouds.size())
            this.virtualClouds.set(index, cloud);
        else
            this.virtualClouds.add(cloud);
    }

    /** Method setVirtualProfs is intended to be called whenever there is a change of the of professors.
     * @param virtualProfs - changed profs
     */
    public void setVirtualProfs(HashMap<ColorS, VirtualPlayer> virtualProfs) {
        this.virtualProfs = virtualProfs;
    }

    /** Method setVirtualCharacters is intended to be called whenever a Character card changes its state.
     * @param index - index of changed card
     * @param character - changed card
     */
    public void setVirtualCharacters(int index, VirtualCharacter character) {
        if(index < virtualCharacters.size())
            this.virtualCharacters.set(index, character);
        else
            this.virtualCharacters.add(character);
    }

    /** Method setVirtualCharacters is intended to be called whenever there is a change of Character cards
     * available during the game.
     * @param virtualCharacters - changed cards
     */
    public void setVirtualCharacters(ArrayList<VirtualCharacter> virtualCharacters) {
        this.virtualCharacters = virtualCharacters;
    }

    /** Method setVirtualCoins is intended to be called whenever there is a change of the amount of coins.
     * @param coins - updated coins
     */
    public void setVirtualCoins(int coins){
        this.virtualCoins = coins;
    }

    /** Method getVirtualCoins returns the amount of available coins.
     * @return virtualCoins - available coins
     */
    public int getVirtualCoins() {
        return virtualCoins;
    }

    /** Method getVirtualPlayers returns the players.
     * @return virtualPlayers - players
     */
    public ArrayList<VirtualPlayer> getVirtualPlayers() {
        return virtualPlayers;
    }
    /** Method getVirtualWorld returns the World.
     * @return virtualWorld - world
     */
    public ArrayList<VirtualIsland> getVirtualWorld() {
        return virtualWorld;
    }

    /** Method getVirtualProfs returns the profs.
     * @return virtualProfs - profs
     */
    public HashMap<ColorS, VirtualPlayer> getVirtualProfs() {
        return virtualProfs;
    }

    /** Method getVirtualClouds returns the clouds.
     * @return virtualPlayers - clouds
     */
    public ArrayList<VirtualCloud> getVirtualClouds() {
        return virtualClouds;
    }

    /**
     * Method getVirtualCharacters returns the Characters
     * @return virtualCharacters - Characters
     */
    public ArrayList<VirtualCharacter> getVirtualCharacters(){return virtualCharacters;}

    /** Method getMnPos returns the mother nature's position.
     * @return mnPos - mother nature's position
     */
    public int getMnPos() {
        return mnPos;
    }

    /** Method setVirtualPlayers is intended to be called whenever there is a change of players.
     * @param virtualPlayers - changed players
     */
    public void setVirtualPlayers(ArrayList<VirtualPlayer> virtualPlayers) {
        this.virtualPlayers = virtualPlayers;
    }

}
