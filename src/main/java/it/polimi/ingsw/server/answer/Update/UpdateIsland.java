package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.server.answer.Update.Update;
import it.polimi.ingsw.server.virtualview.VirtualIsland;

public class UpdateIsland implements Update {
    private final VirtualIsland island;
    private final int index;

    public UpdateIsland(VirtualIsland island, int index) {
        this.island = island;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public VirtualIsland getIsland() {
        return island;
    }
}
