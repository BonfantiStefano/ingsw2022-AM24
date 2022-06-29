package it.polimi.ingsw.server.answer;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.virtualview.VirtualLobby;

import java.util.ArrayList;

/**
 * Class Welcome is used from the server to notify the client of the available lobbies.
 */
public class Welcome implements Answer{
    private final ArrayList<VirtualLobby> lobbies;

    /**
     * Constructor Welcome creates a new message with the information of all the available lobbies.
     * @param lobbies an ArrayList containing all the available lobbies.
     */
    public Welcome(ArrayList<VirtualLobby> lobbies) {
        this.lobbies = new ArrayList<>(lobbies);
    }

    /**
     * Method getLobbies returns the ArrayList containing all the lobbies.
     * @return the ArrayList containing all the lobbies.
     */
    public ArrayList<VirtualLobby> getLobbies() {
        return lobbies;
    }

    /**
     * Method accept is used to pass the correct type to the visitor.
     * @param c Client - the object that will handle the message.
     */
    @Override
    public void accept(Client c) {
        c.visit(this);
    }
}
