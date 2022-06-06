package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.virtualview.VirtualIsland;

/**
 * Class UpdateIsland is used from the server to notify about the game's islands to the client.
 */
public class UpdateIsland implements Update {
    private final VirtualIsland island;
    private final int index;

    /**
     * Constructor UpdateIsland creates a new update message.
     * @param island VirtualIsland - all the data contained on the island.
     * @param index int - the island's index.
     */
    public UpdateIsland(VirtualIsland island, int index) {
        this.island = island;
        this.index = index;
    }

    /**
     * Method getIndex returns the index of the Island.
     * @return the index of the Island.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Method getIsland returns the VirtualIsland.
     * @return the VirtualIsland.
     */
    public VirtualIsland getIsland() {
        return island;
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
