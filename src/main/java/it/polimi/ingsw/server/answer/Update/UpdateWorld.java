package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.virtualview.VirtualIsland;

import java.util.ArrayList;

/**
 * Class UpdateWorld is used from the server to notify about the game's world to the client.
 */
public class UpdateWorld implements Update{
    private final ArrayList<VirtualIsland> islands;

    /**
     * Constructor UpdateWorld creates a new update message.
     * @param islands ArrayList - all the islands.
     */
    public UpdateWorld(ArrayList<VirtualIsland> islands) {
        this.islands = islands;
    }

    /**
     * Method getIslands returns all the VirtualIsland.
     * @return all the VirtualIsland.
     */
    public ArrayList<VirtualIsland> getIslands() {
        return islands;
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
