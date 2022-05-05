package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.server.answer.Update.Update;
import it.polimi.ingsw.server.virtualview.VirtualIsland;

public class UpdateIsland implements Update {
    private final VirtualIsland island;

    public UpdateIsland(VirtualIsland island) {
        this.island = island;
    }

    public VirtualIsland getIsland() {
        return island;
    }
}
