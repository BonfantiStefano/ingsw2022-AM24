package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.virtualview.VirtualView;

/**
 * Class FullView notifies the client about the game's view.
 */
public class FullView implements Update{
    private final VirtualView virtualView;

    /**
     * Constructor AddPlayer creates a new update message.
     * @param virtualView VirtualView - all the data of the virtual view.
     */
    public FullView(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    /**
     * Method getVirtualView returns the virtualView.
     * @return the virtualView contained in the message.
     */
    public VirtualView getVirtualView() {
        return virtualView;
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
