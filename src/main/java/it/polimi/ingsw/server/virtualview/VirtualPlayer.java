package it.polimi.ingsw.server.virtualview;

import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Hand;
import it.polimi.ingsw.model.player.Player;

/** VirtualPlayer class is a simplified representation of a player.*/
public class VirtualPlayer {

    private int virtualCoins;
    private Assistant virtualLastAssistant;
    private Hand virtualHand;
    private VirtualSchoolBoard virtualBoard;

    /**Constructor VirtualPlayer creates a new VirtualPlayer instance.*/
    public VirtualPlayer(Player player) {
        this.virtualCoins = player.getCoins();
        this.virtualLastAssistant = player.getLastAssistant();
        this.virtualHand = player.getMyCards();
        this.virtualBoard = new VirtualSchoolBoard(player.getMyBoard());
    }


}
