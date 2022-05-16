package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.CLIView.CLI;
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
    public void accept(CLI c) {
        c.visit(this);
    }
}