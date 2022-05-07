package it.polimi.ingsw.server.answer.Update;

import it.polimi.ingsw.client.CLIView.CLI;
import it.polimi.ingsw.server.virtualview.VirtualIsland;

import java.util.ArrayList;

public class UpdateWorld implements Update{
    private final ArrayList<VirtualIsland> islands;

    public UpdateWorld(ArrayList<VirtualIsland> islands) {
        this.islands = islands;
    }

    public ArrayList<VirtualIsland> getIslands() {
        return islands;
    }

    @Override
    public void accept(CLI c){
        c.visit(this);
    }
}
