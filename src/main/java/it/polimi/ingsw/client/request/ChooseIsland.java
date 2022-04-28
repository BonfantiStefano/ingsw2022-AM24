package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.Controller;

public class ChooseIsland implements Request {
    private final int islandIndex;

    public ChooseIsland(int islandIndex) {
        this.islandIndex = islandIndex;
    }

    public int getIslandIndex(){
        return islandIndex;
    }

    @Override
    public void accept(Controller c) {
        c.visit(this);
    }
}
