package it.polimi.ingsw.client.request;

import it.polimi.ingsw.model.ColorS;

public class EntranceToHall implements Request{
    private ColorS colorS;

    public EntranceToHall(ColorS colorS) {
        this.colorS = colorS;
    }

    public ColorS getColorS() {
        return colorS;
    }
}
