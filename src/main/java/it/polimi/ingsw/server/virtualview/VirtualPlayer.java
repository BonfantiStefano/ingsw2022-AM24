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

    public int getVirtualCoins() {
        return virtualCoins;
    }

    public Assistant getVirtualLastAssistant() {
        return virtualLastAssistant;
    }

    public Hand getVirtualHand() {
        return virtualHand;
    }

    public VirtualSchoolBoard getVirtualBoard() {
        return virtualBoard;
    }

    public String getNickname() {
        return nickname;
    }
}
