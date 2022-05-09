package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.controllers.Controller;
import it.polimi.ingsw.model.ColorS;

public class MoveToIsland implements Request {
    private final ColorS colorS;
    private final int index;

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

    @Override
    public void accept(Controller c) {
        c.visit(this);
    }
}
