package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.virtualview.VirtualCloud;

import java.util.ArrayList;

/**
 * Class CreateClouds is used from the server to notify about a virtualView's update to the client.
 */
public class CreateClouds implements Update{
    ArrayList<VirtualCloud> clouds;

    /**
     * Constructor AddPlayer creates a new update message.
     * @param clouds ArrayList - the List of clouds that must be added to the virtualView.
     */
    public CreateClouds(ArrayList<VirtualCloud> clouds) {
        this.clouds = clouds;
    }

    /**
     * Method getClouds returns the List of the clouds.
     * @return ArrayList - the List of the clouds.
     */
    public ArrayList<VirtualCloud> getClouds() {
        return clouds;
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
