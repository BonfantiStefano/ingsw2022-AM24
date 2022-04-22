package it.polimi.ingsw.client.request;

import it.polimi.ingsw.model.ColorS;

public class ChooseColor implements Request {
    private ColorS color;

    public ChooseColor(ColorS color){
       this.color = color;
    }

    public ColorS getColor() {
        return color;
    }

}
