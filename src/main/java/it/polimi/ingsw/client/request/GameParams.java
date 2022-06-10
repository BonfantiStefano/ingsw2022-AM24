package it.polimi.ingsw.client.request;

import it.polimi.ingsw.model.ColorT;
import it.polimi.ingsw.model.player.Mage;

/**
 * GameParams class contains all the parameters used to create the lobby.
 */
public class GameParams{
    private final int numPlayers;
    private final boolean expert;
    private final String nickname;
    private final Mage mage;
    private final ColorT colorT;

    /**
     * Constructor GameParams creates a new GameParams instance.
     * @param numPlayers int - the number of the player of the game.
     * @param expert boolean - true if the game will be in expert mode, false otherwise.
     * @param nickname String - the player's nickname.
     * @param mage Mage - the mage chosen by the player.
     * @param colorT ColorT - the color of the tower.
     */
    public GameParams(int numPlayers, boolean expert, String nickname, Mage mage, ColorT colorT) {
        this.numPlayers = numPlayers;
        this.expert = expert;
        this.nickname = nickname;
        this.mage = mage;
        this.colorT = colorT;
    }

    /**
     * Method getColorT returns the color of the towers chosen.
     * @return ColorT - towers' color.
     */
    public ColorT getColorT() {
        return colorT;
    }

    /**
     * Method getNumPlayers returns the number of players chosen.
     * @return int - number of players.
     */
    public int getNumPlayers() {
        return numPlayers;
    }

    /**
     * Method isExpert returns true if the game is in expert mode, false otherwise.
     * @return boolean - expert mode.
     */
    public boolean isExpert() {
        return expert;
    }

    /**
     * Method getNickname returns the player's nickname.
     * @return String - player's nickname.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Method getMage returns the mage chosen.
     * @return Mage - mage chosen.
     */
    public Mage getMage() {
        return mage;
    }

}
