package it.polimi.ingsw.client.request;

import it.polimi.ingsw.model.ColorS;

public class MoveToIsland implements Request {
    private ColorS colorS;
    private int index;

    public MoveToIsland(ColorS colorS, int index) {
        this.colorS = colorS;
        this.index = index;
    }

    public ColorS getColorS() {
        return colorS;
    }

    public int getIndex() {
        return index;
    }
}
