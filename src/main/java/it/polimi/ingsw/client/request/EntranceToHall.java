package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.controllers.Controller;
import it.polimi.ingsw.model.ColorS;

public class EntranceToHall implements Request{
    private final ColorS colorS;

    public EntranceToHall(ColorS colorS) {
        this.colorS = colorS;
    }

    public ColorS getColorS() {
        return colorS;
    }

    @Override
    public void accept(Controller c) {
        c.visit(this);
    }
}
