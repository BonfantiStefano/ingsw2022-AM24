package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.server.virtualview.VirtualView;

public class FullView implements Update{
    private VirtualView virtualView;

    public FullView(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    public VirtualView getVirtualView() {
        return virtualView;
    }

    @Override
    public void accept(Client c) {
        c.visit(this);
    }
}
