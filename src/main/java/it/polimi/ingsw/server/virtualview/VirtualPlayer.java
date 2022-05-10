package it.polimi.ingsw.server.virtualview;

import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Hand;
import it.polimi.ingsw.model.player.Player;

/** VirtualPlayer class is a simplified representation of a player.*/
public class VirtualPlayer {

    private final String nickname;
    private final int virtualCoins;
    private final Assistant virtualLastAssistant;
    private final Hand virtualHand;
    private final VirtualSchoolBoard virtualBoard;

    /**Constructor VirtualPlayer creates a new VirtualPlayer instance.*/
    public VirtualPlayer(Player player) {
        this.virtualCoins = player.getCoins();
        this.virtualLastAssistant = player.getLastAssistant();
        this.virtualHand = player.getMyCards();
        this.virtualBoard = new VirtualSchoolBoard(player.getMyBoard());
        this.nickname = player.getNickname();
    }

    /**
     * Method getVirtualCoins returns virtual player's coins
     * @return virtualCoins - virtual player's coins
     */
    public int getVirtualCoins() {
        return virtualCoins;
    }

    /**
     * Method getVirtualLastAssistant returns the Assistant card played by the virtual player
     * @return virtualLastAssistant - the last Assistant card played
     */
    public Assistant getVirtualLastAssistant() {
        return virtualLastAssistant;
    }

    /**
     * Method getVirtualHand returns the virtual player's available Assistant cards
     * @return virtualHand - virtual player's hand
     */
    public Hand getVirtualHand() {
        return virtualHand;
    }

    /**
     * Method getVirtualBoard returns the virtual player's board
     * @return virtualBoard - virtual player's board
     */
    public VirtualSchoolBoard getVirtualBoard() {
        return virtualBoard;
    }

    /**
     * Method getNickname returns the virtual player's nickname
     * @return virtualBoard - virtual player's nickname
     */
    public String getNickname() {
        return nickname;
    }
}
