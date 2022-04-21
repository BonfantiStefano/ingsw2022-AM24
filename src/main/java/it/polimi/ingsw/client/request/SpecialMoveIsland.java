package it.polimi.ingsw.client.request;

import it.polimi.ingsw.model.ColorS;

public class SpecialMoveIsland implements Request{
    private ColorS student;
    private int islandIndex;

    public ColorS getStudent() {
        return student;
    }
    public int getIslandIndex(){
        return islandIndex;
    }
}
