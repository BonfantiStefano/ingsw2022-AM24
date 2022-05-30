package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.virtualview.VirtualPlayer;

/**
 * Class AddPlayer is used from the server to notify a virtualView's update to the client.
 */
public class AddPlayer implements Update {
    private final VirtualPlayer player;

    /**
     * Constructor AddPlayer creates a new update message.
     * @param player VirtualPlayer - the player that must be added to the virtualView.
     */
    public AddPlayer(VirtualPlayer player) {
        this.player = player;
    }

    /**
     * Method getPlayer returns the player.
     * @return VirtualPlayer - the player.
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
