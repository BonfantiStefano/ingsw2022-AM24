package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.virtualview.VirtualCloud;

/**
 * Class ReplaceCloud is used from the server to notify about a cloud's update to the client.
 */
public class ReplaceCloud implements Update{
    private final VirtualCloud cloud;
    private final int index;

    /**
     * Constructor ReplaceCharacter creates a new update message.
     * @param cloud VirtualCloud - all the data of the cloud.
     * @param index int - the index of the cloud.
     */
    public ReplaceCloud(VirtualCloud cloud, int index) {
        this.cloud = cloud;
        this.index = index;
    }

    /**
     * Method getCloud returns the VirtualCloud.
     * @return the VirtualCloud contained in the message.
     */
    public VirtualCloud getCloud() {
        return cloud;
    }

    /**
     * Method getIndex returns the VirtualCloud's index.
     * @return the index of the VirtualCloud.
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
