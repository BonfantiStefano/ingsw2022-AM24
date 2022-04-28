package it.polimi.ingsw.client.request;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.ColorS;

public class ChooseColor implements Request {
    private final ColorS color;

    public ChooseColor(ColorS color){
       this.color = color;
    }

    public ColorS getColor() {
        return color;
    }

    @Override
    public void accept(Controller c) {
        c.visit(this);
    }
}
