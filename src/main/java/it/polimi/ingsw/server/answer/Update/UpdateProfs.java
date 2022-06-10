package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.model.ColorS;
import it.polimi.ingsw.server.virtualview.VirtualPlayer;

import java.util.HashMap;

/**
 * Class UpdateProfs is used from the server to notify about the profs to the client.
 */
public class UpdateProfs implements Update{
    private final HashMap<ColorS, VirtualPlayer> profs;

    /**
     * Constructor UpdateIsland creates a new update message.
     * @param profs HashMap - a map with the relationship owner - prof.
ù     */
    public UpdateProfs(HashMap<ColorS, VirtualPlayer> profs) {
        this.profs = profs;
    }

    /**
     * Method getProfs returns the Map with the relationship owner - prof.
     * @return the Map with the relationship owner - prof.
     */
    public HashMap<ColorS, VirtualPlayer> getProfs() {
        return profs;
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
