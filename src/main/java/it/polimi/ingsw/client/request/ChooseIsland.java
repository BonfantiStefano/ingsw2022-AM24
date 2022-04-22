package it.polimi.ingsw.client.request;

public class ChooseIsland implements Request {
    private int islandIndex;

    public ChooseIsland(int islandIndex) {
        this.islandIndex = islandIndex;
    }

    public int getIslandIndex(){
        return islandIndex;
    }
}
