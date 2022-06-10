package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;

/**
 * Class UpdateMN is used from the server to notify about mother nature location to the client.
 */
public class UpdateMN implements Update{
    private final int index;

    /**
     * Constructor UpdateIsland creates a new update message.
     * @param index int - the island's index where mother nature is.
     */
    public UpdateMN(int index) {
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
     * Method accept is used to pass the correct type to the visitor.
     * @param c Client - the object that will handle the message.
     */
    @Override
    public void accept(Client c){
        c.visit(this);
    }
}
