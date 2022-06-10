package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.virtualview.VirtualPlayer;

/**
 * Class UpdatePlayer is used from the server to notify about the game's players to the client.
 */
public class UpdatePlayer implements Update{
    private final VirtualPlayer player;
    private final int index;

    /**
     * Constructor UpdateIsland creates a new update message.
     * @param player VirtualPlayer - all the player's data.
     * @param index int - the island's index.
     */
    public UpdatePlayer(VirtualPlayer player, int index) {
        this.player = player;
        this.index = index;
    }

    /**
     * Method getIndex returns the index of the Player.
     * @return the index of the Island.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Method getPlayer returns the VirtualPlayer.
     * @return the VirtualPlayer.
     */
    public VirtualPlayer getPlayer() {
        return player;
    }

    /**
     * Method accept is used to pass the correct type to the visitor.
     * @param c Client - the object that will handle the message.
     */
    @Override
    public void accept(Client c){
        c.visit(this);
    }
}
